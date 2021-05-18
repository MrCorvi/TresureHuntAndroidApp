package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MakerMapActivity extends AppCompatActivity {

    private int LAUNCH_PLACE = 1;
    private int LAUNCH_CAMERA = 2;
    private int LAUNCH_RECAP = 3;
    private final int MIN_STEP = 2;
    private final String alertMessage = "Add some step";

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker_map);
        Intent i = getIntent();
        game = new Game(i.getStringExtra("gameName"));
    }

    public void placeClick(View view){
        //recuperare le coordinate della posizione fissata attualmente
        game.getAnswers().add("Position placeholder"); // IMPORTANT : If you press back in PlaceFormActivity
                                                       // the list will be corrupted. Not solved this issue
                                                       // because in the final version it will be remove and
                                                       // position evaluated and sent via intent in PlaceFormActivity
        Intent intent = new Intent( MakerMapActivity.this, PlaceFormActivity.class);
        startActivityForResult(intent, LAUNCH_PLACE);
    }

    public void cameraClick(View view){
        Intent intent = new Intent( MakerMapActivity.this, CameraFormActivity.class);
        startActivityForResult(intent, LAUNCH_CAMERA);
    }

    public void finishClick(View view) {

        if(game.getSize() < MIN_STEP){
            Toast.makeText(this, alertMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent( MakerMapActivity.this, RecapActivity.class);

        Bundle args_q = new Bundle();
        Bundle args_a = new Bundle();
        Bundle args_s = new Bundle();
        args_q.putSerializable("ARRAYLIST",(Serializable)game.getQuestions());
        args_a.putSerializable("ARRAYLIST",(Serializable)game.getAnswers());
        args_s.putSerializable("ARRAYLIST",(Serializable)game.getStepTypes());

        intent.putExtra("questionBundle",args_q);
        intent.putExtra("answerBundle",args_a);
        intent.putExtra("stepTypeBundle",args_s);
        intent.putExtra("gameName",game.getGameName());
        startActivityForResult(intent, LAUNCH_RECAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == LAUNCH_PLACE) {
                game.getQuestions().add(data.getStringExtra("question"));
                game.getStepTypes().add(Boolean.TRUE);
            }
            if(requestCode == LAUNCH_CAMERA) {
                game.getQuestions().add(data.getStringExtra("question"));
                game.getAnswers().add(data.getStringExtra("answer"));
                game.getStepTypes().add(Boolean.FALSE);
            }
            if(requestCode == LAUNCH_RECAP){
                Bundle args_q = data.getBundleExtra("questionBundle");
                Bundle args_a = data.getBundleExtra("answerBundle");
                Bundle args_s = data.getBundleExtra("stepTypeBundle");
                ArrayList<String> q = (ArrayList<String>) args_q.getSerializable("ARRAYLIST");
                ArrayList<String> a = (ArrayList<String>) args_a.getSerializable("ARRAYLIST");
                ArrayList<Boolean> s = (ArrayList<Boolean>) args_s.getSerializable("ARRAYLIST");
                game.rebuild(q,a,s);
            }

            /*for(String q : questions){
                System.out.println(q);
            }
            for(String a: answers){
                System.out.println(a);
            }*/
        }
        else if(resultCode == Activity.RESULT_CANCELED){
            if(requestCode == LAUNCH_PLACE) {
                game.getAnswers().remove(game.getAnswers().size()-1);
            }
        }
    }
}