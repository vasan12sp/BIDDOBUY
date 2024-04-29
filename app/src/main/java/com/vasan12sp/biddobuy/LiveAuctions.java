package com.vasan12sp.biddobuy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiveAuctions extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> auctionList;
    private String phone; // Declare phone as a class-level variable

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_auctions);

        listView = findViewById(R.id.listview);
        auctionList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, auctionList);
        listView.setAdapter(adapter);

        // Get auction IDs from the intent
        String[] auctionIds = getIntent().getStringArrayExtra("currentAuctionIds");
        phone = getIntent().getStringExtra("phone"); // Assign phone from the intent
        if (auctionIds != null) {
            fetchAuctions(auctionIds);
        }

        // Initialize exit button and set OnClickListener
        Button exit1 = findViewById(R.id.exitbtn1);
        exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected auction item
                String selectedItem = auctionList.get(position);

                // Split the selected item to extract the auction ID and category
                String[] parts = selectedItem.split("\n");
                String auctionId = parts[0]; // Auction ID is the first part
                String productNameAndCategory = parts[1]; // Product name and category are the second part

                // Split the product name and category
                String[] productParts = productNameAndCategory.split(" - ");
                String productName = productParts[0];
                String category = productParts[1];

                // Create an intent to shift to Bidding.class
                Intent intent = new Intent(LiveAuctions.this, Bidding.class);

                // Pass auction ID and phone number as extras
                intent.putExtra("auctionId", auctionId);
                intent.putExtra("phone", phone);

                // Start the activity
                startActivity(intent);
            }
        });
    }

    private void fetchAuctions(String[] auctionIds) {
        DatabaseReference auctionsRef = FirebaseDatabase.getInstance().getReference("seller_auctions");

        auctionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sellerSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot categorySnapshot : sellerSnapshot.getChildren()) {
                        for (DataSnapshot auctionSnapshot : categorySnapshot.getChildren()) {
                            String auctionId = auctionSnapshot.getKey();
                            if (Arrays.asList(auctionIds).contains(auctionId)) {
                                String productName = auctionSnapshot.child("product").getValue(String.class);
                                String category = auctionSnapshot.child("category").getValue(String.class);
                                if (productName != null && category != null) {
                                    auctionList.add(auctionId + "\n" + productName + " - " + category);
                                }
                            }
                        }
                    }
                }
                // Notify adapter after adding all auctions
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Log.e("Firebase", "Error fetching auctions: " + databaseError.getMessage());
            }
        });
    }
}
