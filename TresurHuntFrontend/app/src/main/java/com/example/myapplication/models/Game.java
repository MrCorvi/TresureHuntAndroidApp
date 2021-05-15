package com.example.myapplication.models;

public class Game {
    private int id;
    private String name;
    private int steps;

    public  Game(){
        id = 0;
        name = "";
        steps = 0;
    }

    public Game(int in_id, String in_name, int in_steps){
        id = in_id;
        name = in_name;
        steps = in_steps;
    }
}
