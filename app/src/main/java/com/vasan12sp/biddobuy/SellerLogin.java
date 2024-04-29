package com.vasan12sp.biddobuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        Button loginButton, cancelButton;
        loginButton = findViewById(R.id.login);
        cancelButton = findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Finish the current activity
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phoneEditText = findViewById(R.id.phone);
                EditText passwordEditText = findViewById(R.id.password);

                String phone = phoneEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (phone.isEmpty() || password.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Phone and password should not be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(phone);
                sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Seller exists, check password
                            String storedPassword = dataSnapshot.child("password").getValue(String.class);
                            if (password.equals(storedPassword)) {
                                // Password matches, login successful
                                Intent intent = new Intent(SellerLogin.this, SellerLogged.class);
                                intent.putExtra("phone", phone);
                                Toast.makeText(SellerLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            } else {
                                // Incorrect password
                                Snackbar.make(findViewById(android.R.id.content), "Incorrect password", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            // Seller does not exist
                            Snackbar.make(findViewById(android.R.id.content), "Seller does not exist, kindly register", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content), "Failed to login: " + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
