package com.cis400.findmyprofessor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Initialize back button
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){

            case R.id.back:
                //Take us to main method
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:

        }

    }
}