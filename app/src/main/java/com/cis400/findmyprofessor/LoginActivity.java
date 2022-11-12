//package com.cis400.findmyprofessor;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.view.View;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener{
//
//    //Initialize private TextView(type) variable for the register button
//    private TextView register;
//    private TextView login;
//    private TextView forgotPassword;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //Initialize Register textview
//        register = (TextView) findViewById(R.id.register);
//        register.setOnClickListener(this);
//
//        // Initialize login button
//        login = (TextView) findViewById(R.id.login);
//        login.setOnClickListener(this);
//
//        // Initialize forgetPassword textview
//        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
//        forgotPassword.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v){
//        //Get Id of the click location
//        switch(v.getId()){
//
//            case R.id.register:
//                //Take us to register user activity
//                startActivity(new Intent(this, RegisterUser.class));
//                break;
//
//            case R.id.login:
//                //Take us to picture options activity
//                startActivity(new Intent(this, LoginActivity.class));
//                break;
//
//            case R.id.forgotPassword:
//                // take us to forget password activity
//                startActivity(new Intent(this, ForgotPasswordActivity.class));
//                break;
//
//            default:
//                break;
//        }
//
//    }
//
//}

//package com.cis400.FindMyProf;
//Last Update: Oct 25th
package com.cis400.findmyprofessor;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Patterns;
        import android.widget.TextView;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.FirebaseApp;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //Initialize private TextView variable for the register button,
    //EditText variables to store user password and email for checking,
    //Firebase variable, and progressbar.
    private TextView registerUser;
    private EditText userEmail, userPassword;
    private Button logInButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        FirebaseApp.initializeApp(this);

        //Initialize Register button
        registerUser = (TextView) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        userEmail = (EditText) findViewById(R.id.email);
        userPassword = (EditText) findViewById(R.id.password);

        logInButton = (Button) findViewById(R.id.login);
        logInButton.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        // Initialize forgotPassword
        // create onClicklister with a toast message when button is clicked
        // & Once button is clicked, open ForgotPasswordActivity
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }
//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(LoginActivity.this, "You can now reset your Password!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent( LoginActivity.this, ForgotPasswordActivity.class));
//            }
//        });
//    }

    @Override
    public void onClick(View v){
        //Get Id of the click location
        switch(v.getId()){
            //Possible Cases below
            case R.id.register:
                //Takes us to register user layout
                startActivity(new Intent(LoginActivity.this, RegisterUser.class));
                break;
            case R.id.login:
                //Takes us to login/home option activity
                startActivity(new Intent(LoginActivity.this, HomePage.class));
                break;
            case R.id.forgotPassword:
                // Takes us to forgotpassword activity
                Toast.makeText(LoginActivity.this, "You can now reset your Password!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent( LoginActivity.this, ForgotPasswordActivity.class));
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

        progressBar.setVisibility(View.VISIBLE);

        //Sign User in
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Check task status.
                if(task.isSuccessful()){
                    //Send to user Profile
                    startActivity(new Intent(LoginActivity.this, HomePage.class));
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Print failure message.
                    Toast.makeText(LoginActivity.this, "Login Failed. Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}