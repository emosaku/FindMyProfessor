package com.cis400.findmyprofessor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Initialize private TextView(type) variable for the register button
    private TextView register;
    private TextView login;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Register textview
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        // Initialize login button
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);

        // Initialize forgetPassword textview
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){

            case R.id.register:
                //Take us to register user activity
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.login:
                //Take us to picture options activity
                startActivity(new Intent(this, CameraActivity.class));
                break;

            case R.id.forgotPassword:
                // take us to forget password activity
                startActivity(new Intent(this, ForgotPasswordActivity.class));

                break;

            default:
                break;
        }

    }

}