package com.example.moveinn;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class CreateListingActivity extends AppCompatActivity {

    private EditText etTitle, etPrice, etLocation;
    private Button btnSaveListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        etTitle = findViewById(R.id.etTitle);
        etPrice = findViewById(R.id.etPrice);
        etLocation = findViewById(R.id.etLocation);
        btnSaveListing = findViewById(R.id.btnSaveListing);

        btnSaveListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveListing();
            }
        });
    }

    private void saveListing() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new Listing object
        // For simplicity, we'll set some default values for fields not in the form (rating, reviews, etc.)
        Listing newListing = new Listing(
                UUID.randomUUID().toString(), // Unique ID
                title,
                null, // No image for now
                price,
                0.0, // New listing has 0 rating
                0,   // 0 reviews
                true, // Available by default
                true, // Insurance included (mock default)
                location,
                "Current Vendor", // In a real app, we'd get the logged-in vendor's name
                false // Not verified yet
        );

        MockDatabase.addListing(newListing);

        Toast.makeText(this, "Listing Created Successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Close activity and return to Home
    }
}
