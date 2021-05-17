package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class GameStepQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_step_question);

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        boolean isPositionQuestion = intent.getBooleanExtra("isPositionQuestion", true);

        //Set icon according to type
        ImageView iconView = (ImageView) findViewById(R.id.questionImage);
        if(isPositionQuestion){
            iconView.setImageResource(R.drawable.ic_flag);
        }else{
            iconView.setImageResource(R.drawable.ic_camera);
        }

        //Set text of question
        TextView text = (TextView) findViewById(R.id.questionText);
        text.setText(question);
    }
}