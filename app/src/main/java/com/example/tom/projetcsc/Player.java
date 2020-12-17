package com.example.tom.projetcsc;

public class Player {

    public boolean isNarrator;
    public int roundsAsNarrator;
    public String name;
    public int score;


    public Player(String name){
        this.name = name;
        isNarrator = false;
        roundsAsNarrator = 0;
        score = 0;
    }

    public boolean changeStatus(){
        isNarrator = !isNarrator;
        return isNarrator;
    }

    public String toString(){
        return name;
    }
}
