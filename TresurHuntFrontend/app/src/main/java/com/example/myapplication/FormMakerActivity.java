package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FormMakerActivity extends AppCompatActivity {

    private EditText gameDisplay;
    private final String alertMessage = getString(R.string.choose_name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_maker);

        gameDisplay =findViewById(R.id.inputName);
        gameDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getString(R.string.game_display).equals(gameDisplay.getText().toString()))
                    gameDisplay.setText("");
            }
        });

        //set top-bar button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onContinueClick(View view){

        String gameName=gameDisplay.getText().toString();

        if(!gameName.equals("")){
            //Allow to switch from the current Activity to the next
            Intent intent = new Intent(FormMakerActivity.this, MakerMapActivity.class);
            intent.putExtra("gameName",gameName);
            startActivity(intent);
         }
        else{
            Toast.makeText(this, alertMessage , Toast.LENGTH_SHORT).show();
         }
    }

}