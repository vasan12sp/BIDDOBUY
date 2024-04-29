package com.vasan12sp.biddobuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
// Your imports...

public class BidderRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_register);

        Button registerButton = findViewById(R.id.registerbutton);
        Button exitButton = findViewById(R.id.exitbutton);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Finish the current activity
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from EditText fields
                EditText nameEditText = findViewById(R.id.nameid);
                EditText phoneEditText = findViewById(R.id.phoneid);
                EditText emailEditText = findViewById(R.id.emailid);
                EditText addressEditText = findViewById(R.id.addressid);
                EditText pinEditText = findViewById(R.id.pincodeid);
                EditText passwordEditText = findViewById(R.id.passwordid);
                EditText confirmPasswordEditText = findViewById(R.id.confirmpassid);

                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String pincode = pinEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    Snackbar.make(findViewById(android.R.id.content), "Passwords do not match", Snackbar.LENGTH_SHORT).show();
                    return; // Exit onClick method if passwords don't match
                }

                // Check if any fields are empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(address) || TextUtils.isEmpty(pincode) || TextUtils.isEmpty(password)) {
                    Snackbar.make(findViewById(android.R.id.content), "All fields are required", Snackbar.LENGTH_SHORT).show();
                    return; // Exit onClick method if any field is empty
                }

                // Proceed with registration
                registerBidder(name, phone, email, address, pincode, password);
            }
        });
    }

    private void registerBidder(String name, String phone, String email, String address, String pincode, String password) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bidders");

        ref.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(BidderRegister.this, "Bidder with this phone number already exists", Toast.LENGTH_SHORT).show();
                } else {
                    Bidders bidder = new Bidders(name, phone, email, address, pincode, password);
                    ref.child(phone).setValue(bidder)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(BidderRegister.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(BidderRegister.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(BidderRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(findViewById(android.R.id.content), "Failed to register: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
