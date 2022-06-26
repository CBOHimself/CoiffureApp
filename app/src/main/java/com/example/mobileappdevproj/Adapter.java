package com.example.mobileappdevproj;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {




    private static final int REQUEST_CALL = 1;
    Context context;
    ArrayList<BusinessData> list;

    public Adapter(Context context, ArrayList<BusinessData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.business_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusinessData businessDetails= list.get(position);
        holder.businessName.setText(businessDetails.getBusinessName());
        holder.businessType.setText(businessDetails.getBusinessType());
        holder.description.setText(businessDetails.getDescription());
        holder.phoneNumber.setText(businessDetails.getPhoneNumber());
        holder.address.setText(businessDetails.getAddress());
        holder.city.setText(businessDetails.getCity());
        holder.country.setText(businessDetails.getCountry());
        holder.postcode.setText(businessDetails.getPostcode());





    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView businessName, businessType, description, phoneNumber, address, city, country, postcode, photo;
        ImageView PfpCard;
        Button bookBtn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            businessName = itemView.findViewById(R.id.bNameCard);
            businessType = itemView.findViewById(R.id.bTypeCard);
            description = itemView.findViewById(R.id.descriptionCard);
            phoneNumber = itemView.findViewById(R.id.phoneNo);
            address = itemView.findViewById(R.id.bAddress);
            city = itemView.findViewById(R.id.bCity);
            country = itemView.findViewById(R.id.bCountry);
            postcode = itemView.findViewById(R.id.bPostCode);

            bookBtn = itemView.findViewById(R.id.bookBtn);
            bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = phoneNumber.getText().toString();
                    if (number.trim().length()>0) {
                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) view.getContext(), new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                        }
                        else {
                            //String number = phoneNumber.getText().toString();;
                            String dial = "tel:" + number;

                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                            context.startActivity(intent);
                        }

                    }
                    else {
                        Toast.makeText(view.getContext(), "Error Calling Business", Toast.LENGTH_SHORT);
                    }
                }


            });



        }

    }




}
