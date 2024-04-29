package com.vasan12sp.biddobuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerLogged extends AppCompatActivity {


    private String name="", email="", address="", pincode="", password="", phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_logged);


        // Get the Intent that started this activity
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

                TextView welcomeView = findViewById(R.id.welcometxt);
                welcomeView.setText("WELCOME " + name + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button viewProfile, addProducts, createAuctions,exit1,viewauctions;
        viewProfile = findViewById(R.id.profilebtn);
        addProducts = findViewById(R.id.addbtn);
        createAuctions = findViewById(R.id.createbtn);
        exit1 = findViewById(R.id.exitbtn);
        viewauctions = findViewById(R.id.viewauctions);

        viewauctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerLogged.this, ViewSellerProfile.class);
                intent.putExtra("phone", phone);
                startActivity(intent);

            }
        });

        addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerLogged.this, AddProducts.class);
                intent.putExtra("phone", phone);
                startActivity(intent);

            }
        });

        createAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerLogged.this, CreateAuctions.class);
                intent.putExtra("phone", phone);
                startActivity(intent);

            }
        });

        exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }
}