package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private Button joinBut, makeBut;
    static PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.provaText);
        joinBut = findViewById(R.id.joinButton);
        makeBut = findViewById(R.id.makeButton);

        // Initialize the SDK
        String api_key = "AIzaSyBEk5dFEO0cyysrpoOVZiyzFVL-KzNehJI";
        Places.initialize(getApplicationContext(),api_key);

        // Create a new PlacesClient instance
        placesClient = Places.createClient(this);

    }


    public void joinButtonClick(View view){
        //text.setText("Join");
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(MainActivity.this, SearchGameActivity.class);
        startActivity(intent);
    }

    public void makeButtonClick(View view){
        //text.setText("Make");

        Intent intent = new Intent(MainActivity.this, FormMakerActivity.class);
        startActivity(intent);
    }

}
