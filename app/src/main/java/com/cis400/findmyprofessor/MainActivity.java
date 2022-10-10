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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Register button
        register = (TextView) findViewById(R.id.textView3);
        register.setOnClickListener(this);

        // Initialize login button
        register = (TextView) findViewById(R.id.button);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){
            //Posible Cases below
            case R.id.textView3:
                //Take us to register user layout
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.button:
                //take us to camera layout
                startActivity(new Intent(this, RegisterUser.class));
                break;
            default:
        }

    }


}