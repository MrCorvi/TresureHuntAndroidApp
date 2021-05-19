package com.example.myapplication.models;

import android.app.Application;

// Global configuration
public class GlobalClass extends Application {

    private String backEndURL;

    public String getBackEndURL() {
        return backEndURL;
    }

    public void setBackEndURL(String backEndURL) {
        this.backEndURL = backEndURL;
    }

}
