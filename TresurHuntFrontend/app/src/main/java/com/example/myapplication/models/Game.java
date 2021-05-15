package com.example.myapplication.models;

public class Game {
    public int id;
    public String name;
    public int steps;

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
