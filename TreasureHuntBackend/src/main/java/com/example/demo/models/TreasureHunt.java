package com.example.demo.models;

import java.util.List;
import java.io.Serializable;

/* TreasureHuntStep Wrapper class */
public class TreasureHunt implements Serializable {
    
    private List<TreasureHuntStep> game;

    public TreasureHunt() {}

    public List<TreasureHuntStep> getGame() {
        return game;
    }

    public void setGame(List<TreasureHuntStep> game){
        this.game = game;
    }

    @Override
    public String toString() {
        return "Game: " + game.toString();
    }
}
