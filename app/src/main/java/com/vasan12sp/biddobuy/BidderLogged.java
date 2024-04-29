package com.vasan12sp.biddobuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BidderLogged extends AppCompatActivity {

    private String name="", email="", address="", pincode="", password="", phone;
    private List<String> liveAuctionIds= new ArrayList<>();;
    private DatabaseReference auctionsRef = FirebaseDatabase.getInstance().getReference("seller_auctions");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_logged);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Get the extra data ("phone") from the Intent
        phone = intent.getStringExtra("phone");


        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("Bidders").child(phone);


        sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                address = snapshot.child("address").getValue(String.class);
                pincode = snapshot.child("pincode").getValue(String.class);
                password = snapshot.child("password").getValue(String.class);

                TextView welcomeView = findViewById(R.id.welcomeView);
                welcomeView.setText("WELCOME " + name + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button viewProfile, logout, liveauctions;
        viewProfile = findViewById(R.id.viewprofile);
        liveauctions = findViewById(R.id.liveauctions);
        logout = findViewById(R.id.logout);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BidderLogged.this, ViewBidderProfile.class);
                intent.putExtra("phone", phone);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        liveauctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchLiveAuctionIds();
                Intent intent = new Intent(BidderLogged.this, LiveAuctions.class);
                intent.putExtra("phone", phone);
                String[] array = liveAuctionIds.toArray(new String[liveAuctionIds.size()]);
                intent.putExtra("currentAuctionIds",array);
                startActivity(intent);

            }
        });

    }

    private boolean isAuctionLive(String startTime, String startDate, String endTime, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date currentDate = Calendar.getInstance().getTime();

        try {
            Date auctionStartDate = dateFormat.parse(startDate + " " + startTime);
            Date auctionEndDate = dateFormat.parse(endDate + " " + endTime);

            Log.d("Debug", "Current date: " + currentDate);
            Log.d("Debug", "Auction start date: " + auctionStartDate);
            Log.d("Debug", "Auction end date: " + auctionEndDate);

            boolean isLive = currentDate.after(auctionStartDate) && currentDate.before(auctionEndDate);
            Log.d("Debug", "Is auction live: " + isLive);

            return isLive;
        } catch (ParseException e) {
            // Handle the parsing error
            e.printStackTrace(); // Log the error
            return false; // Return false or handle the error as needed
        }
    }


    public List<String> getLiveAuctionIds() {
        return liveAuctionIds;
    }

    public void fetchLiveAuctionIds() {
        auctionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sellerSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot categorySnapshot : sellerSnapshot.getChildren()) {
                        for (DataSnapshot auctionSnapshot : categorySnapshot.getChildren()) {
                            String startTime = auctionSnapshot.child("startTime").getValue(String.class);
                            String startDate = auctionSnapshot.child("startDate").getValue(String.class);
                            String endTime = auctionSnapshot.child("endTime").getValue(String.class);
                            String endDate = auctionSnapshot.child("endDate").getValue(String.class);

                            if (startTime != null && startDate != null && endTime != null && endDate != null) {
                                if (isAuctionLive(startTime, startDate, endTime, endDate)) {
                                    String auctionId = auctionSnapshot.getKey();
                                    liveAuctionIds.add(auctionId);
                                    System.out.println("Debug: " +auctionId);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}