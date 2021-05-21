package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.models.Game;
import com.example.myapplication.models.GlobalClass;
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
    private String backEndURL;
    private String [] list ;


    //list of games with all their proprieties
    private List<Game> gameList;
    //Is the index of the current selected gama in gameList
    //Set to -1, to mean that no  game was selected
    private int gameSelected = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_game);

        // Get Global Params
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        backEndURL = globalClass.getBackEndURL();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        // Assumes current activity is the searchable activity
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {

                // This is for when the activity is reloaded with the quarry searched in the bar
                Intent intent = getIntent();
                // Instantiate the RequestQueue.
                queue = Volley.newRequestQueue(SearchGameActivity.this);
                listView = findViewById(R.id.listview);


                list = new String[]{};
                final List<String> games = new ArrayList<String>(Arrays.asList(list));
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchGameActivity.this, android.R.layout.simple_list_item_1, games);
                listView.setAdapter(arrayAdapter);

                //handlers for the click of the items in the list
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("ResourceAsColor")
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
                        backEndURL.concat("/games?initName="+query), //Here we search in the database all the games that have the quarry term in it
                        null,
                        new Response.Listener<JSONObject>() { //Called on successful response
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArrey = response.getJSONArray("games");

                                    //reset list
                                    games.removeAll(games);
                                    gameList.removeAll(gameList);

                                    for(int i=0; i < jsonArrey.length(); i++){
                                        JSONObject game = jsonArrey.getJSONObject(i);
                                        Integer gameId = game.getInt("gameId");
                                        String gameName = game.getString("gameName");
                                        Integer steps = game.getInt("numSteps");

                                        //add new element to the list view
                                        games.add(gameName+" ("+steps+" steps): "+gameId);

                                        //add game to internal game list
                                        gameList.add(new Game(
                                                gameId,
                                                gameName,
                                                steps
                                        ));
                                    }

                                    arrayAdapter.notifyDataSetChanged();

                                    //enable button if we have more then one result
                                    Button joinButton = (Button) findViewById(R.id.joinSubmitGameButton);
                                    if(gameList.size() > 0) {
                                        joinButton.setEnabled(true);
                                    }else{
                                        joinButton.setEnabled(false);
                                    }
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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



    }

    public void joinSubmitButtonClick(View view){

        System.out.println(gameSelected);

        if(gameSelected <= -1){
            Snackbar mySnackbar = Snackbar.make(view, R.string.noGameSelected, Snackbar.LENGTH_SHORT);
            mySnackbar.show();
            return;
        }

        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(SearchGameActivity.this, GameActivity.class);
        Game game = gameList.get(gameSelected);
        intent.putExtra("gameId", game.id);
        startActivity(intent);
    }
}