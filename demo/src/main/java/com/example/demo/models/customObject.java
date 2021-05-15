package com.example.demo.models;

import org.json.simple.JSONObject;

public class customObject {

    private int gameId;
    public String gameName;
    public long numSteps;

    public customObject(int gameId, String gameName, long numSteps) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.numSteps = numSteps;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        jo.put("gameId", gameId);
        jo.put("gameName", gameName);
        jo.put("numSteps", numSteps);

        return jo;
    }
    
}
