package com.example.mobileappdevproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobileappdevproj.databinding.ActivityMainBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //private Button logoutbutton;

//    Button profileButton;
//    Button mainMapButton;
//    Button calenderButton;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragments(new ProfileFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.mainMap:
                    changeFragments(new MapsFragment());
                    break;
                case R.id.profile:
                    changeFragments(new ProfileFragment());
                    break;
//                case R.id.Calender:
//                    changeFragments(new BookingsFragment());
//                    break;

            }
            return true;
        });

    }

    private void changeFragments(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.add(R.id.frameLayout, new ProfileFragment());
        fragmentTransaction.commit();

    }

}