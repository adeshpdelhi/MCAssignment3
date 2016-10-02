package com.creation.adesh.mcassignment3;

/**
 * Created by adesh on 10/1/16.
 */
class User {
    private String name = null;
    private Integer highScore = 0;
    private Long _ID = null;
    public User(Long _ID,String name, Integer highScore){
        this._ID = _ID;
        this.name = name;
        this.highScore = highScore;
    }

    public Long get_ID() {
        return _ID;
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
