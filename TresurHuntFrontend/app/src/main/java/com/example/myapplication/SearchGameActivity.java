package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchGameActivity extends AppCompatActivity {

    private ListView listView;
    private RequestQueue queue;
    private String backendRoot = "http://192.168.1.4:8080";
    private String [] list = {
            "Casa",
            "Di",
            "Tua",
            "Madre"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This is for when the activity is reloaded with the quarry searched in the bar
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println(query);

            // Instantiate the RequestQueue.
            queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    backendRoot + "/games?initName="+query, //Here we search in the database all the games that have the quarry term in it
                    null,
                    new Response.Listener<JSONObject>() { //Called on successful response
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArrey = response.getJSONArray("game");
                                for(int i=0; i < jsonArrey.length(); i++){
                                    JSONObject game = jsonArrey.getJSONObject(i);
                                    String gameName = game.getString("creator");
                                    System.out.println(gameName);
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
                    });
            /*StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                backendRoot+"/game?gameId=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("That didn't work!");
                        System.out.println(error.toString());
                    }
                }
            );*/

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

        //here we create the list view object, select a list style and assign the array of object we want to display
        listView = findViewById(R.id.listview);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

    }

    public void joinSubmitButtonClick(View view){
        //text.setText("Join");
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(SearchGameActivity.this, GameActivity.class);
        startActivity(intent);
    }
}