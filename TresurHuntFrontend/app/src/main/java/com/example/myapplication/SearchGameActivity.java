package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.models.Game;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchGameActivity extends AppCompatActivity {

    private ListView listView;
    private RequestQueue queue;
    private String backendRoot = "http://192.168.1.4:8080";
    private String [] list ;


    //list of games with all their proprieties
    private List<Game> gameList;
    private int gameSelected = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set to -1, to mean that no  game was selected
        gameSelected = -1;


        // This is for when the activity is reloaded with the quarry searched in the bar
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println(query);

            // Instantiate the RequestQueue.
            queue = Volley.newRequestQueue(this);
            listView = findViewById(R.id.listview);


            list = new String[]{
                    "A",
                    "B",
                    "C"
            };
            final List<String> games = new ArrayList<String>(Arrays.asList(list));
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, games);
            listView.setAdapter(arrayAdapter);

            //handlers for the click of the items in the list
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gameSelected = position;
                }
            });

            //Reset the list game
            gameList = new ArrayList<Game>();

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.GET,
                backendRoot + "/games?initName="+query, //Here we search in the database all the games that have the quarry term in it
                null,
                new Response.Listener<JSONObject>() { //Called on successful response
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArrey = response.getJSONArray("games");

                            //reset list
                            games.removeAll(games);
                            gameList.removeAll(gameList);

                            for(int i=0; i < jsonArrey.length(); i++){
                                JSONObject game = jsonArrey.getJSONObject(i);
                                String gameName = game.getString("gameName");
                                String steps = game.getString("numSteps");

                                //add new element to the list view
                                games.add(gameName+" ("+steps+" steps)");

                                //add game to internal game list
                                gameList.add(new Game(
                                        1,
                                        gameName,
                                        Integer.parseInt(steps))
                                );
                            }

                            arrayAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //Called on error response
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("That didn't work!");
                        System.out.println(error.toString());
                    }
                }
            );

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

    }

    public void joinSubmitButtonClick(View view){

        System.out.println(gameSelected);

        if(gameSelected == -1){
            Snackbar mySnackbar = Snackbar.make(view, R.string.noGameSelected, Snackbar.LENGTH_SHORT);
            mySnackbar.show();
            return;
        }


        //Allow to switch from the current Activity to the next
        //Intent intent = new Intent(SearchGameActivity.this, GameActivity.class);
        //startActivity(intent);
    }
}