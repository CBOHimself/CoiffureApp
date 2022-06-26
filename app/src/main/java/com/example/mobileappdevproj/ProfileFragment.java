package com.example.mobileappdevproj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {


    private TextView dispFullname, dispEmail, dispUsername, dispPhone;
    private ImageView dispPfp;
    private Button logOutBtn, settingsBtn;
    private String firstName, lastName, email, phoneNo, businessName, businessType;

    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dispFullname = view.findViewById(R.id.displayFullname);
        dispEmail = view.findViewById(R.id.displayEmail);
        dispUsername = view.findViewById(R.id.displayUserName);
        dispPhone = view.findViewById(R.id.displayPhone);
        dispPfp = view.findViewById(R.id.displayPfp);
        logOutBtn = view.findViewById(R.id.logOut);
        settingsBtn = view.findViewById(R.id.accountSettings);


        logOutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(),LogInScrn.class));
                getActivity().finish();
            }
        } );



        settingsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (businessName == null) {
                    startActivity(new Intent(getActivity().getApplicationContext(),UserProfileSettings.class));
                    getActivity().finish();

                }
                else if (firstName == null) {
                    startActivity(new Intent(getActivity().getApplicationContext(),BusinessSettings.class));
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getActivity(),"Unable to retrieve details", Toast.LENGTH_SHORT);
                }
                //startActivity(new Intent(getActivity().getApplicationContext(),UserProfileSettings.class));
                //getActivity().finish();

            }
        } );



        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getActivity(), "Could not obtain user details!", Toast.LENGTH_SHORT).show();
        }else {
            getUserData(currentUser);
        }

        return view;
    }

    private void getUserData(FirebaseUser currentUser) {
        String userID = currentUser.getUid();

        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("User Accounts");
        referenceUser.child(userID).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StoreUserDetails readUserInfo = snapshot.getValue(StoreUserDetails.class);
                if (readUserInfo != null) {
                    firstName = readUserInfo.firstName;
                    email = currentUser.getEmail();
                    lastName = readUserInfo.lastName;
                    phoneNo = readUserInfo.phoneNumber;

                    dispEmail.setText(email);
                    dispFullname.setText(firstName + " " + lastName);
                    dispPhone.setText(phoneNo);
                    dispUsername.setText(firstName);



                    Uri uri = currentUser.getPhotoUrl();
                    Picasso.with(getActivity()).load(uri).fit().centerCrop().into(dispPfp);
                    //Picasso.with(getActivity()).load(uri).into(dispPfp);




                }   else {
                    Toast.makeText(getActivity(), "An Error Occurred", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error Occurred with authentication", Toast.LENGTH_SHORT);
            }
        } );

        DatabaseReference referenceBusiness = FirebaseDatabase.getInstance().getReference("Business Accounts");
        referenceBusiness.child(userID).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StoreBusinessDetails readBusinessInfo = snapshot.getValue(StoreBusinessDetails.class);
                if (readBusinessInfo != null) {
                    businessName = readBusinessInfo.businessName;
                    email = currentUser.getEmail();
                    businessType = readBusinessInfo.businessType;
                    phoneNo = readBusinessInfo.phoneNumber;

                    dispEmail.setText(email);
                    dispFullname.setText(businessName + " " + businessType);
                    dispPhone.setText(phoneNo);
                    dispUsername.setText(businessName);

                    Uri uri = currentUser.getPhotoUrl();
                    Picasso.with(getActivity()).load(uri).fit().centerCrop().into(dispPfp);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error Occured with authentication", Toast.LENGTH_SHORT);
            }
        } );
    }

        public class CropSquareTransformation implements Transformation {
            @Override
            public Bitmap transform(Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;
                Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
                if (result != source) {
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return null;
            }
        }

    }