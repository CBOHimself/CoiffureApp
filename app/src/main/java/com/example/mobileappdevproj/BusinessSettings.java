package com.example.mobileappdevproj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class BusinessSettings extends AppCompatActivity {

    private EditText editbName, editbType, editEmail, editPhone, editAddress, editPostCode, editCity, editCountry, editDescription;
    private String businessName, businessType, email, phoneNo, address, postCode, city, country, description, photo;
    private FirebaseAuth fAuth;
    Button changesBtn, backBtn, uploadPfpBtn, selectPfpBtn;
    Uri uri;
    private StorageReference storageReference;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_business_settings );
        getSupportActionBar().setTitle("Account Settings");

        editbName = findViewById(R.id.changeBName);
        editbType = findViewById(R.id.changeBType);
        //editEmail = findViewById(R.id.changebEmail);
        editPhone = findViewById(R.id.changebPhoneNo);
        editAddress = findViewById(R.id.changeAddress);
        editPostCode = findViewById(R.id.changePostCode);
        editCity = findViewById(R.id.changeCity);
        editCountry = findViewById(R.id.changeCountry);
        editDescription = findViewById(R.id.changeDescription);

        storageReference = FirebaseStorage.getInstance().getReference("Profile_Pictures");
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentBusiness = fAuth.getCurrentUser();
        uri = currentBusiness.getPhotoUrl();


        if (currentBusiness == null) {
            Toast.makeText(BusinessSettings.this , "Could not obtain business details!", Toast.LENGTH_SHORT).show();
        }else {
            showBusinessDetails(currentBusiness);
        }

        uploadPfpBtn = findViewById(R.id.changePfp);
        uploadPfpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        selectPfpBtn = findViewById(R.id.selectPfp);
        selectPfpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }


        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });


        changesBtn = findViewById(R.id.saveBtn);
        changesBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(currentBusiness);
            }

            private void updateProfile(FirebaseUser currentBusiness) {


                String bName = editbName.getText().toString().trim();
                String bType = editbType.getText().toString().trim();
                String bPhone = editPhone.getText().toString().trim();
                String bAddress = editAddress.getText().toString().trim();
                String bCity = editCity.getText().toString().trim();
                String bPostCode = editPostCode.getText().toString().trim();
                String bCountry = editCountry.getText().toString().trim();
                String bDesc = editDescription.getText().toString().trim();

                if (bPhone.length() !=10) {
                    editPhone.setError("Enter a 10-digit phone number");
                    return;
                }
                if (TextUtils.isEmpty(bPhone))   {
                    editPhone.setError("Enter a Phone Number");
                    return;
                }

                if (TextUtils.isEmpty(bName))   {
                    editbName.setError("Enter Business Name");
                    return;
                }

                if (TextUtils.isEmpty(bType))   {
                    editbType.setError("Enter Business Type");
                    return;
                }

                if (TextUtils.isEmpty(bAddress))   {
                    editAddress.setError("Enter Address");
                    return;
                }
                if (TextUtils.isEmpty(bCity))   {
                    editCity.setError("Enter City");
                    return;
                }
                if (TextUtils.isEmpty(bCountry))   {
                    editCountry.setError("Enter Country");
                    return;
                }
                if (TextUtils.isEmpty(bPostCode))   {
                    editPostCode.setError("Enter Post Code");
                    return;
                }
                if (TextUtils.isEmpty(bDesc))   {
                    editDescription.setError("Enter Description");
                    return;
                }
                if (bDesc.length()>200)   {
                    editDescription.setError("Too many characters in Description");
                    return;
                }
                else {
                    businessName = editbName.getText().toString();
                    businessType = editbType.getText().toString();
                    phoneNo = editPhone.getText().toString();
                    address = editAddress.getText().toString();
                    city = editCity.getText().toString();
                    postCode = editPostCode.getText().toString();
                    country = editCountry.getText().toString();
                    description = editDescription.getText().toString();
                    photo = "";

                    ReadWriteBusinessDetails writeBusinessInfo = new ReadWriteBusinessDetails(businessName, businessType, phoneNo, address, city, postCode, country, description, photo);

                    DatabaseReference referenceBusiness = FirebaseDatabase.getInstance().getReference("Business Accounts");
                    String userID = currentBusiness.getUid();

                    referenceBusiness.child(userID).setValue(writeBusinessInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(businessName).build();
                                currentBusiness.updateProfile(profileUpdate);
                                Toast.makeText( BusinessSettings.this, "Your changes have been saved!", Toast.LENGTH_SHORT ).show();

                                Intent intent = new Intent(BusinessSettings.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                            else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText( BusinessSettings.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                                }

                            }

                        }
                    });


                }
            }

        });

    }

    private void showBusinessDetails(FirebaseUser currentBusiness) {
        String userID = currentBusiness.getUid();

        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Business Accounts");
        referenceUser.child(userID).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteBusinessDetails readUserInfo = snapshot.getValue(ReadWriteBusinessDetails.class);
                if (readUserInfo != null) {
                    businessName = readUserInfo.businessName;
                    email = currentBusiness.getEmail();
                    businessType = readUserInfo.businessType;
                    phoneNo = readUserInfo.phoneNumber;
                    address = readUserInfo.address;
                    city = readUserInfo.city;
                    country = readUserInfo.country;
                    postCode = readUserInfo.postcode;
                    description = readUserInfo.description;


                    editbName.setText(businessName);
                    editbType.setText(businessType);
                    editPhone.setText(phoneNo);
                    editAddress.setText(address);
                    editCity.setText(city);
                    editCountry.setText(country);
                    editPostCode.setText(postCode);
                    editDescription.setText(description);


                }

                else{
                    Toast.makeText( BusinessSettings.this, "Errror Occured!", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

    private void uploadPhoto() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading profile photo");
        progressDialog.show();

        if (uri != null) {


            //storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference fileReference = storageReference.child(fAuth.getCurrentUser().getUid() + "." + getFileExt(uri));
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(BusinessSettings.this, "Photo uploaded", Toast.LENGTH_SHORT);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri = uri;

                                photo = downloadUri.toString();
                                FirebaseUser currentUser = fAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                                //UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest().Builder().setPhotoUri(downloadUri).build();
                                currentUser.updateProfile(profileUpdate);


                            }
                        });
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BusinessSettings.this, "Photo Failed to Upload", Toast.LENGTH_SHORT);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

        } else  {
            Toast.makeText(BusinessSettings.this, "Photo Not Selected", Toast.LENGTH_SHORT);
        }
    }
    //Get File Extentions of Profile Pic
    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void selectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {
            uri = data.getData();



        }

    }

    public void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}