package com.vasan12sp.biddobuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProducts extends AppCompatActivity {

    String productname, productdescription, productcategory;
    Float basePrice;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("seller_products");
        String productId = productsRef.push().getKey(); // This generates a unique ID


        Intent intent = getIntent();

        // Get the extra data ("phone") from the Intent
        String phone = intent.getStringExtra("phone");



        Button add,cancel;
        add=findViewById(R.id.addproductbtn);
        cancel=findViewById(R.id.cancelbtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProducts.this, SellerLogged.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pn, pd, pc, bp;
                pn = findViewById(R.id.productName);
                pc = findViewById(R.id.productCategory);
                pd = findViewById(R.id.productDesc);
                bp = findViewById(R.id.basePrice);

                productname = pn.getText().toString();
                productdescription = pd.getText().toString();
                productcategory = pc.getText().toString();
                basePrice = Float.parseFloat(bp.getText().toString());

                Products product = new Products(productname, productdescription, phone, productcategory, basePrice, productId);
                DatabaseReference sellerProductsRef = FirebaseDatabase.getInstance().getReference("seller_products").child(phone);
                sellerProductsRef.child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Product added successfully
                            Toast.makeText(AddProducts.this, "Product added successfully", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(AddProducts.this, SellerLogged.class);
//                            intent.putExtra("phone", phone);
//                            startActivity(intent);
                            finish();
                        } else {
                            // Product addition failed
                            Toast.makeText(AddProducts.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });







    }
}