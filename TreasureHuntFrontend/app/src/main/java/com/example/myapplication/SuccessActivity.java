package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        //get the id of the selected game
        Intent intent = getIntent();
        int hintUsed = intent.getIntExtra("hintUsed", 0);

        //Set text of question
        TextView text = (TextView) findViewById(R.id.hintUsedText);
        text.setText("Hint used: "+hintUsed);
    }

    public void homeClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}