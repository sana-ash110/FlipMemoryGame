package com.example.flipmemorygame.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipmemorygame.R;
import com.example.flipmemorygame.model.Card;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    /**
     * CardAdapter.java - Connects card data to the RecyclerView grid.
     *
     * Each item has two faces:
     *   cardBack  → face-down (shows "?")
     *   cardFront → face-up   (shows emoji)
     *
     * The adapter swaps visibility between them and runs a 3D flip animation.
     */
    private final int numColumns;
    private final int spacing = 8;

    // ── Listener interface — GameActivity implements this ────────────────────
    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    private final Context context;
    private final List<Card> cards;
    private final OnCardClickListener listener;

    public CardAdapter(Context context, List<Card> cards,
                       OnCardClickListener listener, int numColumns) {
        this.context    = context;
        this.cards      = cards;
        this.listener   = listener;
        this.numColumns = numColumns;
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_card, parent, false);

        // Force each card to be a perfect square based on grid column width
        int totalWidth = parent.getMeasuredWidth();
        int cardSize   = totalWidth / numColumns - (spacing * 2);
        if (cardSize < 1) cardSize = 1;

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(cardSize, cardSize);
        view.setLayoutParams(params);

        return new CardViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(cards.get(position), position);
    }

    @Override
    public int getItemCount() { return cards.size(); }

    // ── ViewHolder ───────────────────────────────────────────────────────────
    class CardViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardBack;
        private final CardView cardFront;
        private final TextView tvSymbol;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBack  = itemView.findViewById(R.id.cardBack);
            cardFront = itemView.findViewById(R.id.cardFront);
            tvSymbol  = itemView.findViewById(R.id.tvSymbol);
        }

        void bind(Card card, int position) {
            tvSymbol.setText(card.getSymbol());

            // Show correct face instantly (no animation during bind)
            if (card.isFlipped() || card.isMatched()) {
                showFront();
            } else {
                showBack();
            }

            // Matched cards get a green tint
            if (card.isMatched()) {
                cardFront.setCardBackgroundColor(
                        context.getColor(R.color.matched_card_bg));
            } else {
                cardFront.setCardBackgroundColor(
                        context.getColor(R.color.card_front_bg));
            }

            // Only allow clicks on face-down, unmatched cards
            itemView.setOnClickListener(v -> {
                if (!card.isMatched() && !card.isFlipped()) {
                    listener.onCardClick(position);
                }
            });
        }

        void flipToFront() { runFlipAnimation(cardBack, cardFront); }
        void flipToBack()  { runFlipAnimation(cardFront, cardBack); }

        private void showFront() {
            cardBack.setVisibility(View.INVISIBLE);
            cardFront.setVisibility(View.VISIBLE);
        }

        private void showBack() {
            cardFront.setVisibility(View.INVISIBLE);
            cardBack.setVisibility(View.VISIBLE);
        }

        /**
         * Classic two-phase card flip:
         * Phase 1 → viewOut rotates 0° to 90°  (disappears edge-on)
         * Phase 2 → viewIn  rotates -90° to 0° (new face swings in)
         */
        private void runFlipAnimation(View viewOut, View viewIn) {
            float scale = context.getResources().getDisplayMetrics().density;
            viewOut.setCameraDistance(8000 * scale);
            viewIn.setCameraDistance(8000 * scale);

            AnimatorSet flipOut = (AnimatorSet) AnimatorInflater
                    .loadAnimator(context, R.anim.card_flip_out);
            AnimatorSet flipIn = (AnimatorSet) AnimatorInflater
                    .loadAnimator(context, R.anim.card_flip_in);

            flipOut.setTarget(viewOut);
            flipIn.setTarget(viewIn);

            flipOut.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    viewOut.setVisibility(View.INVISIBLE);
                    viewIn.setVisibility(View.VISIBLE);
                    flipIn.start();
                }
            });

            flipOut.start();
        }
    }

    // ── Public animation triggers called from GameActivity ───────────────────

    public void animateFlipToFront(RecyclerView rv, int position) {
        CardViewHolder holder = (CardViewHolder)
                rv.findViewHolderForAdapterPosition(position);
        if (holder != null) holder.flipToFront();
    }

    public void animateFlipToBack(RecyclerView rv, int position) {
        CardViewHolder holder = (CardViewHolder)
                rv.findViewHolderForAdapterPosition(position);
        if (holder != null) holder.flipToBack();
    }
}