package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameHangmanActivity extends AppCompatActivity {

    private TextView titleText, solutionText, lineText, questionText;
    private ImageView imageView;
    private Button hintButton;
    private String answer, question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hangman);

        //Get the id of the selected game
        Intent intent = getIntent();
        answer = intent.getStringExtra("answer");
        question = intent.getStringExtra("question");


        // Init Graph variables
        titleText = (TextView) findViewById(R.id.titleText);
        solutionText = (TextView) findViewById(R.id.solutionText);
        lineText = (TextView) findViewById(R.id.lineText);

        imageView = (ImageView) findViewById(R.id.imageView);
        hintButton = (Button) findViewById(R.id.hintButton);

        solutionText.setText(answer);
        lineText.setText(question);
    }
}