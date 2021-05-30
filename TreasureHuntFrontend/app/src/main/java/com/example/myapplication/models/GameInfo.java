package com.example.myapplication.models;

import java.util.ArrayList;
import java.util.Collections;

public class GameInfo {

    private String gameName;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private ArrayList<Boolean> stepTypes;

    public GameInfo(String gameName) {
        this.gameName = gameName;
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        stepTypes = new ArrayList<>();
    }

    public GameInfo(String gameName,ArrayList<String> questions, ArrayList<String> answers, ArrayList<Boolean> stepTypes) {

        this.gameName = gameName;
        this.questions = questions;
        this.answers = answers;
        this.stepTypes = stepTypes;
    }

    public void clear(){
        questions.clear();
        answers.clear();
        stepTypes.clear();
    }
    public void rebuild(ArrayList<String> questions, ArrayList<String> answers, ArrayList<Boolean> stepTypes) {

        clear();
        this.questions = questions;
        this.answers = answers;
        this.stepTypes = stepTypes;
    }

    public void remove(int pos){
        questions.remove(pos);
        answers.remove(pos);
        stepTypes.remove(pos);
    }

    public void swap(int position_dragged, int position_target){
        Collections.swap(questions,position_dragged,position_target);
        Collections.swap(answers,position_dragged,position_target);
        Collections.swap(stepTypes,position_dragged,position_target);
    }


    public ArrayList<String> getQuestions(){ return questions;}
    public ArrayList<String> getAnswers(){ return answers;}
    public ArrayList<Boolean> getStepTypes(){ return stepTypes;}
    public String getGameName(){return gameName;}

    public int getSize(){
        assert (questions.size()==answers.size() && questions.size()==stepTypes.size());
        return questions.size();}

}

