package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.example.myapplication.models.GameInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;


import java.io.Serializable;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MakerMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private int LAUNCH_PLACE = 1;
    private int LAUNCH_CAMERA = 2;
    private int LAUNCH_RECAP = 3;
    private final int MIN_STEP = 2;
    private String alertMessage;

    private GameInfo game;

    private GoogleMap mMap;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private LatLng CurrentLocation;
    private Boolean f_up_pos= true;
    Polyline last_route;
    MarkerOptions mo;
    Marker pos_marker;
    private ArrayList<LatLng> GameStepPosition = new ArrayList<>();
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker_map);
        
        alertMessage = getString(R.string.add_step);
        Intent i = getIntent();
        game = new GameInfo(i.getStringExtra("gameName"));
        //mapFragment allows to manage map fragment retrieved
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //geocoder = new Geocoder(this, Locale.getDefault());
        //GameStepPosition = new HashMap<>();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mo = new MarkerOptions().position(new LatLng(0, 0)).title(getString(R.string.current_location));
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            // Initialize the SDK

            String api_key = "AIzaSyB2tGjQcDtCAJtWBsMoF2MnC1wc-05ERJA";
            Places.initialize(getApplicationContext(),api_key);

            // Create a new PlacesClient instance
            PlacesClient placesClient = Places.createClient(this);

            //Criteria set parameters to access location service
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            String provider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(provider, 1000, 5, this);
        }
        if (!isLocationEnabled())
            showAlert(1);


    }

    public void placeClick(View view) {
        //recuperare le coordinate della posizione fissata attualmente
        LatLng c_latlng = new LatLng(CurrentLocation.latitude,CurrentLocation.longitude);
        game.getAnswers().add(c_latlng.toString()); // IMPORTANT : If you press back in PlaceFormActivity
        GameStepPosition.add(c_latlng);
        drawRoute();
        // the list will be corrupted. Not solved this issue
        // because in the final version it will be remove and
        // position evaluated and sent via intent in PlaceFormActivity
        Intent intent = new Intent(MakerMapActivity.this, PlaceFormActivity.class);
        startActivityForResult(intent, LAUNCH_PLACE);
    }

    public void cameraClick(View view) {
        Intent intent = new Intent(MakerMapActivity.this, CameraFormActivity.class);
        startActivityForResult(intent, LAUNCH_CAMERA);
    }

    public void finishClick(View view) {

        if (game.getSize() < MIN_STEP) {
            Toast.makeText(this, alertMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MakerMapActivity.this, RecapActivity.class);

        Bundle args_q = new Bundle();
        Bundle args_a = new Bundle();
        Bundle args_s = new Bundle();
        args_q.putSerializable("ARRAYLIST", (Serializable) game.getQuestions());
        args_a.putSerializable("ARRAYLIST", (Serializable) game.getAnswers());
        args_s.putSerializable("ARRAYLIST", (Serializable) game.getStepTypes());

        intent.putExtra("questionBundle", args_q);
        intent.putExtra("answerBundle", args_a);
        intent.putExtra("stepTypeBundle", args_s);
        intent.putExtra("gameName", game.getGameName());
        startActivityForResult(intent, LAUNCH_RECAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LAUNCH_PLACE) {
                game.getQuestions().add(data.getStringExtra("question"));
                game.getStepTypes().add(Boolean.TRUE);
                //find current address
                String location_title = makeTargetLocationTitle(CurrentLocation.latitude,CurrentLocation.longitude);
                //update map adding a marker on new step position
                mMap.addMarker(new MarkerOptions()
                        .position(CurrentLocation)
                        .title(location_title)
                        .snippet("STEP:"+ game.getSize())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
            }
            if (requestCode == LAUNCH_CAMERA) {
                game.getQuestions().add(data.getStringExtra("question"));
                game.getAnswers().add(data.getStringExtra("answer"));
                game.getStepTypes().add(Boolean.FALSE);
            }
            if (requestCode == LAUNCH_RECAP) {
                Bundle args_q = data.getBundleExtra("questionBundle");
                Bundle args_a = data.getBundleExtra("answerBundle");
                Bundle args_s = data.getBundleExtra("stepTypeBundle");
                ArrayList<String> q = (ArrayList<String>) args_q.getSerializable("ARRAYLIST");
                ArrayList<String> a = (ArrayList<String>) args_a.getSerializable("ARRAYLIST");
                ArrayList<Boolean> s = (ArrayList<Boolean>) args_s.getSerializable("ARRAYLIST");
                game.rebuild(q, a, s);
            }

            /*for(String q : questions){
                System.out.println(q);
            }
            for(String a: answers){
                System.out.println(a);
            }*/
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == LAUNCH_PLACE) {
                game.getAnswers().remove(game.getAnswers().size() - 1);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ImageButton placebutton = (ImageButton) findViewById(R.id.placeButton);
        placebutton.setEnabled(false);
        //disable button until position is loaded
        pos_marker = mMap.addMarker(mo);
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ));

    }

    @Override
    public void onLocationChanged(Location location) {
        //first update position
        if (f_up_pos) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
            f_up_pos=false;
            //place button activation
            ImageButton placebutton = (ImageButton) findViewById(R.id.placeButton);
            placebutton.setEnabled(true);
        }
        LatLng CurrentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        //keep last position registered
        CurrentLocation = CurrentCoordinates;
        //update position marker and view
        pos_marker.setPosition(CurrentCoordinates);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentCoordinates));
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

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isPermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(getString(R.string.log), getString(R.string.permission_granted));
            return true;
        } else {
            Log.v(getString(R.string.log), getString(R.string.permission_granted));
            return false;
        }
    }

    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = getString(R.string.permission_location);
            title = getString(R.string.enabled_location);
            btnText = getString(R.string.setting_location);
        } else {
            message = getString(R.string.permission_message);
            title = getString(R.string.permission_access);
            btnText = getString(R.string.grant);
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
                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();

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
                    strAdd = getString(R.string.unknown_place);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strAdd;
        }

        public void drawRoute(){
            if (last_route != null)
                last_route.remove();

            PolylineOptions line= new PolylineOptions();
            for (int i=0;i<GameStepPosition.size();i++){
                line.add(GameStepPosition.get(i))
                        .width(5).color(Color.RED);
            }

            last_route = mMap.addPolyline(line);
            last_route.setEndCap(new RoundCap());
            last_route.setJointType(JointType.ROUND);
            List<PatternItem> pattern = Arrays.asList(
                    new Dot(), new Gap(20), new Dash(30), new Gap(20));
            last_route.setPattern(pattern);
        }

}