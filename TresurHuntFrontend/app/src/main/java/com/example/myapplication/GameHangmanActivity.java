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

import java.io.Serializable;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_game_hangman);

        // Alert Message No more Hints
        final String alertHintsError = getString(R.string.no_hint_message);

        //Get the id of the selected game
        Intent intent = getIntent();
        String answer = (intent.getStringExtra("answer")).toLowerCase();
        hints = intent.getIntExtra("hints", 3);
        maxTries = intent.getIntExtra("maxTries", 5);
        Bundle args_s = intent.getBundleExtra("usedlist");
        usedlist = (ArrayList<Character>) args_s.getSerializable("ARRAYLIST");

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

        if (hints == 0) {hintButton.setEnabled(false);}
        onCreateHangman();
    }

    public void onHintClick(View v) {
        if (hints < 1){
            // imageView.setImageResource(R.drawable.hangma_0);
            hintButton.setEnabled(false);
            Toast.makeText(this, getString(R.string.no_hint_message), Toast.LENGTH_SHORT).show();
        } else {
            for(char c : answerlist){
                if(!usedlist.contains(c)){
                    checkChar(c);
                    break;
                }
            }
        }
        hints--;
        // On-hot change the enabled button
        if (hints == 0) {hintButton.setEnabled(false);}
    }

    public void onOkClick(View view) {
        checkChar(Character.toLowerCase(inputQuestion.getText().charAt(0)));
        inputQuestion.setText("");
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
            maxTries--;

            if (!(usedlist.contains(compareChar))){
                usedlist.add(compareChar);
                if (maxTries<=0){
                    okButton.setEnabled(false);
                    imageView.setImageResource(R.drawable.hangma_0);
                } else {
                    putImageHangman(maxTries);
                }
            }
        }
    }

    public void putImageHangman(int option){
        switch(option) {
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
                imageView.setImageResource(R.drawable.hangma_4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.hangma_5);
                break;
            default:
                imageView.setImageResource(R.drawable.hangma_0);
        }
    }

    public void hangmanController(char[] answerChar){
        answerlist = new ArrayList<Character>();
        for (char c : answerChar) {
            answerlist.add(c);
            solutionText.setText(solutionText.getText() + "_ ");
        }
    }

    public void onCreateHangman(){
        for(char c : answerlist){
            if(usedlist.contains(c)){
                checkChar(c);
            }
        }

        putImageHangman(maxTries);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameHangmanActivity.this, GameActivity.class);

        Bundle args_s = new Bundle();
        args_s.putSerializable("ARRAYLIST",(Serializable)usedlist);

        intent.putExtra("usedlist",args_s);
        intent.putExtra("hints",hints);
        intent.putExtra("maxTries", maxTries);

        setResult(RESULT_OK, intent);

        super.onBackPressed();

        // finish();

    }

}