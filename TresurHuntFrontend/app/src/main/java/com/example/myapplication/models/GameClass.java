package com.example.myapplication.models;

public class GameClass {
    public int id;
    public String name;
    public int steps;

    public  GameClass(){
        id = 0;
        name = "";
        steps = 0;
    }

    public GameClass(int in_id, String in_name, int in_steps){
        id = in_id;
        name = in_name;
        steps = in_steps;
    }
}
