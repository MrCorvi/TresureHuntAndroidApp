package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameCameraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String answer;
    private TextView titleText;
    private TextView descriptionText;
    private Button confirmButton;
    private ImageView imageView;
    final static String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_camera);

        //Get the id of the selected game
        Intent intent = getIntent();
        answer = intent.getStringExtra("answer");

        // Init Graph variables
        titleText = (TextView) findViewById(R.id.titleText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        dispatchTakePictureIntent();
    }

    // Intent openCamera and take a picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(GameCameraActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    // Machine Learning Kit. Detection of posible labels
    private void detectLabelFromImage(Bitmap imageBitmap){
        int rotationDegree = 0;
        InputImage image = InputImage.fromBitmap(imageBitmap, rotationDegree);
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        List<String> resultLabels = new ArrayList<String>();

                        // Task completed successfully
                        for (ImageLabel label : labels) {
                            String text = label.getText();
                            float confidence = label.getConfidence();
                            int index = label.getIndex();
                            resultLabels.add(label.getText().toLowerCase());
                        }

                        // Check if the correct answer is on result labels
                        checkLabel(resultLabels);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        Toast.makeText(GameCameraActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkLabel(List<String> resultLabels){
        // Check if resultLabels have the answer inside
        Boolean success = true;
        List<String> result = resultLabels
                .stream()
                .filter(x -> x.equals(answer.toLowerCase()))
                .collect(Collectors.toList());

        if (result.size() != 0){
            // On successful
            titleText.setText(R.string.found_image);
            descriptionText.setText(R.string.found_image_message);
            confirmButton.setText(R.string.continue_button);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfirmButtonClick(true);
                    onBackPressed();
                }
            });
                    //onConfirmButtonClick(success));
        } else {
            // On Failure
            titleText.setText(R.string.try_again);
            descriptionText.setText(R.string.try_again_message);
            confirmButton.setText(R.string.try_again);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfirmButtonClick(false);
                    onBackPressed();
                }
            });
                    //onConfirmButtonClick(success));
        }

    }

    public void onConfirmButtonClick(Boolean success) {
        Intent data = new Intent();

        // Set the data to pass back
        data.setData(Uri.parse(success.toString()));
        setResult(RESULT_OK, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            detectLabelFromImage(imageBitmap);
            imageView.setImageBitmap(imageBitmap);
        }
    }
}



