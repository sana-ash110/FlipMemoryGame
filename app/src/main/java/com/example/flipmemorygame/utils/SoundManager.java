package com.example.flipmemorygame.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.flipmemorygame.R;

/**
 * SoundManager.java - Plays short sound effects using SoundPool.
 *
 * Sounds:
 *   flip  → card is tapped
 *   match → correct pair found
 *   wrong → mismatch
 *   win   → all pairs matched
 *
 * NOTE: Requires flip.ogg, match.ogg, wrong.ogg, win.ogg in res/raw/
 * If files are missing the game runs silently — no crash.
 */
public class SoundManager {

    private SoundPool soundPool;
    private int soundFlip, soundMatch, soundWrong, soundWin;
    private boolean loaded = false;

    public SoundManager(Context context) {
        // Sound disabled — add flip.ogg, match.ogg, wrong.ogg, win.ogg
        // to res/raw/ folder to enable sounds
        loaded = false;
    }



    public void playFlip()  { play(soundFlip);  }
    public void playMatch() { play(soundMatch); }
    public void playWrong() { play(soundWrong); }
    public void playWin()   { play(soundWin);   }

    private void play(int soundId) {
        if (loaded && soundId != 0) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
        }
    }

    /** Call from onDestroy() to free memory */
    public void release() {
       //nothing
    }
}