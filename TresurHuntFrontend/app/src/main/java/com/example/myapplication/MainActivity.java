package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.models.GlobalClass;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MainActivity extends AppCompatActivity {

    private Button joinBut, makeBut;
    static PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Global Params
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        globalClass.setBackEndURL(getString(R.string.back_end_url));

        joinBut = findViewById(R.id.joinButton);
        makeBut = findViewById(R.id.makeButton);
<<<<<<< HEAD

        // Initialize the SDK
        String api_key = "AIzaSyBEk5dFEO0cyysrpoOVZiyzFVL-KzNehJI";
        Places.initialize(getApplicationContext(),api_key);

        // Create a new PlacesClient instance
        placesClient = Places.createClient(this);

=======
>>>>>>> 33bb6fe3a3c00cfe1883fcd35a221af0b8ab1cbd
    }


    public void joinButtonClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(MainActivity.this, SearchGameActivity.class);
        startActivity(intent);
    }

    public void makeButtonClick(View view){
        Intent intent = new Intent(MainActivity.this, FormMakerActivity.class);
        startActivity(intent);
    }

}
