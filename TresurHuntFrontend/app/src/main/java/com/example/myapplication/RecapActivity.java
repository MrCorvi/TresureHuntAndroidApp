package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecapActivity extends AppCompatActivity {

    //private final String url ="http://10.0.2.2:8080/test"; //"localhost" doesn't work. Tested on emulator
    private final String url ="http://192.168.56.1:8080/games?initName=e";
    //private final String url = "https://postman-echo.com/get?foo1=bar1&foo2=bar2";
    //private final String url = "https://www.google.com";
    public static final String TAG = "MyTag";
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            list.add("Q: " + game.getQuestions().get(i)+ "\n"+ "A: " + game.getAnswers().get(i));
            System.out.println("Q: " + game.getQuestions().get(i)+ "\n"+ "A: " + game.getAnswers().get(i));
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RESPONSE: " + response);
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("VOLLEY ERROR:" + error.getMessage());
                }
        });

        //stringRequest.setTag(TAG);
        //stringRequest.setShouldCache(false); // By default, volley saves the response in its cache
                                       // and this behavior may cause some strange problem.
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(500, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        System.out.println("QUEUE ADD");
        queue.add(stringRequest);

        try {

            JSONObject jsonBody = new JSONObject();
            for(int i=0; i<game.getSize(); i++){
                JSONObject jsonTmp = new JSONObject();
                jsonTmp.put("gameId", 5);
                //jsonTmp.put("creator", game.getGameName());
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

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
        };

        /*
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }
            @Override
            public int getCurrentRetryCount() {
                return DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
                System.out.println("VOLLEY ERRORINO");
            }
        });*/

        //queue.add(stringRequest);

        Intent intent = new Intent(RecapActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
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
}