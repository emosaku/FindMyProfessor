//package com.cis400.FindMyProf;
//Last Update: Oct 30th
package com.cis400.findmyprofessor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cis400.findmyprofessor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Initialize private TextView variable for the register button,
    //EditText variables to store user password and email for checking,
    //Firebase variable, and progressbar.
    private TextView registerUser;
    private EditText userEmail, userPassword;
    private Button logInButton;
    private FirebaseAuth mAuth;
    //private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Register button
        registerUser = (TextView) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        userEmail = (EditText) findViewById(R.id.email);
        userPassword = (EditText) findViewById(R.id.password);

        logInButton = (Button)findViewById(R.id.login);
        logInButton.setOnClickListener(this);

        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){
            //Possible Cases below
            case R.id.register:
                //Take us to register user layout
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.login:
                //Log in the user. Validate login from Firebase, take us to the 
                userLogin();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }



    }

    private void userLogin() {
        //Initialize user login input from the EditText's.
        //Trim just in case theres an extra space.
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        //Make sure there is no inputs aren't Null.
        if (email.isEmpty()) {
            userEmail.setError("Email is Required.");
            userEmail.requestFocus();
            return;
        }
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            userEmail.setError("Please Provide Valid Email.");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            userPassword.setError("Email is Required.");
            userPassword.requestFocus();
            return;
        }

        if (password.length() <= 6) {
            userPassword.setError("Minimum Password Length is 6 Characters.");
            userPassword.requestFocus();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);

        //Sign User in
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Check task status.
                if(task.isSuccessful()){
                    //Send to user Profile
                    Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, ProfilePage.class));

                }
                else{
                    //Print failure message.
                    Toast.makeText(MainActivity.this, "Login Failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}