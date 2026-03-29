package com.example.flipmemorygame.model;

/**
 * Card.java - Data model for each card.
 *
 * Each card has:
 * - id        : its position in the grid
 * - pairId    : cards with the same pairId are a matching pair
 * - symbol    : the emoji shown on the front face
 * - isFlipped : is the card currently face-up?
 * - isMatched : has this card been successfully matched?
 */
public class Card {

    private int id;
    private int pairId;
    private String symbol;
    private boolean isFlipped;
    private boolean isMatched;

    // Constructor
    public Card(int id, int pairId, String symbol) {
        this.id        = id;
        this.pairId    = pairId;
        this.symbol    = symbol;
        this.isFlipped = false;  // starts face-down
        this.isMatched = false;  // starts unmatched
    }

    // Getters
    public int     getId()       { return id; }
    public int     getPairId()   { return pairId; }
    public String  getSymbol()   { return symbol; }
    public boolean isFlipped()   { return isFlipped; }
    public boolean isMatched()   { return isMatched; }

    // Setters
    public void setFlipped(boolean flipped) {
        this.isFlipped = flipped;
    }

    public void setMatched(boolean matched) {
        this.isMatched = matched;
        if (matched) this.isFlipped = true; // matched cards always stay face-up
    }
}