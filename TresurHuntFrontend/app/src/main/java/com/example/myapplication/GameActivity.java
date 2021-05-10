package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //set top-bar button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void listStepsClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(GameActivity.this, ListViewActivity.class);
        startActivity(intent);
    }
}