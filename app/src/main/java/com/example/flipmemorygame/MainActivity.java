package com.example.flipmemorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flipmemorygame.model.GameLevel;

/**
 * MainActivity.java - Level selection screen.
 *
 * Shows three difficulty buttons and the saved high scores.
 * Passes the chosen level to GameActivity via Intent.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL = "game_level";

    private Button btnBeginner, btnIntermediate, btnAdvanced;
    private TextView tvHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBeginner     = findViewById(R.id.btnBeginner);
        btnIntermediate = findViewById(R.id.btnIntermediate);
        btnAdvanced     = findViewById(R.id.btnAdvanced);
        tvHighScore     = findViewById(R.id.tvHighScore);

        btnBeginner    .setOnClickListener(v -> startGame(GameLevel.BEGINNER));
        btnIntermediate.setOnClickListener(v -> startGame(GameLevel.INTERMEDIATE));
        btnAdvanced    .setOnClickListener(v -> startGame(GameLevel.ADVANCED));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHighScores();
    }

    private void startGame(GameLevel level) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_LEVEL, level.name());
        startActivity(intent);
    }

    private void refreshHighScores() {
        SharedPreferences prefs =
                getSharedPreferences("flip_prefs", MODE_PRIVATE);

        int b = prefs.getInt("hs_BEGINNER",     0);
        int m = prefs.getInt("hs_INTERMEDIATE", 0);
        int a = prefs.getInt("hs_ADVANCED",     0);

        tvHighScore.setText(
                "🏆 High Scores\n" +
                        "🌱 Beginner:      " + b + "\n" +
                        "🔥 Intermediate:  " + m + "\n" +
                        "💀 Advanced:      " + a
        );
    }
}