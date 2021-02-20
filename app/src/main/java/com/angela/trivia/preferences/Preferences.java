package com.angela.trivia.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Display;

public class Preferences {
    private SharedPreferences preferences;

    public Preferences(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE) ;
    }

    public void saveHighestScore(int score){
        int currentScore = score;
        int previousScore = preferences.getInt("highest_score", 0);
        if (currentScore > previousScore){
            preferences.edit().putInt("highest_score", currentScore).apply();
        }
    }
    public int getHighestScore(){
        int highestScore = preferences.getInt("highest_score", 0);
        return highestScore;
    }
}
