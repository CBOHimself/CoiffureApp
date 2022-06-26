package com.example.mobileappdevproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInScrn extends AppCompatActivity {

    private Button registerbutton;
    EditText signupemail,password;
    Button signinbutton;
    FirebaseAuth fAuth;
    ProgressBar signinprog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_scrn);
        getSupportActionBar().setTitle("Log In");


        signupemail = findViewById(R.id.signinemail);
        password = findViewById(R.id.signinpword);
        signinbutton = findViewById(R.id.signinbutton);
        registerbutton = findViewById(R.id.registerbutton);
        fAuth = FirebaseAuth.getInstance();
        signinprog = findViewById(R.id.signinprog);

        //Redirects to registration page
        registerbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openNewAccScrn();
            }
        });

        //Sign in listener
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signupemail.getText().toString().trim();
                String userpassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(email))   {
                    signupemail.setError("Enter an Email Address");
                    return;
                }

                if (TextUtils.isEmpty(userpassword))   {
                    password.setError("Enter a Password");
                    return;
                }

                if (userpassword.length() <= 6)   {
                    password.setError("Password must have 6 or more characters");
                    return;
                }

                //Progress Bar
                signinprog.setVisibility(View.VISIBLE);


                //User Sign in

                fAuth.signInWithEmailAndPassword(email, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())    {
                            Toast.makeText(LogInScrn.this, "Account Signed In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }   else {
                            Toast.makeText(LogInScrn.this,"Unable to Sign In:" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void openNewAccScrn() {
        Intent intent = new Intent(this, NewAccountScrn.class);
        startActivity(intent);
    }
}