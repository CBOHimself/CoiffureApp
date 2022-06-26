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

public class UserProfileSettings extends AppCompatActivity {

    private EditText editFirstName, editLastName, editEmail, editPhone;
    private String firstName, lastName, email, phoneNo;
    private FirebaseAuth fAuth;
    Button changesBtn, uploadPfpBtn, selectPfpBtn, backBtn;

    private StorageReference storageReference;
    Uri uri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Account Settings");
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_profile_settings );


        editFirstName = findViewById(R.id.changeFirstName);
        editLastName = findViewById(R.id.changeLastName);
        editEmail = findViewById(R.id.changeEmail);
        editPhone = findViewById(R.id.changePhoneNo);
        fAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("Profile_Pictures");
        FirebaseUser currentUser = fAuth.getCurrentUser();
        uri = currentUser.getPhotoUrl();


        //showUserDetails(currentUser);

        if (currentUser == null) {
            Toast.makeText(UserProfileSettings.this , "Could not obtain user details!", Toast.LENGTH_SHORT).show();
        }else {
            showUserDetails(currentUser);
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
                updateProfile(currentUser);
            }

            private void updateProfile(FirebaseUser currentUser) {

                String email = editEmail.getText().toString().trim();
                String userfname = editFirstName.getText().toString().trim();
                String userlname = editLastName.getText().toString().trim();
                String userphone = editPhone.getText().toString().trim();

                if (TextUtils.isEmpty(email))   {
                    editEmail.setError("Enter an Email Address");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editEmail.setError("Enter a valid Email Address");
                    return;
                }

                if (userphone.length() !=10) {
                    editPhone.setError("Enter a 10-digit phone number");
                    return;
                }
                if (TextUtils.isEmpty(userphone))   {
                    editPhone.setError("Enter a Password");
                    return;
                }


                if (TextUtils.isEmpty(userfname))   {
                    editFirstName.setError("Enter First Name");
                    return;
                }

                if (TextUtils.isEmpty(userlname))   {
                    editLastName.setError("Enter Last Name");
                    return;
                }
                else {

                    firstName = editFirstName.getText().toString();
                    lastName = editLastName.getText().toString();
                    phoneNo = editPhone.getText().toString();
                    ReadWriteUserDetails writeUserInfo = new ReadWriteUserDetails(firstName, lastName, phoneNo);

                    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("/User Accounts");
                    String userID = currentUser.getUid();

                    referenceUser.child(userID).setValue(writeUserInfo).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(firstName).build();
                                currentUser.updateProfile(profileUpdate);
                                Toast.makeText( UserProfileSettings.this, "Your changes have been saved!", Toast.LENGTH_SHORT ).show();

                                Intent intent = new Intent(UserProfileSettings.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText( UserProfileSettings.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                                }
                                
                            }
                        }
                    } );
                }
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
                    Toast.makeText(UserProfileSettings.this, "Photo uploaded", Toast.LENGTH_SHORT);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri = uri;
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
                    Toast.makeText(UserProfileSettings.this, "Photo Failed to Upload", Toast.LENGTH_SHORT);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

        } else  {
            Toast.makeText(UserProfileSettings.this, "Photo Not Selected", Toast.LENGTH_SHORT);
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

    private void showUserDetails(FirebaseUser currentUser) {
        String userID = currentUser.getUid();

        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("User Accounts");
        referenceUser.child(userID).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserInfo = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserInfo != null) {
                    firstName = readUserInfo.firstName;
                    email = currentUser.getEmail();
                    lastName = readUserInfo.lastName;
                    phoneNo = readUserInfo.phoneNumber;

                    editEmail.setText(email);
                    editFirstName.setText(firstName);
                    editLastName.setText(lastName);
                    editPhone.setText(phoneNo);

                }

                else{
                    Toast.makeText( UserProfileSettings.this, "Errror Occured!", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

    public void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}