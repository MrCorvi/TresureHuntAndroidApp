package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SuccessCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_creation);

        Intent intent = getIntent();
        int gameName = intent.getIntExtra("gameId",-1);

        TextView tmp =  (TextView) findViewById(R.id.success_gameId); ;
        tmp.setText(String.valueOf(gameName));
    }

    public void onConfirmClick(View view){

        Intent intent = new Intent(SuccessCreationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}