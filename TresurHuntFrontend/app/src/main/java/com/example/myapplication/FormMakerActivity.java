package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FormMakerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_maker);

        //set top-bar button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onContinueClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(FormMakerActivity.this, MakerMapActivity.class);
        startActivity(intent);
    }

}