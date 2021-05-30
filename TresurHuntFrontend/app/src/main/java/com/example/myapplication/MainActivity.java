package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.models.GlobalClass;


public class MainActivity extends AppCompatActivity {

    private Button joinBut, makeBut;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int STORAGE_PERMISSION_CODE = 1;
    //static PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Global Params
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        globalClass.setBackEndURL(getString(R.string.back_end_url));

        joinBut = findViewById(R.id.joinButton);
        makeBut = findViewById(R.id.makeButton);
        requestCameraPermissions();
    }

    public void joinButtonClick(View view){
        //Allow to switch from the current Activity to the next
        Intent intent = new Intent(MainActivity.this, SearchGameActivity.class);
        startActivity(intent);
    }

    public void makeButtonClick(View view){
        Intent intent = new Intent(MainActivity.this, FormMakerActivity.class);
        startActivity(intent);
    }

    public void requestCameraPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_need)
                    .setMessage(R.string.permission_message)
                    .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }
    }

    // Request permissions for open the camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
