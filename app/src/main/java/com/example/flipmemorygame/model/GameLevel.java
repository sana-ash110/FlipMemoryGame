package com.example.flipmemorygame.model;

/**
 * GameLevel.java - Defines the three difficulty levels.
 *
 * Each level has:
 * - cols/rows      : grid dimensions
 * - pairs          : number of unique symbol pairs
 * - flipBackDelay  : ms before mismatched cards flip back (harder = faster)
 * - label          : display name
 * - icon           : emoji icon
 */
public enum GameLevel {

    BEGINNER    (4, 4,  8,  1200, "Beginner",     "🌱"),
    INTERMEDIATE(6, 6, 18,   900, "Intermediate", "🔥"),
    ADVANCED    (8, 8, 32,   600, "Advanced",     "💀");

    public final int    cols;
    public final int    rows;
    public final int    pairs;
    public final int    flipBackDelay;
    public final String label;
    public final String icon;

    GameLevel(int cols, int rows, int pairs,
              int flipBackDelay, String label, String icon) {
        this.cols          = cols;
        this.rows          = rows;
        this.pairs         = pairs;
        this.flipBackDelay = flipBackDelay;
        this.label         = label;
        this.icon          = icon;
    }

    /** Total cards on the grid */
    public int totalCards() { return pairs * 2; }
}