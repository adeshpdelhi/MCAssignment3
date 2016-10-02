package com.creation.adesh.mcassignment3;

import java.util.Random;

class Question {
    private Integer number = 0;
    private Boolean answer = true;
    private void setAnswer(){
        for(int i=2;i<number;i++)
            if(number%i==0)
                answer=false;
    }
    public Question(){
        number = new Random().nextInt(1000)+1;
        setAnswer();
    }

    public Question(Integer number){
        this.number = number;
        setAnswer();
    }

    Integer getNumber(){
        return number;
    }
    Boolean checkAnswer(Boolean givenAnswer){
        return (givenAnswer == answer);
    }
    Boolean getAnswer(){
        return answer;
    }
}
