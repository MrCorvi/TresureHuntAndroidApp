package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class CameraFormActivity extends AppCompatActivity {

    private final String alertMessageQuesiton = "Choose a question";
    private final String alertMessageAnswer = "Choose an answer";

    private EditText questionDisplay;
    private ListView listView;
    private String [] list = {
            "Flower",
            "Butterfly",
            "Pisa Merda",
            "Statue",
            "Fountain",
            "Ball"
    };
    private boolean selected;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pos = -1;
        questionDisplay =findViewById(R.id.inputQuestion);
        questionDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getString(R.string.question_display).equals(questionDisplay.getText().toString()))
                    questionDisplay.setText("");
            }
        });

        listView = findViewById(R.id.answerListview);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected=true;
                pos=position;
            }

        });

    }


    public void onConfirmClick(View view){

        String question = questionDisplay.getText().toString();

        if(pos==-1)
            Toast.makeText(this, alertMessageAnswer , Toast.LENGTH_SHORT).show();
        else  if(question.equals("")) {
            Toast.makeText(this, alertMessageQuesiton, Toast.LENGTH_SHORT).show();
        }else {
            String answer = (String) (listView.getItemAtPosition(pos).toString());

            Intent intent = new Intent(CameraFormActivity.this, MakerMapActivity.class);
            intent.putExtra("question",question);
            intent.putExtra("answer",answer);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}