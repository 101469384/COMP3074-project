package com.example.moveinn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        TextView txtTitle = findViewById(R.id.txtDetailTitle);
        TextView txtPrice = findViewById(R.id.txtDetailPrice);
        TextView txtRating = findViewById(R.id.txtDetailRating);
        TextView txtAvailability = findViewById(R.id.txtDetailAvailability);
        TextView txtInsurance = findViewById(R.id.txtDetailInsurance);
        TextView txtVerified = findViewById(R.id.txtDetailVerified);
        TextView txtLocation = findViewById(R.id.txtDetailLocation);
        TextView txtVendor = findViewById(R.id.txtDetailVendor);
        Button btnClose = findViewById(R.id.btnCloseDetails);

        // Get data from Intent
        String title = getIntent().getStringExtra("title");
        double price = getIntent().getDoubleExtra("price", 0);
        double rating = getIntent().getDoubleExtra("rating", 0);
        int reviews = getIntent().getIntExtra("reviews", 0);
        boolean available = getIntent().getBooleanExtra("available", false);
        boolean insurance = getIntent().getBooleanExtra("insurance", false);
        boolean verified = getIntent().getBooleanExtra("verified", false);
        String location = getIntent().getStringExtra("location");
        String vendor = getIntent().getStringExtra("vendor");

        // Set values
        txtTitle.setText(title);
        txtPrice.setText("$" + price + " / day");
        txtRating.setText("Rating: " + rating + " (" + reviews + " reviews)");
        txtAvailability.setText(available ? "Available" : "Unavailable");
        txtInsurance.setText(insurance ? "Insurance included" : "No insurance");
        txtVerified.setText(verified ? "Verified" : "Not verified");
        txtLocation.setText("Location: " + location);
        txtVendor.setText("Vendor: " + vendor);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // close this screen
            }
        });
    }
}

