package com.creation.adesh.mcassignment3;

/**
 * Created by adesh on 10/1/16.
 */
public class User {
    String name = null;
    Integer highScore = 0;
    Long _ID = null;
    public User(Long _ID,String name, Integer highScore){
        this._ID = _ID;
        this.name = name;
        this.highScore = highScore;
    }

    public void setHighScore(Integer highScore) {
        this.highScore = highScore;
    }

    public Integer getHighScore() {
        return highScore;
    }

    public String getName() {
        return name;
    }
}
