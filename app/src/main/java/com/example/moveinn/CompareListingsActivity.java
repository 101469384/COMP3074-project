package com.example.moveinn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CompareListingsActivity extends AppCompatActivity {

    private RecyclerView recyclerListings;
    private ListingAdapter adapter;
    private LinearLayout bottomBar;
    private LinearLayout emptyState;
    private LinearLayout contentState;
    private TextView btnBack;   // 👈 back arrow in header

    private java.util.List<Listing> listings;
    private int nextId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_listings);

        recyclerListings = findViewById(R.id.recyclerListings);
        bottomBar = findViewById(R.id.bottomBar);
        emptyState = findViewById(R.id.emptyState);
        contentState = findViewById(R.id.contentState);
        btnBack = findViewById(R.id.btnBack);

        recyclerListings.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Back arrow: just close this activity and return to previous screen
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // goes back to MainActivity / HomeActivity, wherever you came from
            }
        });

        // Use listings from MockDatabase Comparison List
        listings = MockDatabase.getComparisonList();

        adapter = new ListingAdapter(
                listings,
                new ListingAdapter.OnViewListingListener() {
                    @Override
                    public void onViewListing(Listing listing) {
                        Intent intent = new Intent(CompareListingsActivity.this, ListingDetailsActivity.class);
                        intent.putExtra("title", listing.getTitle());
                        intent.putExtra("price", listing.getPrice());
                        intent.putExtra("rating", listing.getRating());
                        intent.putExtra("reviews", listing.getReviews());
                        intent.putExtra("available", listing.isAvailable());
                        intent.putExtra("insurance", listing.hasInsurance());
                        intent.putExtra("verified", listing.isVerified());
                        intent.putExtra("location", listing.getLocation());
                        intent.putExtra("vendor", listing.getVendor());
                        intent.putExtra("USER_TYPE", getIntent().getStringExtra("USER_TYPE"));
                        startActivity(intent);
                    }
                },
                new ListingAdapter.OnRemoveListingListener() {
                    @Override
                    public void onRemoveListing(Listing listing) {
                        listings.remove(listing);
                        adapter.notifyDataSetChanged();
                        checkEmptyState();
                    }
                }
        );

        recyclerListings.setAdapter(adapter);

        checkEmptyState();

        checkEmptyState();

        Button btnAddMore = findViewById(R.id.btnAddMore);
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to Home to add more
            }
        });

        Button btnBrowseEmpty = findViewById(R.id.btnBrowseEmpty);
        btnBrowseEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to Home
            }
        });
    }

    private void checkEmptyState() {
        if (listings.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            contentState.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            contentState.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
        }
    }
}
