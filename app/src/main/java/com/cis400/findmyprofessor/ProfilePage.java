
package com.cis400.findmyprofessor;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ProfilePage extends AppCompatActivity implements View.OnClickListener{
    //Initialize private TextView(type) variable for the register button
    private Button uploadPhoto;
    private Button logout;
    private Button takePhoto;
    private TextView TextView3;
    private Button GENERATE;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        FirebaseApp.initializeApp(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            finish();
            return;
        }

        //Initialize back / logout button
        logout= (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);
        //Initialize Register textview
        uploadPhoto = (Button) findViewById(R.id.uploadPhoto);
        uploadPhoto.setOnClickListener(this);

        // Initialize upload photo textview
        takePhoto = (Button) findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(this);

        // Initialize generate textview
        GENERATE = (Button) findViewById(R.id.GENERATE);
        GENERATE.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){
            case R.id.logout:
                //Take us to mainActivity class
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.takePhoto:
                //Take us to CameraActivity class
                // startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.uploadPhoto:
                //Take us to uploadPhoto class
                // startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.GENERATE:
                //Take us to GENERATE class
                // startActivity(new Intent(this, CameraActivity.class));
                break;
            default:
        }
    }
}