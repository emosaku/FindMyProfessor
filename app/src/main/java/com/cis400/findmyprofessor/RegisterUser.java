package com.cis400.findmyprofessor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){
            //Posible Cases below
            case R.id.textView3:
                //Take us to register user layout
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.button:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            default:
        }

    }
}