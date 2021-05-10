package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private Button joinBut, makeBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.provaText);
        joinBut = findViewById(R.id.joinButton);
        makeBut = findViewById(R.id.makeButton);

    }


    public void joinButtonClick(View view){
        //text.setText("Join");
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void makeButtonClick(View view){
        text.setText("Make");
    }

}
