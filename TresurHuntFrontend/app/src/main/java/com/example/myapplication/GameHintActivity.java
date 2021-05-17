package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class GameHintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hint);

        //get the id of the selected game
        Intent intent = getIntent();
        String answer = intent.getStringExtra("answer");
        int imageHintsUsed = intent.getIntExtra("imageHintsUsed", 0);

        Random random = new Random(1234);
        int freePos = random.nextInt();
        System.out.println(freePos);
    }
}