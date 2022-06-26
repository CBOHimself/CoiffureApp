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

public class NewBusinessAcc extends AppCompatActivity {

    private Button backtosignin;
    EditText businessName, businessType, signupemail, phone, password, passwordreentry, address, postcode, city, country, description;
    Button signupbutton, userAccBtn;
    FirebaseAuth fAuth;
    ProgressBar signupprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_business_acc );
        getSupportActionBar().setTitle("Create New Business Account");

        //Back Button
        backtosignin = findViewById(R.id.backsignin);
        backtosignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BackToSignIn();
            }
        });

        //Business Account Button
        userAccBtn = findViewById(R.id.registerAsUser);
        userAccBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UserAccount();
            }
        });

        businessName = findViewById(R.id.businessName);
        signupemail = findViewById(R.id.signupemail);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        passwordreentry = findViewById(R.id.passwordrentry);
        signupbutton = findViewById(R.id.completesignup);
        businessType = findViewById(R.id.businessType);
        address = findViewById(R.id.address);
        postcode = findViewById(R.id.postcode);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        description = findViewById(R.id.description);

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
                String bName = businessName.getText().toString().trim();
                String bType = businessType.getText().toString().trim();
                String userpasswordreentry = passwordreentry.getText().toString().trim();
                String bPhone = phone.getText().toString().trim();
                String bAddress = address.getText().toString().trim();
                String bCity = city.getText().toString().trim();
                String bPostCode = postcode.getText().toString().trim();
                String bCountry = country.getText().toString().trim();
                String bDesc = description.getText().toString().trim();
                String bPhoto = " ";


                if (TextUtils.isEmpty(email))   {
                    signupemail.setError("Enter an Email Address");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupemail.setError("Enter a valid Email Address");
                    return;
                }

                if (bPhone.length() !=10) {
                    phone.setError("Enter a 10-digit phone number");
                    return;
                }
                if (TextUtils.isEmpty(bPhone))   {
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

                if (TextUtils.isEmpty(bName))   {
                    businessName.setError("Enter First Name");
                    return;
                }

                if (!userpassword.equals(userpasswordreentry)) {
                    passwordreentry.setError("Password does not match");
                    return;
                }
                if (TextUtils.isEmpty(bType))   {
                    businessType.setError("Enter Last Name");
                    return;
                }
                if (TextUtils.isEmpty(bAddress))   {
                    address.setError("Enter Address");
                    return;
                }
                if (TextUtils.isEmpty(bCity))   {
                    city.setError("Enter City");
                    return;
                }
                if (TextUtils.isEmpty(bCountry))   {
                    country.setError("Enter Country");
                    return;
                }
                if (TextUtils.isEmpty(bPostCode))   {
                    postcode.setError("Enter Post Code");
                    return;
                }
                if (TextUtils.isEmpty(bDesc))   {
                    description.setError("Enter Description");
                    return;
                }
                if (bDesc.length()>200)   {
                    description.setError("Too many characters in Description");
                    return;
                }
                else {
//                    signupprog.setVisibility(View.VISIBLE);
                    registerBusiness(email, bName, bType, userpassword, bPhone, bAddress, bPostCode, bCity, bCountry, bDesc, bPhoto);
                }

            }
            private void registerBusiness(String email, String bName, String bType, String userpassword, String bPhone, String bAddress, String bPostCode, String bCity, String bCountry, String bDesc, String bPhoto) {
                fAuth.createUserWithEmailAndPassword(email,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())    {

                            FirebaseUser firebaseUser = fAuth.getCurrentUser();


                            //Store other data in database
                            StoreBusinessDetails storeDetails = new StoreBusinessDetails(bName, bType, bPhone, bAddress, bCity, bCountry, bPostCode, bDesc, bPhoto);
                            DatabaseReference referenceDetails = FirebaseDatabase.getInstance().getReference("Business Accounts");

                            referenceDetails.child(firebaseUser.getUid()).setValue(storeDetails).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Verification Email

                                    if (task.isSuccessful()) {
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText( NewBusinessAcc.this, "Account Created", Toast.LENGTH_SHORT ).show();
                                        startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                                    } else {
                                        Toast.makeText(NewBusinessAcc.this,"Error Occurred: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } );

                        }   else {
                            Toast.makeText(NewBusinessAcc.this,"Error Occurred: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });
    }


    public void UserAccount() {
        Intent intent = new Intent(this, NewAccountScrn.class);
        startActivity(intent);
    }

    public void BackToSignIn() {
        Intent intent = new Intent(this, LogInScrn.class);
        startActivity(intent);
    }

}