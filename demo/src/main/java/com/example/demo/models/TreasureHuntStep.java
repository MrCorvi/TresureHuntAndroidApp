package com.example.demo.models;

import java.io.Serializable;
import org.json.simple.JSONObject; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="treasurehunt")
public class TreasureHuntStep implements Serializable {

    @Id
    @Column(name = "gameID", nullable = false)
    protected Integer gameID; // int isn't nullable
    @Id
    @Column(name = "step", nullable=false)
    protected Integer step;
    @Column(name = "gameName", nullable = false)
    private String gameName;
    @Column(name = "question", nullable = false)
    private String question;
    @Column(name = "answer", nullable = false)
    private String answer;
    @Column(name = "stepType", nullable = false)
    private Boolean stepType;

    public TreasureHuntStep(){}

    public TreasureHuntStep(Integer step, String gameName, String question, String answer, Boolean stepType){
        
        this.gameID = -1;
        this.step = step;
        this.gameName=gameName;
        this.question=question;
        this.answer=answer;
        this.stepType = stepType;
    }

    /*public TreasureHuntStep(Integer gameID, Integer step, String gameName, String question, String answer, Boolean stepType){
        
        this.gameID = gameID;
        this.step = step;
        this.gameName=gameName;
        this.question=question;
        this.answer=answer;
        this.stepType = stepType;
    }*/

    public TreasureHuntStep(String gameName, String question, String answer, Boolean stepType){

        this.gameName=gameName;
        this.question=question;
        this.answer=answer;
        this.stepType = stepType;
    }

    public String getgameName(){return gameName;}
    public String getQuestion(){return question;}
    public String getAnswer(){return answer;}
    public Boolean getStepType(){return stepType;}
    public Integer getGameId(){return gameID;}
    public Integer getStep(){return step;}
    
    public void setgameName(String gameName){this.gameName= gameName;}
    public void setQuestion(String question){this.question=question;}
    public void setAnswer(String answer){this.answer=answer;}
    public void setStepType(Boolean stepType){this.stepType=stepType;}
    public void setGameId(Integer gameID){this.gameID=gameID;}
    public void setStep(Integer step){this.step=step;}

    @Override
    public String toString() {
        return "{ 'gameId': "+ gameID  +",\n 'gameName': " + gameName + ",\n 'question': " 
        + question + ",\n 'Answer': " + answer + ",\n 'step': " + step + ",\n 'stepType': " + stepType + " }\n";
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        jo.put("gameId", gameID);
        jo.put("gameName", gameName);
        jo.put("question", question);
        jo.put("answer", answer);
        jo.put("step", step);
        jo.put("stepType", stepType);

        return jo;
    }

}