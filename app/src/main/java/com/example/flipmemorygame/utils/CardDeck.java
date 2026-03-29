package com.example.flipmemorygame.utils;

import com.example.flipmemorygame.model.Card;
import com.example.flipmemorygame.model.GameLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CardDeck.java - Builds a shuffled deck of cards for a given level.
 *
 * Steps:
 * 1. Pick the first N emojis from the pool (N = number of pairs)
 * 2. Create two Card objects for each emoji (the matching pair)
 * 3. Shuffle the whole list
 * 4. Assign position IDs after shuffle
 */
public class CardDeck {

    // Pool of 32 emojis — enough for the hardest level
    private static final String[] EMOJI_POOL = {
            "🐶","🐱","🐻","🦊","🐼","🐨","🐯","🦁",
            "🐸","🐙","🦋","🌸","🌈","🍎","🍕","🚀",
            "🎸","🎯","🎲","🏆","💎","🔮","🎭","🌙",
            "🌊","🍦","🦄","🐲","🎪","🎨","🎀","🎁"
    };

    public static List<Card> buildDeck(GameLevel level) {
        List<Card> deck = new ArrayList<>();

        for (int pairId = 0; pairId < level.pairs; pairId++) {
            String symbol = EMOJI_POOL[pairId];
            deck.add(new Card(pairId * 2,     pairId, symbol));
            deck.add(new Card(pairId * 2 + 1, pairId, symbol));
        }

        Collections.shuffle(deck);

        for (int i = 0; i < deck.size(); i++) {
            Card old = deck.get(i);
            deck.set(i, new Card(i, old.getPairId(), old.getSymbol()));
        }

        return deck;
    }

        // Step 3: shuffle

}