package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.models.Game;
import com.example.myapplication.models.GlobalClass;
import com.example.myapplication.models.Step;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private String backEndURL;

    private int gameId;
    private List<Step> stepList;
    private int currentStep = 0;

    private int maxPosSteps = 0;
    private int maxImgSteps = 0;

    private int maxHints = 3;
    private int hints = maxHints;
    private int imageHintsUsed = 0;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get Global Params
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        backEndURL = globalClass.getBackEndURL();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.game_action_bar);

        //get the id of the selected game
        Intent intent = getIntent();
        gameId = intent.getIntExtra("gameId", 1);

        stepList = new ArrayList<Step>();

        //Request the steps of the selected game
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                backEndURL + "/game?gameId="+gameId, //Here we search in the database all the games that have the quarry term in it
                null,
                new Response.Listener<JSONObject>() { //Called on successful response
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Request arrived !!!!!!");
                        try {
                            JSONArray jsonArrey = response.getJSONArray("game");

                            Integer nImages = 0;
                            Integer nPos = 0;

                            //we add all the steps recived to the stepList
                            for(int i=0; i < jsonArrey.length(); i++){
                                JSONObject step = jsonArrey.getJSONObject(i);
                                Integer id = step.getInt("step");
                                Boolean isPositionQuestion = step.getBoolean("stepType");
                                String question = step.getString("question");
                                String answer = step.getString("answer");


                                System.out.println(id);

                                //add game to internal step list
                                stepList.add(new Step(
                                        id,
                                        isPositionQuestion,
                                        question,
                                        answer
                                ));

                                if(isPositionQuestion)
                                    nPos++;
                                else
                                    nImages++;
                            }

                            //Set the labels of the top-bar
                            TextView posLabel = (TextView) findViewById(R.id.pos_step_counter_label);
                            posLabel.setText("0/"+nPos.toString());
                            TextView imgLabel = (TextView) findViewById(R.id.image_step_counter_label);
                            imgLabel.setText("0/"+nImages.toString());

                            maxPosSteps = nPos;
                            maxImgSteps = nImages;

                            System.out.println(nImages);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() { //Called on error response
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("The game request was unsuccessful!");
                        System.out.println(error.toString());
                    }
                }
        );
        // Add the request to the RequestQueue.
        queue.add(request);

    }

    @Override
    public void onStart() {
        super.onStart();
        //check for the existence of the fragment
        if (findViewById(R.id.map)==null){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    public void checkClick(View view){

        //Distinguish between type of steps
        boolean success = true;
        if(!stepList.get(currentStep).isPositionQuestion){
            // TODO Marsha: attivare la videocamera e far scattare la foto da far controllare al modello
        }else{
            // TODO Gianmarco: controllare le le coordinate attuali sono vinine a quelle dello step on answer
        }

        //if success, next step
        if(success){
            currentStep++;
            setTopBarCounters();
        }

        //Send to victory activity if it was last step
        if(currentStep >= stepList.size()){
            Intent intent = new Intent(GameActivity.this, SuccessActivity.class);
            intent.putExtra("hintUsed", maxHints - hints);
            startActivity(intent);
        }
    }

    public void listStepsClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(GameActivity.this, ListViewActivity.class);
        intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) stepList);
        startActivity(intent);
    }

    public void questionClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(GameActivity.this, GameStepQuestionActivity.class);
        intent.putExtra("question", stepList.get(currentStep).question);
        intent.putExtra("isPositionQuestion", stepList.get(currentStep).isPositionQuestion);
        startActivity(intent);
    }

    public void hintClick(View view){

        if(hints <= 0){
            return;
        }

        //decrease hint counter
        hints--;
        System.out.println(hints);

        if(!stepList.get(currentStep).isPositionQuestion){
            imageHintsUsed++;
            // TODO Marsha: chiama ;'activity dell'impiccato
        }else{
            // TODO Gianmarco deve far comparire il cerchio sulla mappa o diminuirne il raggio
        }

    }

    private void setTopBarCounters(){
        Integer nImages = 0;
        Integer nPos = 0;

        //we add all the steps recived to the stepList
        for(int i=0; i < currentStep; i++){
            Step step = stepList.get(i);
            if(step.isPositionQuestion)
                nPos++;
            else
                nImages++;
        }

        //Set the labels of the top-bar
        TextView posLabel = (TextView) findViewById(R.id.pos_step_counter_label);
        posLabel.setText(nPos.toString()+"/"+maxPosSteps);
        TextView imgLabel = (TextView) findViewById(R.id.image_step_counter_label);
        imgLabel.setText(nImages.toString()+"/"+maxImgSteps);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}