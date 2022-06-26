package com.example.mobileappdevproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusinessList extends AppCompatActivity {

    private static final int REQUEST_CALL = 0;
    RecyclerView recyclerView;
    DatabaseReference databaseReference, dbReference;
    Adapter adapter;
    ArrayList<BusinessData> list;
    Button backBtn, requestPermission;
    FirebaseRecyclerOptions<BusinessData> options;
    FirebaseRecyclerAdapter<BusinessData, Adapter.ViewHolder> frAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        getSupportActionBar().setTitle("List of Businesses");


        dbReference = FirebaseDatabase.getInstance().getReference().child("Business Accounts");

        recyclerView = findViewById(R.id.businessList);
        recyclerView.hasFixedSize();
        databaseReference = FirebaseDatabase.getInstance().getReference("Business Accounts");
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


       list = new ArrayList<BusinessData>();

        databaseReference.orderByChild("businessName");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    BusinessData business = dataSnapshot.getValue(BusinessData.class);

                    list.add(business);
                }


                adapter = new Adapter(BusinessList.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });

//        requestPermission = findViewById(R.id.requestPermission);
//        requestPermission.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(BusinessList.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) view.getContext(), new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);}
//                else {
//                    Toast.makeText(BusinessList.this, "PERMISSION GRANTED", Toast.LENGTH_SHORT);
//                }
//            }
//        });



    }

    public void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

//    public void getPfp() {
//
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Profile_Pictures");
//        FirebaseUser currentUser = fAuth.getCurrentUser();
//
//        Uri uri = currentUser.getPhotoUrl();
//        //Picasso.with(getActivity()).load(uri).fit().centerCrop().into(dispPfp);
//
//        FirebaseAuth fAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = fAuth.getCurrentUser();
//        String userID = currentUser.getUid();
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }

            else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT);
            }
        }
    }
}