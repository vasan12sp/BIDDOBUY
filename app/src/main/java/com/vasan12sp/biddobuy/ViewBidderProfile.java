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

public class ViewBidderProfile extends AppCompatActivity {

    private String name="", email="", address="", pincode="", password="", phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_register_profile);

        Intent intent = getIntent();

        // Get the extra data ("phone") from the Intent
        phone = intent.getStringExtra("phone");

        DatabaseReference bidderRef = FirebaseDatabase.getInstance().getReference("Bidders").child(phone);

        bidderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                address = snapshot.child("address").getValue(String.class);
                pincode = snapshot.child("pincode").getValue(String.class);
                password = snapshot.child("password").getValue(String.class);

                TextView namef,phonef,emailf,addressf,pincodef;

                namef=findViewById(R.id.regname);
                phonef=findViewById(R.id.regphone);
                emailf=findViewById(R.id.regmail);
                addressf=findViewById(R.id.regaddress);
                pincodef=findViewById(R.id.regpincode);
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

        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.deleteaccnt);
        exit = findViewById(R.id.exit);

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