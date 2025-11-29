package com.example.moveinn;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        android.widget.TextView tvWelcome = findViewById(R.id.tvWelcome);
        android.widget.Button btnCreateListing = findViewById(R.id.btnCreateListing);
        android.widget.Button btnGoToCompare = findViewById(R.id.btnGoToCompare);
        androidx.recyclerview.widget.RecyclerView recyclerHomeListings = findViewById(R.id.recyclerHomeListings);

        String userName = getIntent().getStringExtra("USER_NAME");
        String userType = getIntent().getStringExtra("USER_TYPE");

        if (userName != null) {
            tvWelcome.setText("Welcome, " + userName + "\n(" + userType + ")");
        }

        // Show "Create Listing" only for Vendors
        if (userType != null && userType.toLowerCase().contains("vendor")) {
            btnCreateListing.setVisibility(android.view.View.VISIBLE);
        } else {
            btnCreateListing.setVisibility(android.view.View.GONE);
        }

        // Setup RecyclerView
        recyclerHomeListings.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        ListingAdapter adapter = new ListingAdapter(
                MockDatabase.getListings(),
                new ListingAdapter.OnViewListingListener() {
                    @Override
                    public void onViewListing(Listing listing) {
                        android.content.Intent intent = new android.content.Intent(HomeActivity.this, ListingDetailsActivity.class);
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
                }
        );
        recyclerHomeListings.setAdapter(adapter);

        // Update Compare Button Text initially
        btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");

        // Add a data observer to update the button text when checkboxes change
        adapter.registerAdapterDataObserver(new androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");
            }
            // In a real app with CheckBoxes, we might need a custom listener from Adapter to Activity
            // For now, let's just update it when we click the button or resume
        });
        
        // HACK: Since Adapter doesn't notify Activity of checkbox changes directly in this simple setup,
        // we'll just update the text whenever the user interacts with the list (scrolling) or clicks the button.
        // A better way is to pass a listener to the adapter.
        
        // Re-instantiate adapter with a listener wrapper to update button
        adapter = new ListingAdapter(
                MockDatabase.getListings(),
                new ListingAdapter.OnViewListingListener() {
                    @Override
                    public void onViewListing(Listing listing) {
                         android.content.Intent intent = new android.content.Intent(HomeActivity.this, ListingDetailsActivity.class);
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
                }
        );
        recyclerHomeListings.setAdapter(adapter);


        btnGoToCompare.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // Refresh text just in case
                btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");
                
                if (MockDatabase.getComparisonList().isEmpty()) {
                    android.widget.Toast.makeText(HomeActivity.this, "Select at least 1 listing to compare", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                android.content.Intent intent = new android.content.Intent(HomeActivity.this, CompareListingsActivity.class);
                intent.putExtra("USER_TYPE", userType);
                startActivity(intent);
            }
        });

        btnCreateListing.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                android.content.Intent intent = new android.content.Intent(HomeActivity.this, CreateListingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list and button text when returning (e.g. after creating a listing)
        androidx.recyclerview.widget.RecyclerView recyclerHomeListings = findViewById(R.id.recyclerHomeListings);
        if (recyclerHomeListings.getAdapter() != null) {
            recyclerHomeListings.getAdapter().notifyDataSetChanged();
        }
        android.widget.Button btnGoToCompare = findViewById(R.id.btnGoToCompare);
        if (btnGoToCompare != null) {
             btnGoToCompare.setText("Go to Compare (" + MockDatabase.getComparisonList().size() + "/3)");
        }
    }
}
