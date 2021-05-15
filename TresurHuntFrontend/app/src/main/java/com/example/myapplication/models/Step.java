package com.example.myapplication.models;

public class Step {
    public int id;
    public boolean isPositionQuestion;
    public String question;
    public String answer;

    public Step(int in_id, boolean ip, String q, String a){
        id = in_id;
        isPositionQuestion = ip;
        question = q;
        answer = a;
    }
}
