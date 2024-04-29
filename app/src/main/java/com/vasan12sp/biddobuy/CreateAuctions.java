package com.vasan12sp.biddobuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateAuctions extends AppCompatActivity {

    private String selectedItem;
    String description, category;
    Float basePrice;

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateAuctions.this, android.R.layout.simple_spinner_item, productsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedItem = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(CreateAuctions.this, "Select any product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Spinner spinner;
    private List<String> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_auctions);

        DatabaseReference auctionsRef = FirebaseDatabase.getInstance().getReference("seller_auction");
        String auctionId = auctionsRef.push().getKey();

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("seller_products");

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");

        Button create = findViewById(R.id.createauction);
        Button cancel = findViewById(R.id.cancelauction);

        spinner = findViewById(R.id.spinner);
        productsList = new ArrayList<>();

        DatabaseReference sellerProductsRef = FirebaseDatabase.getInstance().getReference("seller_products").child(phone);

        sellerProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String productName = productSnapshot.child("name").getValue(String.class);
                    if (productName != null) {
                        productsList.add(productName);
                    }
                }
                setupSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CreateAuctions.this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle create button click
                EditText inc = findViewById(R.id.increment);
                Float increment = Float.parseFloat(inc.getText().toString());

                EditText date1, date2, time1, time2;
                date1 = findViewById(R.id.startDate);
                date2 = findViewById(R.id.endDate);
                time1 = findViewById(R.id.startTime);
                time2 = findViewById(R.id.endTime);

                String startDate, endDate, startTime, endTime;
                startDate = date1.getText().toString();
                endDate = date2.getText().toString();
                startTime = time1.getText().toString();
                endTime = time2.getText().toString();

                if (selectedItem != null) {
                    Query query = productsRef.child(phone).orderByChild("name").equalTo(selectedItem);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                description = snapshot.child("description").getValue(String.class);
                                basePrice = snapshot.child("basePrice").getValue(Float.class);
                                category = snapshot.child("category").getValue(String.class);

                                Auction auction = new Auction(phone, selectedItem, description, category, startDate, startTime, endDate, endTime, increment, basePrice);

                                DatabaseReference sellerAuctionsRef = FirebaseDatabase.getInstance().getReference("seller_auctions").child(phone).child(category);
                                sellerAuctionsRef.child(auctionId).setValue(auction).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Auction created successfully
                                            Toast.makeText(CreateAuctions.this, "Auction created", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(CreateAuctions.this, SellerLogged.class);
//                                            intent.putExtra("phone", phone);
//                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Failed to create auction
                                            Toast.makeText(CreateAuctions.this, "Failed to create auction", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(CreateAuctions.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateAuctions.this, "Please select a product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAuctions.this, SellerLogged.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        });
    }
}
