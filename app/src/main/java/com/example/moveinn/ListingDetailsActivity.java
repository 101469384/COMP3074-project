package com.example.moveinn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


        TextView btnBack = findViewById(R.id.btnCloseDetails);

        Button btnBook = findViewById(R.id.btnBook);


        String title = getIntent().getStringExtra("title");
        double price = getIntent().getDoubleExtra("price", 0);
        double rating = getIntent().getDoubleExtra("rating", 0);
        int reviews = getIntent().getIntExtra("reviews", 0);
        boolean available = getIntent().getBooleanExtra("available", false);
        boolean insurance = getIntent().getBooleanExtra("insurance", false);
        boolean verified = getIntent().getBooleanExtra("verified", false);
        String location = getIntent().getStringExtra("location");
        String vendor = getIntent().getStringExtra("vendor");
        String userType = getIntent().getStringExtra("USER_TYPE");

        final boolean isGuest = (userType == null) || userType.equalsIgnoreCase("guest");


        txtTitle.setText(title);
        txtPrice.setText("$" + price + " / day");
        txtRating.setText("Rating: " + rating + " (" + reviews + " reviews)");
        txtAvailability.setText(available ? "Available" : "Unavailable");
        txtInsurance.setText(insurance ? "Insurance included" : "No insurance");
        txtVerified.setText(verified ? "Verified" : "Not verified");
        txtLocation.setText("Location: " + location);
        txtVendor.setText("Vendor: " + vendor);

        if (isGuest) {
            btnBook.setText("Login to book");
            btnBook.setAlpha(0.7f);
        }


        btnBook.setOnClickListener(v -> {
            if (isGuest) {
                // Guest → send to login
                Toast.makeText(
                        ListingDetailsActivity.this,
                        "Please login to book this listing.",
                        Toast.LENGTH_SHORT
                ).show();

                Intent loginIntent = new Intent(ListingDetailsActivity.this, AuthActivity.class);
                loginIntent.putExtra("FROM_BOOKING", true);
                startActivity(loginIntent);
                return;
            }


            CartItem item = new CartItem(title, price, 1);
            CartManager.addItem(item);

            Toast.makeText(
                    ListingDetailsActivity.this,
                    "Added to cart",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(ListingDetailsActivity.this, CartActivity.class);
            intent.putExtra("USER_TYPE", userType);
            startActivity(intent);
        });


        btnBack.setOnClickListener((View v) -> finish());
    }
}
