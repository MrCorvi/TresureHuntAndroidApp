package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.adapters.GameStepAdapter;
import com.example.myapplication.models.Step;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private ListView listView;
    private List<Step> stepList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        stepList = intent.getParcelableArrayListExtra("steps");
        System.out.println(stepList.get(0).question);

        //Create the strings of labels of the list
        List<String> labelList = new ArrayList<String>();
        for(int i=0; i < stepList.size(); i++){
            String question = stepList.get(i).question;
            labelList.add(question);
        }

        //here we create the list view object, select a list style and assign the array of object we want to display
        listView = findViewById(R.id.listview);
        GameStepAdapter arrayAdapter = new GameStepAdapter(this, stepList);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, labelList);
        listView.setAdapter(arrayAdapter);

        //here we handle the click of an element of the list
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //This is like an alert in javascript
                Toast.makeText(ListViewActivity.this, list[position], Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    public void gameButtonClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(ListViewActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
