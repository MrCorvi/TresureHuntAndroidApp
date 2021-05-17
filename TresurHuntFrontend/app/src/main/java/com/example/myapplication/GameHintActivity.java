package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        //If all the letter have been discovered, just assign the answer the the hint string
        String hintString = "";
        if(imageHintsUsed >= answer.length()){
            hintString = answer;
        }else{
            //Write an entirly blank string
            for(int i=0; i<answer.length(); i++) {
                hintString += '_';
            }

            //write the hint string with the hint letters
            Random random = new Random(1234);
            for(int i=0; i<imageHintsUsed; i++){
                int freePos = random.nextInt(answer.length()-1);

                //If the char has already been chosen
                while (hintString.charAt(freePos) != '_'){
                    freePos = (freePos+1) % answer.length();
                }

                //replace '_' with known letter
                hintString = hintString.substring(0,freePos) + answer.charAt(freePos) + hintString.substring(freePos+1);
            }
        }

        //Set textview to hint string
        TextView textView = (TextView) findViewById(R.id.hintText);
        textView.setText(hintString);

    }
}