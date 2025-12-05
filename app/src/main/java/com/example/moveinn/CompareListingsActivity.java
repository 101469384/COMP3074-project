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
import java.util.List;

public class CompareListingsActivity extends AppCompatActivity {

    private RecyclerView recyclerListings;
    private ListingAdapter adapter;
    private LinearLayout bottomBar;
    private LinearLayout emptyState;
    private LinearLayout contentState;
    private TextView btnBack;

    private List<Listing> listings;
    private String userType;
    private boolean isGuest;

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


        userType = getIntent().getStringExtra("USER_TYPE");
        isGuest = "Guest".equals(userType);

        if (isGuest) {
            Toast.makeText(
                    this,
                    "You’re browsing as a guest. You can view and compare listings, " +
                            "but you must sign up or log in to add to cart or check out.",
                    Toast.LENGTH_LONG
            ).show();
        }


        listings = new ArrayList<>(MockDatabase.getComparisonList());


        if (isGuest && listings.isEmpty()) {
            listings.add(createDemoListing(
                    "1",
                    "Downtown Apartment with Storage",
                    80.0,
                    4.7,
                    23,
                    "Toronto, ON",
                    "John Doe"
            ));
            listings.add(createDemoListing(
                    "2",
                    "Suburban Garage Space",
                    45.0,
                    4.3,
                    15,
                    "Mississauga, ON",
                    "Mary Smith"
            ));
            listings.add(createDemoListing(
                    "3",
                    "Basement Storage Unit",
                    55.0,
                    4.5,
                    10,
                    "North York, ON",
                    "Alex Johnson"
            ));
        }


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
                        intent.putExtra("USER_TYPE", userType);
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

        // Back arrow in the header
        btnBack.setOnClickListener(v -> finish());

        // Browse buttons
        Button btnAddMore = findViewById(R.id.btnAddMore);
        Button btnBrowseEmpty = findViewById(R.id.btnBrowseEmpty);

        View.OnClickListener browseListener = v -> {
            // Always go back to HomeActivity, keeping user type (Guest, Student, etc.)
            Intent intent = new Intent(CompareListingsActivity.this, HomeActivity.class);
            intent.putExtra("USER_TYPE", userType);
            startActivity(intent);
            finish();
        };

        btnAddMore.setOnClickListener(browseListener);
        btnBrowseEmpty.setOnClickListener(browseListener);

        checkEmptyState();
    }


    private Listing createDemoListing(String id,
                                      String title,
                                      double price,
                                      double rating,
                                      int reviews,
                                      String location,
                                      String vendor) {

        return new Listing(
                id,
                title,
                null,
                price,
                rating,
                reviews,
                true,
                true,
                location,
                vendor,
                true
        );
    }

    private void checkEmptyState() {
        if (listings == null || listings.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            contentState.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            contentState.setVisibility(View.VISIBLE);


            if (isGuest) {
                bottomBar.setVisibility(View.GONE);
            } else {
                bottomBar.setVisibility(View.VISIBLE);
            }
        }
    }
}
