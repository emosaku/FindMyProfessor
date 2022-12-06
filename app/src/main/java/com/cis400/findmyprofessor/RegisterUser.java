//Latest Update: Oct 14, 2022
//Registers user, creates account and saves info in Firebase.


package com.cis400.findmyprofessor;
//package com.cis400.FindMyProf;
//import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private Button back;
    private TextView banner, registerUser;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        FirebaseApp.initializeApp(this);

        // Change color of Status Bar (Top bar)
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        mAuth = FirebaseAuth.getInstance();


        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

      
        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
    } 
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                registerUser();
                break;
            case R.id.back:
                //Take us to main method
                startActivity(new Intent(this, LoginActivity.class));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void registerUser() {
        //Grab the input data from the response fields.
        String email = editTextEmail.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String passWord = editTextPassword.getText().toString().trim();

        //************If statements to validate inputs************
        if (fullName.isEmpty()) {
            editTextFullName.setError("Full Name is Required.");
            editTextFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is Required.");
            editTextEmail.requestFocus();
            return;
        }

        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            editTextPassword.setError("Please Provide Valid Email.");
            editTextPassword.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            editTextAge.setError("Age is Required.");
            editTextAge.requestFocus();
            return;
        }


        if (passWord.isEmpty()) {
            editTextPassword.setError("Password is Required.");
            editTextPassword.requestFocus();
            return;
        }

        if (passWord.length() <= 6) {
            editTextPassword.setError("Minimum Password Length is 6 Characters.");
            editTextPassword.requestFocus();
            return;
        }
    
        //************ Done Validating ************

        //Progress bar visibility
        //progressBar.setVisibility(View.VISIBLE);
        System.out.println("Validation complete, Here are the inputs: ");
        System.out.println(fullName);
        System.out.println(email);
        System.out.println(age);
        System.out.println(passWord);

        //Send the registration info to Firebase for inspection.
        mAuth.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //Successful registration
                            //Create User
                            User user = new User(fullName, age, email, passWord);

                            //Send User object to the database.
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            if (task.isSuccessful()) {
                                                //Toast notification that user has been registered
                                                Toast.makeText(RegisterUser.this, "Registration Was a Success!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterUser.this, LoginActivity.class));

                                                //Progress Bar Visibility
                                                //progressBar.setVisibility(View.VISIBLE)

                                                //Sign in User and redirect to their profile.
                                                //... Coming Soon...
                                            } else {
                                                //Login Failed
                                                Toast.makeText(RegisterUser.this,"Something Went Wrong, Please Try Again.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}

