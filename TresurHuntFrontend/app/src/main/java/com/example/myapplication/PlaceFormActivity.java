package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlaceFormActivity extends AppCompatActivity {

    private EditText questionDisplay;
    private final String alertMessage = getString(R.string.answer_display);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionDisplay =findViewById(R.id.inputQuestion);
        questionDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getString(R.string.question_display).equals(questionDisplay.getText().toString()))
                    questionDisplay.setText("");
            }
        });
    }

    public void onConfirmClick(View view){

        String question = questionDisplay.getText().toString();
        if(!question.equals("")){
            Intent intent = new Intent(PlaceFormActivity.this, MakerMapActivity.class);
            intent.putExtra("question",question);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else{
            Toast.makeText(this, alertMessage , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        Intent intent = new Intent(PlaceFormActivity.this, MakerMapActivity.class);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}