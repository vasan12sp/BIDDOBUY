package com.vasan12sp.biddobuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewSellerProfile extends AppCompatActivity {

    private String name="", email="", address="", pincode="", password="", phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_seller_profile);

        Intent intent = getIntent();

        // Get the extra data ("phone") from the Intent
        phone = intent.getStringExtra("phone");


        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(phone);


        sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                address = snapshot.child("address").getValue(String.class);
                pincode = snapshot.child("pincode").getValue(String.class);
                password = snapshot.child("password").getValue(String.class);

                TextView namef,phonef,emailf,addressf,pincodef;

                namef=findViewById(R.id.namefield);
                phonef=findViewById(R.id.phonefield);
                emailf=findViewById(R.id.emailfield);
                addressf=findViewById(R.id.addressfield);
                pincodef=findViewById(R.id.pincodefield);
                addressf.setText(address);
                namef.setText(name);
                phonef.setText(phone);
                emailf.setText(email);
                pincodef.setText(pincode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button edit, delete, exit;

        edit = findViewById(R.id.editbtn);
        delete = findViewById(R.id.deletebtn);
        exit = findViewById(R.id.exitbtnvps);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}