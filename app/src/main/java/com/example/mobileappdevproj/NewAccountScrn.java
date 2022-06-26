package com.example.mobileappdevproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;
import java.util.regex.Pattern;

public class NewAccountScrn extends AppCompatActivity {

    private Button backtosignin;
    EditText firstname, lastname, signupemail, phone, password, passwordreentry;
    Button signupbutton, businessAccBtn;
    FirebaseAuth fAuth;
    ProgressBar signupprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_scrn);
        getSupportActionBar().setTitle("Create New User Account");
        //Back Button
        backtosignin = findViewById(R.id.backsignin);
        backtosignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BackToSignIn();
            }
        });

        //Business Account Button
        businessAccBtn = findViewById(R.id.registerAsBusiness);
        businessAccBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BusinessAccount();
            }
        });

        firstname = findViewById(R.id.firstname);
        signupemail = findViewById(R.id.signupemail);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        passwordreentry = findViewById(R.id.passwordrentry);
        signupbutton = findViewById(R.id.completesignup);
        lastname = findViewById(R.id.lastname);

        fAuth = FirebaseAuth.getInstance();


        //Check if user is logged in
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        signupprog = findViewById(R.id.signinprog);


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signupemail.getText().toString().trim();
                String userpassword = password.getText().toString().trim();
                String userfname = firstname.getText().toString().trim();
                String userlname = lastname.getText().toString().trim();
                String userpasswordreentry = passwordreentry.getText().toString().trim();
                String userphone = phone.getText().toString().trim();

                if (TextUtils.isEmpty(email))   {
                    signupemail.setError("Enter an Email Address");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupemail.setError("Enter a valid Email Address");
                    return;
                }

                if (userphone.length() !=10) {
                    phone.setError("Enter a 10-digit phone number");
                    return;
                }
                if (TextUtils.isEmpty(userphone))   {
                    phone.setError("Enter a Password");
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

                if (TextUtils.isEmpty(userfname))   {
                    firstname.setError("Enter First Name");
                    return;
                }

                if (!userpassword.equals(userpasswordreentry)) {
                    passwordreentry.setError("Password does not match");
                    return;
                }
                if (TextUtils.isEmpty(userlname))   {
                    lastname.setError("Enter Last Name");
                    return;
                }
                else {
//                    signupprog.setVisibility(View.VISIBLE);
                    registerUser(email, userfname, userlname, userpassword, userphone);
                }

            }

            private void registerUser(String email, String userfname, String userlname, String userpassword, String userphone) {
                fAuth.createUserWithEmailAndPassword(email,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())    {

                            FirebaseUser firebaseUser = fAuth.getCurrentUser();


                            //Store other data in database
                            StoreUserDetails storeDetails = new StoreUserDetails(userfname, userlname, userphone);
                            DatabaseReference referenceDetails = FirebaseDatabase.getInstance().getReference("User Accounts");

                            referenceDetails.child(firebaseUser.getUid()).setValue(storeDetails).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Verification Email

                                    if (task.isSuccessful()) {
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText( NewAccountScrn.this, "Account Created", Toast.LENGTH_SHORT ).show();
                                        startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                                    } else {
                                        Toast.makeText(NewAccountScrn.this,"Error Occurred: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } );

                        }   else {
                            Toast.makeText(NewAccountScrn.this,"Error Occurred: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void BusinessAccount() {
        Intent intent = new Intent(this, NewBusinessAcc.class);
        startActivity(intent);
    }

    public void BackToSignIn() {
        Intent intent = new Intent(this, LogInScrn.class);
        startActivity(intent);
    }


}