package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GameHangmanActivity extends AppCompatActivity {

    private TextView titleText, solutionText;
    private ImageView imageView;
    private Button hintButton, okButton;
    private EditText inputQuestion;

    private int hints;
    private int maxTries = 5;

    List<Character> answerlist;
    List<Character> usedlist = new ArrayList<Character>();

    char[] answerChar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hangman);

        // Alert Message No more Hints
        final String alertHintsError = getString(R.string.no_hint_message);

        //Get the id of the selected game
        Intent intent = getIntent();
        String answer = (intent.getStringExtra("answer")).toLowerCase();
        hints = intent.getIntExtra("hints", 3);

        // Init Graph variables
        titleText = (TextView) findViewById(R.id.titleText);
        solutionText = (TextView) findViewById(R.id.solutionText);

        inputQuestion = (EditText) findViewById(R.id.inputQuestion);
        imageView = (ImageView) findViewById(R.id.imageView);
        hintButton = (Button) findViewById(R.id.hintButton);
        okButton = (Button) findViewById(R.id.okButton);

        answerChar = answer.toCharArray();
        hangmanController(answerChar);
        imageView.setImageResource(R.drawable.hangma_5);
    }

    public void onHintClick(View v) {
        if (hints <= 0){
            // imageView.setImageResource(R.drawable.hangma_0);
            hintButton.setEnabled(false);
            Toast.makeText(this, getString(R.string.no_hint_message), Toast.LENGTH_SHORT).show();
        } else {
            hints--;
            for(char c : answerlist){
                if(!usedlist.contains(c)){
                    checkChar(c);
                    break;
                }
            }
        }
    }

    public void onOkClick(View view) {
        checkChar(inputQuestion.getText().charAt(0));
    }

    public void checkChar(char compareChar) {
        StringBuilder sb = new StringBuilder(solutionText.getText().toString());
        if (answerlist.contains(compareChar)){
            for(int i=0; i<answerlist.size(); i++) {
                if (answerlist.get(i) == compareChar) {
                    sb.setCharAt(2*i, compareChar);
                }
            }
            usedlist.add(compareChar);
            solutionText.setText(sb.toString());

        } else {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();

            if (!(usedlist.contains(compareChar))){
                usedlist.add(compareChar);

                maxTries--;

                if (maxTries<0){
                    maxTries=0;
                    okButton.setEnabled(false);
                } else {
                    switch(maxTries) {
                        case 1:
                            imageView.setImageResource(R.drawable.hangma_1);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.hangma_2);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.hangma_3);
                            break;
                        case 4:
                            imageView.setImageResource(R.drawable.hangma_3);
                            break;
                        default:
                            imageView.setImageResource(R.drawable.hangma_0);
                    }
                }
            }
        }
    }

    public void hangmanController(char[] answerChar){
        answerlist = new ArrayList<Character>();
        for (char c : answerChar) {
            answerlist.add(c);
            solutionText.setText(solutionText.getText() + "_ ");
        }
    }

}