package com.example.demo.models;

import org.json.simple.JSONObject;

public class customObject {
    public String gameName;
    public long numSteps;

    public customObject(String gameName, long numSteps) {
        this.gameName = gameName;
        this.numSteps = numSteps;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        jo.put("gameName", gameName);
        jo.put("numSteps", numSteps);

        return jo;
    }
    
}
