package com.example.moveinn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private TextView tvLogout;
    private Button btnCreateListing;
    private Button btnGoToCompare;
    private Button btnViewCart;
    private RecyclerView recyclerHomeListings;

    private String userName;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvLogout = findViewById(R.id.tvLogout);
        btnCreateListing = findViewById(R.id.btnCreateListing);
        btnGoToCompare = findViewById(R.id.btnGoToCompare);
        btnViewCart = findViewById(R.id.btnViewCart);
        recyclerHomeListings = findViewById(R.id.recyclerHomeListings);

        userName = getIntent().getStringExtra("USER_NAME");
        userType = getIntent().getStringExtra("USER_TYPE");


        if (userName != null && userType != null) {
            tvWelcome.setText("Welcome, " + userName + "\n(" + userType + ")");
        } else if (userName != null) {
            tvWelcome.setText("Welcome, " + userName);
        }


        if (userType != null && userType.toLowerCase().contains("vendor")) {
            btnCreateListing.setVisibility(View.VISIBLE);
        } else {
            btnCreateListing.setVisibility(View.GONE);
        }


        recyclerHomeListings.setLayoutManager(new LinearLayoutManager(this));

        ListingAdapter adapter = new ListingAdapter(
                MockDatabase.getListings(),
                listing -> {
                    Intent intent = new Intent(HomeActivity.this, ListingDetailsActivity.class);
                    intent.putExtra("title", listing.getTitle());
                    intent.putExtra("price", listing.getPrice());
                    intent.putExtra("rating", listing.getRating());
                    intent.putExtra("reviews", listing.getReviews());
                    intent.putExtra("available", listing.isAvailable());
                    intent.putExtra("insurance", listing.hasInsurance());
                    intent.putExtra("verified", listing.isVerified());
                    intent.putExtra("location", listing.getLocation());
                    intent.putExtra("vendor", listing.getVendor());
                    intent.putExtra("USER_TYPE", userType);
                    startActivity(intent);
                }
        );
        recyclerHomeListings.setAdapter(adapter);


        btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");
            }
        });


        btnGoToCompare.setOnClickListener(v -> {
            btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");

            if (MockDatabase.getComparisonList().isEmpty()) {
                Toast.makeText(HomeActivity.this,
                        "Select at least 1 listing to compare",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(HomeActivity.this, CompareListingsActivity.class);
            intent.putExtra("USER_TYPE", userType);
            startActivity(intent);
        });


        btnViewCart.setOnClickListener(v -> {
            Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
            cartIntent.putExtra("USER_TYPE", userType);
            startActivity(cartIntent);
        });


        btnCreateListing.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateListingActivity.class);
            intent.putExtra("USER_TYPE", userType);
            startActivity(intent);
        });


        tvLogout.setOnClickListener(v -> {

            CartManager.clear();


            Intent logoutIntent = new Intent(HomeActivity.this, AuthActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logoutIntent);

            Toast.makeText(HomeActivity.this,
                    "You have been logged out.",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (recyclerHomeListings.getAdapter() != null) {
            recyclerHomeListings.getAdapter().notifyDataSetChanged();
        }


        if (btnGoToCompare != null) {
            btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");
        }
    }
}
