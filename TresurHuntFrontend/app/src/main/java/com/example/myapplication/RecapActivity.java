
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapters.RecycleAdapter;
import com.example.myapplication.models.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RecapActivity extends AppCompatActivity {

    private String backEndURL;
    private final String alertMessage = "Min length achieved";

    private Game game;
    private RequestQueue queue;
    private String requestBody = "ciao";

    ArrayList<String> list;

    RecyclerView recyclerView;
    RecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        // Get Global Params
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        backEndURL = globalClass.getBackEndURL();

        queue = Volley.newRequestQueue(this);

        list = new ArrayList<String>();

        Intent intent = getIntent();
        String gameName = intent.getStringExtra("gameName");
        Bundle args_q = intent.getBundleExtra("questionBundle");
        Bundle args_a = intent.getBundleExtra("answerBundle");
        Bundle args_s = intent.getBundleExtra("stepTypeBundle");
        ArrayList<String> q = (ArrayList<String>) args_q.getSerializable("ARRAYLIST");
        ArrayList<String> a= (ArrayList<String>) args_a.getSerializable("ARRAYLIST");
        ArrayList<Boolean> s = (ArrayList<Boolean>) args_s.getSerializable("ARRAYLIST");

        game = new Game(gameName, q,a,s);

        for(int i=0; i<game.getSize(); i++){
            Boolean cst_isplace = game.getStepTypes().get(i);
            String ans = game.getAnswers().get(i);
            //gestione domanda di tipo place
            if (cst_isplace){
                Double[] ll = GameActivity.getCoordinatesFromLocationString(ans);
                //chiedi a title_maker
                ans = makeTargetLocationTitle(ll[0],ll[1]);
            }


            //gestire lunghezza stringa maggiore di 35 con ...
            if (ans.length()>33)
                ans = ans.substring(0,34)+"...";
            list.add("Q: " + game.getQuestions().get(i)+ "\n"+ "A: " + ans);
            System.out.println("Q: " + game.getQuestions().get(i)+ "\n"+ "A: " + ans);
        }

        //instantiate custom adapter
        adapter = new RecycleAdapter(list,game, this);

        recyclerView = (RecyclerView)findViewById(R.id.recapList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(list,position_dragged,position_target);

                game.swap(position_dragged,position_target);
                adapter.notifyItemMoved(position_dragged,position_target);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        });
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    public void onConfirmClick(View view){

        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, backEndURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RESPONSE: " + response);
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("VOLLEY ERROR:" + error.toString());
                }
        });*/

        JSONObject jsonBody = new JSONObject();
        try {

            for(int i=0; i<game.getSize(); i++){

                JSONObject jsonTmp = new JSONObject();
                //jsonTmp.put("gameId", -1); //it will be setted in the backend
                jsonTmp.put("gameName", game.getGameName());
                jsonTmp.put("step", i+1);
                jsonTmp.put("question", game.getQuestions().get(i));
                jsonTmp.put("answer", game.getAnswers().get(i));
                jsonTmp.put("stepType", game.getStepTypes().get(i));

                jsonBody.accumulate("game", jsonTmp);
            }

            requestBody = jsonBody.toString();
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("REQUEST BODY: " + requestBody);

        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.POST, 
                backEndURL.concat("/game"), jsonBody,
                new Response.Listener<JSONObject>() { //Called on successful response
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            int gameId = response.getInt("gameId");

                            Intent intent = new Intent(RecapActivity.this, SuccessCreationActivity.class);
                            intent.putExtra("gameId",gameId);
                            startActivity(intent);
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

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, backEndURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };*/

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        Intent intent = new Intent(RecapActivity.this, MakerMapActivity.class);

        Bundle args_q = new Bundle();
        Bundle args_a = new Bundle();
        Bundle args_s = new Bundle();
        args_q.putSerializable("ARRAYLIST",(Serializable)game.getQuestions());
        args_a.putSerializable("ARRAYLIST",(Serializable)game.getAnswers());
        args_s.putSerializable("ARRAYLIST",(Serializable)game.getStepTypes());

        intent.putExtra("questionBundle",args_q);
        intent.putExtra("answerBundle",args_a);
        intent.putExtra("stepTypeBundle",args_s);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public String makeTargetLocationTitle(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                strAdd = "Unknown place";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
}