package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameHangmanActivity extends AppCompatActivity {

    private final String alertHintsError = getString(R.string.no_hint_message);
    private TextView titleText, solutionText, lineText, questionText;
    private ImageView imageView;
    private Button hintButton;
    private int hints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hangman);

        //Get the id of the selected game
        Intent intent = getIntent();
        String answer = intent.getStringExtra("answer");
        hints = intent.getIntExtra("hints", 3);

        // Init Graph variables
        titleText = (TextView) findViewById(R.id.titleText);
        solutionText = (TextView) findViewById(R.id.solutionText);
        lineText = (TextView) findViewById(R.id.lineText);

        imageView = (ImageView) findViewById(R.id.imageView);
        hintButton = (Button) findViewById(R.id.hintButton);

        char[] ch = answer.toCharArray();
        // Printing array
        for (char c : ch) {
            System.out.println(c);
        }

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hints--;
                if (hints <= 0){
                    imageView.setImageResource(R.drawable.hangma_0);
                    hintButton.setEnabled(false);
                    toastAlert(alertHintsError);
                } else {
                    showHint();
                    switch(hints) {
                        case 1:
                            imageView.setImageResource(R.drawable.hangma_1);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.hangma_2);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.hangma_3);
                            break;
                        default:
                            imageView.setImageResource(R.drawable.hangma_3);
                    }
                }
            }
        });
    }

    public void showHint(){


    }

    public void toastAlert(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}