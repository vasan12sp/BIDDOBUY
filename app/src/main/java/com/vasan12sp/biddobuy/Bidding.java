package com.vasan12sp.biddobuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bidding extends AppCompatActivity {

    TextView bidding; // Declare TextView here
    String phone;
    Float increment, currentbit,baseprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

        // Initialize TextView after setContentView
        bidding = findViewById(R.id.whoisbidding);

        Intent intent = getIntent();

        // Get the extra data ("phone") from the Intent
        String auctionId = intent.getStringExtra("auctionId");
        phone = intent.getStringExtra("phone");

        TextView productnameet, descriptionet, dateet, timeet, basepriceet, incrementet;
        productnameet = findViewById(R.id.productname);
        descriptionet = findViewById(R.id.description);
        timeet = findViewById(R.id.endtime);
        dateet = findViewById(R.id.enddate);
        basepriceet = findViewById(R.id.baseprice);
        incrementet = findViewById(R.id.incrementamt);


        Button placebid, cancel;
        placebid = findViewById(R.id.placebid);
        cancel = findViewById(R.id.exitauction);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        placebid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBidding(auctionId,phone,increment,baseprice);
                currentbit+=increment;
            }
        });

        DatabaseReference auctionsRef = FirebaseDatabase.getInstance().getReference("seller_auctions");

        auctionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot phoneSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot categorySnapshot : phoneSnapshot.getChildren()) {
                        for (DataSnapshot auctionSnapshot : categorySnapshot.getChildren()) {
                            String auctionId = auctionSnapshot.getKey();
                            DatabaseReference auctionReference = FirebaseDatabase.getInstance().getReference("seller_auctions")
                                    .child(phoneSnapshot.getKey())
                                    .child(categorySnapshot.getKey())
                                    .child(auctionId);

                            // Fetch data asynchronously
                            auctionReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // Retrieve data from snapshot
                                    String productName = snapshot.child("product").getValue(String.class);
                                    String description = snapshot.child("description").getValue(String.class);
                                    String endTime = snapshot.child("endTime").getValue(String.class);
                                    String endDate = snapshot.child("endDate").getValue(String.class);
                                    Float basePrice = snapshot.child("basePrice").getValue(Float.class);
                                    baseprice = basePrice;
                                    increment = snapshot.child("increment").getValue(Float.class);
                                    currentbit = basePrice;

                                    productnameet.setText(productName);
                                    descriptionet.setText(description);
                                    timeet.setText(endTime);
                                    dateet.setText(endDate);
                                    basepriceet.setText(basePrice.toString());
                                    incrementet.setText(increment.toString());
                                    // Use the retrieved data here
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }



    public void setBidding(String auctionId, String bidderPhone, Float increment, Float basePrice) {
        DatabaseReference bidRef = FirebaseDatabase.getInstance().getReference("bids").child(auctionId);

        bidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Float currentBid = dataSnapshot.child("currentBid").getValue(Float.class);
                    String currentBidderPhone = dataSnapshot.child("bidderPhone").getValue(String.class);

                    if (currentBid == null) {
                        currentBid = basePrice;
                    }

                    // Declare as effectively final
                    final Float finalCurrentBid = currentBid;

                    if (currentBidderPhone != null) {
                        if (!currentBidderPhone.equals(bidderPhone)) {
                            // Update the bid only if the bidder is different
                            DatabaseReference bidderRef = FirebaseDatabase.getInstance().getReference("Bidders").child(bidderPhone);
                            bidderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String bidderName = snapshot.child("name").getValue(String.class);
                                        if (bidderName != null) {
                                            // Use effectively final variable
                                            Float newBid = finalCurrentBid + increment;
                                            String biddingText = bidderName + " bid for " + newBid;
                                            bidding.setText(biddingText);

                                            // Update bid information in the database
                                            bidRef.child("currentBid").setValue(newBid);
                                            bidRef.child("bidderPhone").setValue(bidderPhone);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                        } else {
                            // Bidder is the same, do not allow placing bid again
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(findViewById(android.R.id.content), "You have already placed a bid for this auction", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    // No bids placed yet, proceed with placing the bid
                    DatabaseReference bidderRef = FirebaseDatabase.getInstance().getReference("Bidders").child(bidderPhone);
                    bidderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String bidderName = snapshot.child("name").getValue(String.class);
                                if (bidderName != null) {
                                    // Use effectively final variable
                                    Float newBid = basePrice;
                                    String biddingText = bidderName + " bid for " + newBid;
                                    bidding.setText(biddingText);

                                    // Update bid information in the database
                                    bidRef.child("currentBid").setValue(newBid);
                                    bidRef.child("bidderPhone").setValue(bidderPhone);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error fetching bid information: " + databaseError.getMessage());
            }
        });
    }



}
