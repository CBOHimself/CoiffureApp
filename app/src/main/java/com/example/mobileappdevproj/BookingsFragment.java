package com.example.mobileappdevproj;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class BookingsFragment extends Fragment {

    private Button businessBtn;


    public BookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        businessBtn = view.findViewById(R.id.businessButton);
        businessBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getActivity().getApplicationContext(),BusinessList.class);
                startActivity(intent);
            }
        } );

        return view;
    }


}