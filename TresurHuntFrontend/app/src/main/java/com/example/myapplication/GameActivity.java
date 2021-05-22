package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.models.GlobalClass;
import com.example.myapplication.models.Step;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.random;
import static java.lang.Math.sqrt;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    // Code Request
    static final int REQUEST_CODE = 0;
    private String backEndURL;

    private int gameId;
    private List<Step> stepList;
    private List<CircleOptions> hintList;
    private int hintStep = 0;
    private int currentStep = 0;

    private int maxPosSteps = 0;
    private int maxImgSteps = 0;

    private int maxHints = 3;
    private int hints = maxHints;
    private int imageHintsUsed = 0;

    private GoogleMap mMap;
    private boolean f_up_pos = true;
    public static final int INIT_REQUEST_CODE = 777;
    private MarkerOptions mo;
    private Marker pos_marker;
    private final int radius = 500;
    private LocationManager locationManager;
    private Location CurrentLocation;
    private Circle lastCircleHint;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get Global Params
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        backEndURL = globalClass.getBackEndURL();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.game_action_bar);

        ImageButton joinButton = (ImageButton) findViewById(R.id.mapButton);
        joinButton.setEnabled(false);

        //get the id of the selected game
        Intent intent = getIntent();
        gameId = intent.getIntExtra("gameId", 1);

        stepList = new ArrayList<Step>();
        hintList = new ArrayList<CircleOptions>();

        //Request the steps of the selected game
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                backEndURL + "/game?gameId=" + gameId, //Here we search in the database all the games that have the quarry term in it
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
                            for (int i = 0; i < jsonArrey.length(); i++) {
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

                                if (isPositionQuestion)
                                    nPos++;
                                else
                                    nImages++;
                            }

                            //Set the labels of the top-bar
                            TextView posLabel = (TextView) findViewById(R.id.pos_step_counter_label);
                            posLabel.setText("0/" + nPos.toString());
                            TextView imgLabel = (TextView) findViewById(R.id.image_step_counter_label);
                            imgLabel.setText("0/" + nImages.toString());

                            maxPosSteps = nPos;
                            maxImgSteps = nImages;
                            System.out.println(nImages);
                            //prepare hints for place steps
                            for (int i = 0; i < stepList.size(); i++) {
                                if (stepList.get(i).isPositionQuestion)
                                    hintList.add(hintLoader(i));
                            }

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            //Criteria set parameters to access location service
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mo = new MarkerOptions().position(new LatLng(0, 0)).title("My Current Location");
            String provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 5, this);
        }
        if (!isLocationEnabled())
            showAlert(1);


    }


    public CircleOptions hintLoader(int step){
        //elimina l'eventuale cerchio non presente
        //carica il livello attuale da risolvere
        System.out.println(stepList.toString());
        Step c_step = stepList.get(step);
        //mostra un raggio d'azione intorno alla zona di interesse
        Double[] latlng = getCoordinatesFromLocationString(c_step.answer);
        //calcola il centro dell'area di gioco aggiungendo rumore
        LatLng r_latlng = new LatLng(latlng[0],latlng[1]);
        LatLng n_latlng = addNoiseToCoordinates(r_latlng, radius);
        //draw map circle
        CircleOptions cl_circle = new CircleOptions()
                .center(n_latlng)
                .radius(radius)
                .strokeWidth(3f)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(70,150,50,50));
        return cl_circle;
    }

    public LatLng addNoiseToCoordinates(LatLng c, int r) {

        Double x =  (- r + ( random() * (2*r)));//new latitude
        Double y_range = sqrt(Math.pow(r,2)-Math.pow(x,2));
        Double y = - y_range + ( random() * (2*y_range));
        LatLng nll;
        if (x>=0)
            nll = SphericalUtil.computeOffset(c,x,90);//est 90
        else
            nll = SphericalUtil.computeOffset(c,x,270);//270 ovest
        if (y>=0)
            nll = SphericalUtil.computeOffset(nll,y,0);//360 nord
        else
            nll = SphericalUtil.computeOffset(nll,y,180);//180 sud
        return nll;

    }

    public void checkClick(View view){

        //Distinguish between type of steps
        boolean success = false;
        Step c_step = stepList.get(currentStep);
        //devo inizializzare la location relativa al prossimo step
        Double[] latlng = getCoordinatesFromLocationString(c_step.answer);

        System.out.println(c_step.answer);
        if(!c_step.isPositionQuestion){
            // TODO Camera and MKL Kit Controller
            gameCameraButtonClick();
        }else{
            // TODO Gianmarco: controllare le le coordinate attuali sono vinine a quelle dello step on answer
            Location targetLocation = new Location("");//fictitious provider
            targetLocation.setLatitude(latlng[0]);//your coords of course
            targetLocation.setLongitude(latlng[1]);
            float dist_from_goal = CurrentLocation.distanceTo(targetLocation);
            System.out.println(dist_from_goal);
            if (dist_from_goal<30)
                success= true;
            else if (dist_from_goal>= 30 && dist_from_goal <100)
                Toast.makeText(this, "Sei molto vicino alla meta!" , Toast.LENGTH_SHORT).show();
            else if (dist_from_goal>100 && dist_from_goal <= 1000)
                Toast.makeText(this, "Ti stai avvicinando!" , Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Non sei ancora sulla giusta strada!" , Toast.LENGTH_SHORT).show();

            stepController(success);
        }
    }

    // Control the number of the step
    public void stepController(Boolean success){
        Step c_step = stepList.get(currentStep);
        Double[] latlng = getCoordinatesFromLocationString(c_step.answer);
        //if success, next step
        if(success){
            //annuncia successo del task
            Toast.makeText(this, "Complimenti, hai superato questo step!" , Toast.LENGTH_LONG).show();
            //segnala graficamente la meta raggiunta
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latlng[0],latlng[1]))
                    .title(makeTargetLocationTitle(latlng[0],latlng[1]))
                    .snippet("STEP:"+ (currentStep+1))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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

    public static Double[] getCoordinatesFromLocationString(String loc){
        loc = loc.split("\\(")[1];
        String[] loc_array = loc.substring(0,loc.length()-1).split(",");
        System.out.println(loc_array[0]);
        System.out.println(loc_array[1]);
        //System.out.println(CurrentLocation.toString());

        Double lat = Double.parseDouble(loc_array[0]);
        Double lng = Double.parseDouble(loc_array[1]);
        return new Double[]{lat,lng};
    }

    public void listStepsClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(GameActivity.this, ListViewActivity.class);

        List<Step> stepsDone = new ArrayList<Step>();
        for(int i=0; i<=currentStep; i++){
            Step step = stepList.get(i);
            stepsDone.add(step);
        }
        intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) stepsDone);
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
            // Hangman Activity
            hangmanButtonClick();
        }else{
            // TODO Gianmarco deve far diminuire il raggio della mappa
            //verifico l'aiuto non sia già stato utilizzato
            if (hintStep <= currentStep){
            //carico il livello corrente
            System.out.println(hintList.get(hintStep).getCenter().toString());
            if (lastCircleHint != null) {
                lastCircleHint.remove();
            }
            lastCircleHint =mMap.addCircle(hintList.get(hintStep));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(hintList.get(hintStep).getCenter()));
            //aumento il livello
            hintStep = currentStep+1;
            }
            else{ Toast.makeText(this, "Hai già avuto il tuo aiuto!" , Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void hangmanButtonClick(){
        // Open GameCameraActivity
        Intent intent = new Intent(GameActivity.this, GameHangmanActivity.class);
        intent.putExtra("answer", stepList.get(currentStep).answer);
        intent.putExtra("hints", hints);
        startActivity(intent);
    }

    private void gameCameraButtonClick(){
        // Open GameCameraActivity
        Intent intent = new Intent(GameActivity.this, GameCameraActivity.class);
        intent.putExtra("answer", stepList.get(currentStep).answer);
        startActivityForResult(intent, REQUEST_CODE);
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
        //first update position
        if (f_up_pos) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
            f_up_pos=false;
        }

        LatLng CurrentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        //keep last position registered
        CurrentLocation = location;
        //update position marker and view
        pos_marker.setPosition(CurrentCoordinates);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentCoordinates));
        ///location.distanceTo() settare tolleranza -> condizione per chiamare step accomplished (<20)
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
        pos_marker = mMap.addMarker(mo);
        //zoom sul cursore
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );

    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isPermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("mylog", "Permission is granted");
            return true;
        } else {
            Log.v("mylog", "Permission not granted");
            return false;
        }
    }

    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();

    }

    // Game Camera Activity Return Data
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String returnedResult = data.getData().toString();
                stepController(Boolean.parseBoolean(returnedResult));
            }
        }
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