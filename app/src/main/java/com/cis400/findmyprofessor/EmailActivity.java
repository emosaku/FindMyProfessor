package com.cis400.findmyprofessor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        // Change color of Status Bar (Top bar)
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }



//        mEditTextTo = findViewById(R.id.edit_text_to);
//        mEditTextSubject = findViewById(R.id.edit_text_subject);
//        mEditTextMessage = findViewById(R.id.edit_text_message);
//
//        Button buttonSend = findViewById(R.id.button_send);
//        buttonSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendMail();
//            }
//        });
//    }
//    // generate from: "users email" automatically
//
//    private void sendMail() {
//        String recipientList = mEditTextTo.getText().toString();
//        String[] recipients = recipientList.split(",");
//
//        String subject = mEditTextSubject.getText().toString();
//        String message = mEditTextMessage.getText().toString();
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//
//        intent.setType("message/rfc822");
//        startActivity(Intent.createChooser(intent, "Choose an email client"));
//    }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                //Take us to main method
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

    }

    // this event will enable the back
    // function to the button on press

}
