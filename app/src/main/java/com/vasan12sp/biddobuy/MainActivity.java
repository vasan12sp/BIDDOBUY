package com.vasan12sp.biddobuy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bidderLogin, bidderRegister, sellerLogin, sellerRegister, exitButton;

        bidderLogin = findViewById(R.id.bidderlogin);
        bidderRegister = findViewById(R.id.bidderregister);
        sellerLogin = findViewById(R.id.sellerlogin);
        sellerRegister = findViewById(R.id.sellerregister);
        exitButton = findViewById(R.id.exitbutton);

        bidderLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start BidderLogin activity
                startActivity(new Intent(MainActivity.this, BidderLogin.class));
            }
        });

        bidderRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start BidderRegister activity
                startActivity(new Intent(MainActivity.this, BidderRegister.class));
            }
        });

        sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start SellerLogin activity
                startActivity(new Intent(MainActivity.this, SellerLogin.class));
            }
        });

        sellerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start SellerRegister activity
                startActivity(new Intent(MainActivity.this, SellerRegister.class));
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.exit(0);
            }
        });
    }

}
