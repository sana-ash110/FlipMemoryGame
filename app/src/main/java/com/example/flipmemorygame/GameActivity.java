package com.example.flipmemorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipmemorygame.adapter.CardAdapter;
import com.example.flipmemorygame.model.Card;
import com.example.flipmemorygame.model.GameLevel;
import com.example.flipmemorygame.utils.CardDeck;
import com.example.flipmemorygame.utils.ScoreCalculator;
import com.example.flipmemorygame.utils.SoundManager;

import java.util.List;
import java.util.Locale;

/**
 * GameActivity.java - Core game screen.
 *
 * Handles:
 *  - Building and displaying the card grid
 *  - Flip logic (first card, second card, match/mismatch)
 *  - Mismatch delay using Handler
 *  - Live timer
 *  - Win detection and dialog
 *  - Saving high scores
 */
public class GameActivity extends AppCompatActivity
        implements CardAdapter.OnCardClickListener {

    // ── Game state ───────────────────────────────────────────────────────────
    private GameLevel   level;
    private List<Card>  cards;
    private CardAdapter adapter;

    private int     firstFlippedPos = -1;   // -1 means no card flipped yet
    private boolean isProcessing    = false; // blocks taps during mismatch delay

    private int  moves        = 0;
    private int  matchedPairs = 0;
    private long startTimeMs  = 0;
    private long elapsedMs    = 0;

    // ── Helpers ──────────────────────────────────────────────────────────────
    private final Handler handler = new Handler(Looper.getMainLooper());
    private SoundManager soundManager;

    // ── Views ────────────────────────────────────────────────────────────────
    private RecyclerView recyclerView;
    private TextView tvLevel, tvMoves, tvTimer;
    private Button btnRestart;

    // ── Timer runnable ───────────────────────────────────────────────────────
    private final Runnable timerTick = new Runnable() {
        @Override
        public void run() {
            elapsedMs = System.currentTimeMillis() - startTimeMs;
            tvTimer.setText("⏱ " + formatTime(elapsedMs / 1000));
            handler.postDelayed(this, 1000);
        }
    };

    // ════════════════════════════════════════════════════════════════════════
    // Lifecycle
    // ════════════════════════════════════════════════════════════════════════

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Read level from Intent
        String levelName = getIntent().getStringExtra(MainActivity.EXTRA_LEVEL);
        level = GameLevel.valueOf(levelName != null ? levelName : "BEGINNER");

        soundManager = new SoundManager(this);

        // Bind views
        recyclerView = findViewById(R.id.recyclerView);
        tvLevel      = findViewById(R.id.tvLevel);
        tvMoves      = findViewById(R.id.tvMoves);
        tvTimer      = findViewById(R.id.tvTimer);
        btnRestart   = findViewById(R.id.btnRestart);

        btnRestart.setOnClickListener(v -> restartGame());

        initGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        soundManager.release();
    }

    // ════════════════════════════════════════════════════════════════════════
    // Game setup
    // ════════════════════════════════════════════════════════════════════════

    private void initGame() {
        // Reset all state
        moves         = 0;
        matchedPairs  = 0;
        firstFlippedPos = -1;
        isProcessing  = false;
        stopTimer();

        // Update header
        tvLevel.setText(level.icon + " " + level.label
                + "  (" + level.cols + "×" + level.rows + ")");
        tvMoves.setText("Moves: 0");
        tvTimer.setText("⏱ 00:00");

        // Build deck and adapter
        cards   = CardDeck.buildDeck(level);
        adapter = new CardAdapter(this, cards, this, level.cols);

        // Set up grid
        recyclerView.setLayoutManager(new GridLayoutManager(this, level.cols));
        recyclerView.setAdapter(adapter);

        // Add card spacing
        while (recyclerView.getItemDecorationCount() > 0) {
            recyclerView.removeItemDecorationAt(0);
        }
        recyclerView.addItemDecoration(new SpacingDecoration(8));

        // Start timer
        startTimeMs = System.currentTimeMillis();
        handler.post(timerTick);
    }

    // ════════════════════════════════════════════════════════════════════════
    // Card click — the heart of the game logic
    // ════════════════════════════════════════════════════════════════════════

    @Override
    public void onCardClick(int position) {
        // Block taps during mismatch delay
        if (isProcessing) return;

        Card tapped = cards.get(position);

        // Safety check
        if (tapped.isFlipped() || tapped.isMatched()) return;

        // Flip the tapped card face-up
        tapped.setFlipped(true);
        adapter.animateFlipToFront(recyclerView, position);
        soundManager.playFlip();

        if (firstFlippedPos == -1) {
            // ── This is the FIRST card of the pair ──────────────────────────
            firstFlippedPos = position;

        } else {
            // ── This is the SECOND card — check for match ───────────────────
            moves++;
            tvMoves.setText("Moves: " + moves);

            Card first = cards.get(firstFlippedPos);
            Card second = tapped;
            int posA = firstFlippedPos;
            int posB = position;

            firstFlippedPos = -1; // reset for next round

            if (first.getPairId() == second.getPairId()) {
                // ✅ MATCH
                first.setMatched(true);
                second.setMatched(true);
                matchedPairs++;

                soundManager.playMatch();

                adapter.notifyItemChanged(posA);
                adapter.notifyItemChanged(posB);

                // Check win condition
                if (matchedPairs == level.pairs) {
                    handler.postDelayed(this::onGameWon, 500);
                }

            } else {
                // ❌ MISMATCH — flip both back after delay
                isProcessing = true;
                soundManager.playWrong();

                handler.postDelayed(() -> {
                    first.setFlipped(false);
                    second.setFlipped(false);
                    adapter.animateFlipToBack(recyclerView, posA);
                    adapter.animateFlipToBack(recyclerView, posB);
                    isProcessing = false;
                }, level.flipBackDelay);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Win
    // ════════════════════════════════════════════════════════════════════════

    private void onGameWon() {
        stopTimer();
        soundManager.playWin();

        long totalSecs = elapsedMs / 1000;
        int  score     = ScoreCalculator.calculate(level, moves, totalSecs);
        int  stars     = ScoreCalculator.stars(level, score);

        saveHighScore(score);

        // Build star string
        StringBuilder starStr = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            starStr.append(i < stars ? "⭐" : "☆");
        }

        new AlertDialog.Builder(this)
                .setTitle("🎉 You Win!")
                .setMessage(
                        starStr + "\n\n" +
                                "🎯 Moves : " + moves + "\n" +
                                "⏱ Time  : " + formatTime(totalSecs) + "\n" +
                                "🏆 Score : " + score
                )
                .setCancelable(false)
                .setPositiveButton("Play Again", (d, w) -> restartGame())
                .setNegativeButton("Menu",       (d, w) -> finish())
                .show();
    }

    // ════════════════════════════════════════════════════════════════════════
    // Helpers
    // ════════════════════════════════════════════════════════════════════════

    private void restartGame() {
        recyclerView.setAdapter(null);
        initGame();
    }

    private void stopTimer() {
        handler.removeCallbacks(timerTick);
    }

    private String formatTime(long totalSecs) {
        long m = totalSecs / 60;
        long s = totalSecs % 60;
        return String.format(Locale.US, "%02d:%02d", m, s);
    }

    private void saveHighScore(int score) {
        SharedPreferences prefs = getSharedPreferences("flip_prefs", MODE_PRIVATE);
        String key = "hs_" + level.name();
        int existing = prefs.getInt(key, 0);
        if (score > existing) {
            prefs.edit().putInt(key, score).apply();
        }
    }

    // ── Simple grid spacing decoration ───────────────────────────────────────
    static class SpacingDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;
        SpacingDecoration(int spacing) { this.spacing = spacing; }

        @Override
        public void getItemOffsets(android.graphics.Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left   = spacing;
            outRect.top    = spacing;
            outRect.right  = spacing;
            outRect.bottom = spacing;
        }
    }
}