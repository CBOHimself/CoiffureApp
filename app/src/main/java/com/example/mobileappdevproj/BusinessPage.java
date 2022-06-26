package com.example.mobileappdevproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;

public class BusinessPage extends AppCompatActivity {

    TextView businessName, businessType, bEmail, bPhone, bDesc;
    Button bookBtn, backBtn;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);
        getSupportActionBar().setTitle("Businesses Page");

        businessName = findViewById(R.id.displayBName);
        businessType = findViewById(R.id.displayBType);
        bEmail = findViewById(R.id.displayEmail);
        bPhone = findViewById(R.id.displayPhone);
        bDesc = findViewById(R.id.dispDescription);

        bookBtn = findViewById(R.id.bookBtn);
        backBtn = findViewById(R.id.backBtn);

        //dbReference = FirebaseDatabase.getInstance().getReference().child("Business Accounts");
        //dbReference = FirebaseDatabase.getInstance().getReference("Business Accounts");

        //String businessKey = getIntent().getStringExtra("bKey");

//        dbReference.child(businessKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String bName = dataSnapshot.child("businessName").getValue().toString();
//                    String bType = dataSnapshot.child("businessType").getValue().toString();
//                    String bCity = dataSnapshot.child("city").getValue().toString();
//                    String bAddress = dataSnapshot.child("address").getValue().toString();
//                    String bCountry = dataSnapshot.child("country").getValue().toString();
//                    String bPostCode = dataSnapshot.child("postcode").getValue().toString();
//                    String bPhoneNo = dataSnapshot.child("phoneNumber").getValue().toString();
//                    String bDescription = dataSnapshot.child("description").getValue().toString();
//
//
//                    businessName.setText(bName);
//                    businessType.setText(bType);
//                    bPhone.setText(bPhoneNo);
//                    bDesc.setText(bDescription + "\n" + bAddress + "\n" + bPostCode + "\n" + bCity + "\n" + bCountry);
//                }
//
//                else {
//                    Toast.makeText(BusinessPage.this, "No data", Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        DatabaseReference referenceBusiness = FirebaseDatabase.getInstance().getReference("Business Accounts");




    }
}