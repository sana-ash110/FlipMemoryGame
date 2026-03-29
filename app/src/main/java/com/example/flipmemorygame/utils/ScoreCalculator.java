package com.example.flipmemorygame.utils;

import com.example.flipmemorygame.model.GameLevel;

/**
 * ScoreCalculator.java - Calculates the player's final score.
 *
 * Formula:
 *   score = basePoints - movePenalty - timePenalty
 *
 *   basePoints  = 1000 × (level index + 1)   → harder level = higher ceiling
 *   movePenalty = (moves - pairs) × 5        → extra moves cost points
 *   timePenalty = elapsedSeconds × 2         → time costs points
 *
 * Score is clamped to minimum 0.
 */
public class ScoreCalculator {

    public static int calculate(GameLevel level, int moves, long elapsedSeconds) {
        int base        = 1000 * (level.ordinal() + 1);
        int movePenalty = Math.max(0, moves - level.pairs) * 5;
        int timePenalty = (int) elapsedSeconds * 2;

        return Math.max(0, base - movePenalty - timePenalty);
    }

    /**
     * Convert score to a star rating (1-3).
     *   3 stars = 75%+ of max score
     *   2 stars = 45%+ of max score
     *   1 star  = anything lower
     */
    public static int stars(GameLevel level, int score) {
        int max = 1000 * (level.ordinal() + 1);
        double ratio = (double) score / max;
        if (ratio >= 0.75) return 3;
        if (ratio >= 0.45) return 2;
        return 1;
    }
}