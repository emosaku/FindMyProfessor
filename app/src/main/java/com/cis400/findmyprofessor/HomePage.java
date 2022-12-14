package com.cis400.findmyprofessor;


import static android.Manifest.permission.CAMERA;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class HomePage extends AppCompatActivity implements View.OnClickListener{
    private static final int CAPTURE_CODE = 1012;

    private Button scanText;
    private Button logout;
    private Button takePhoto;
    private TextView scannedString;
    private TextView filteredString;
    private Button GENERATE;
    private FirebaseAuth mAuth;
    private ImageView capturedPic;
    private Uri image_uri = null;
    private Bitmap imageBitmap;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private TextRecognizer textRecognizer;
    private static final int REQUEST_CAMERA_CODE = 100;
    private static final int REQUEST_STORAGE_CODE = 101;
    String recognizedText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        FirebaseApp.initializeApp(this);

        // Change color of Status Bar (Top bar)
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        //Initialize back / logout button
        logout= (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        //Initialize Register textview
        scanText = (Button) findViewById(R.id.scanText);


        // Initialize upload photo textview
        takePhoto = (Button) findViewById(R.id.takePhoto);


        // Initialize generate textview
        GENERATE = (Button) findViewById(R.id.GENERATE);
        GENERATE.setOnClickListener(this);

        //Initialize capturedPic ImageView
        capturedPic = findViewById(R.id.capturedImage);

        scannedString = findViewById(R.id.scannedText);
        filteredString = findViewById(R.id.filteredText);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        takePhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                displayInputOptions();

            }
        });

        scanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_uri == null){
                    Toast.makeText(HomePage.this,"Image not selected", Toast.LENGTH_SHORT).show();
                }else{
                    scanTextFromImage();
                }
            }
        });

    }

    private void displayInputOptions() {
        PopupMenu inputMenu = new PopupMenu(this,takePhoto);

        inputMenu.getMenu().add(Menu.NONE, 1, 1, "CAMERA");
        inputMenu.getMenu().add(Menu.NONE, 2, 2, "GALLERY");

        inputMenu.show();

        inputMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 1){
                    if (checkCameraPermission()){
                        //Take picture with camera
                        pickImageFromCamera();
                    }else{
                        requestCameraPermission();
                    }

                }else if (id == 2){
                    //Upload picture from gallery
                    if (checkStoragePermission()){
                        //Take picture with camera
                        pickImageGallery();
                    }else{
                        requestStoragePermission();
                    }
                }
                return false;
            }
        });
    }

    private void pickImageGallery(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*"); // Allows us to pick the image file
        galleryActivityResultLauncher.launch(pickIntent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //We now receive the image from gallery, if picked
                    if (result.getResultCode() == Activity.RESULT_OK){
                        // Image was picked
                        Intent data = result.getData();
                        image_uri = data.getData();
                        capturedPic.setImageURI(image_uri);
                    }else{
                        Toast.makeText(HomePage.this,"Image is not picked", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void pickImageFromCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Material Image");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        cameraActivityResultLauncher.launch(cameraIntent);

    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //We now receive the image from camera, if picked
                    if (result.getResultCode() == Activity.RESULT_OK){
                        //We already have the image in image_uri from using the function pickImageFromCamera
                        capturedPic.setImageURI(image_uri);
                    }else{
                        Toast.makeText(HomePage.this,"Image not picked", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, REQUEST_STORAGE_CODE);
    }

    private boolean checkCameraPermission(){
        boolean cameraResult = ContextCompat.checkSelfPermission(this, CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return cameraResult && storageResult;

    }


    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, REQUEST_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CAMERA_CODE:{
                if (grantResults.length > 0 ){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        pickImageFromCamera();
                    }else{
                        Toast.makeText(this, "Camera & Storage permissions need to be granted", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(this, "Permissions have been denied", Toast.LENGTH_SHORT).show();
                }
            }
            case REQUEST_STORAGE_CODE:{
                if (grantResults.length > 0 ){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted){
                        pickImageGallery();
                    }else{
                        Toast.makeText(this, "Storage permission needs to be granted", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(this, "Permissions have been denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void scanTextFromImage() {
        try {
            InputImage inputImage = InputImage.fromFilePath(this,image_uri);

            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            recognizedText = text.getText();
                            //recognizedText = "Intro to Statistics Guide to Statistics";
                            //Update two Textview
                            scannedString.setText("Raw Text: " + recognizedText);
                            filteredString.setText("Filtered Text: " + filterText(recognizedText));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomePage.this, "Image not made available due to " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Image not made available due to " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private String filterText(String stringToFilter)
    {
        String[] recognizedTextArray = stringToFilter.split(" |, |\n|\n ");
        //String[] recognizedTextArray = "Intro to Statistics Guide to Statistics".split(" ");
        HashSet<String> filteredSet = new HashSet<>();
        String filteredString;

        //Get non blacklisted words into the Hashset
        for (String word : recognizedTextArray){
            //if word is contained in recognizedText
            if (!Arrays.asList(getResources().getStringArray(R.array.Blacklist)).contains(word.toLowerCase())){ //word good
                filteredSet.add(word);
            }
        }

        //Convert the HashSet to a String to be returned
        filteredString = filteredSet.toString(); //Will be in the form "[Geeks, For, Welcome, To]". Trim [] and split by ", "
        return filteredString;
    }

    @Override
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){

            case R.id.logout:
                //Take us to loginActivity class
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.GENERATE:
                //Take us to GENERATE class
                //Need to bring words over as well
                if(scannedString == null) {
                    Toast.makeText(this, "Scan image text first!", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Intent intent = new Intent(this, GenerateActivity.class);
                    intent.putExtra("keyFilteredWords", (filterText(recognizedText)));
                    recognizedText = null;  //Clear for return to the activity
                    startActivity(intent);
                }
                break;
            default:

        }

    }
}