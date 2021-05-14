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
    @Column(name = "creator", nullable = false)
    private String creator;
    @Column(name = "question", nullable = false)
    private String question;
    @Column(name = "answer", nullable = false)
    private String answer;
    @Column(name = "stepType", nullable = false)
    private Boolean stepType;

    public TreasureHuntStep(){}

    public TreasureHuntStep(Integer gameID, Integer step, String creator, String question, String answer, Boolean stepType){
        
        this.gameID = gameID;
        this.step = step;
        this.creator=creator;
        this.question=question;
        this.answer=answer;
        this.stepType = stepType;
    }

    public TreasureHuntStep(String creator, String question, String answer, Boolean stepType){

        this.creator=creator;
        this.question=question;
        this.answer=answer;
        this.stepType = stepType;
    }

    public String getCreator(){return creator;}
    public String getQuestion(){return question;}
    public String getAnswer(){return answer;}
    public Boolean getStepType(){return stepType;}
    public Integer getGameId(){return gameID;}
    public Integer getStep(){return step;}
    
    public void setCreator(String creator){this.creator= creator;}
    public void setQuestion(String question){this.question=question;}
    public void setAnswer(String answer){this.answer=answer;}
    public void setStepType(Boolean stepType){this.stepType=stepType;}
    public void setGameId(Integer gameID){this.gameID=gameID;}
    public void setStep(Integer step){this.step=step;}

    @Override
    public String toString() {
        return "{ 'gameId': "+ gameID  +",\n 'creator': " + creator + ",\n 'question': " 
        + question + ",\n 'Answer': " + answer + ",\n 'step': " + step + ",\n 'stepType': " + stepType + " }\n";
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        jo.put("gameId", gameID);
        jo.put("creator", creator);
        jo.put("question", question);
        jo.put("answer", answer);
        jo.put("step", step);
        jo.put("stepType", stepType);

        return jo;
    }

}