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
    private Button btnAddMore;
    private Button btnBrowseEmpty;
    private TextView btnBack;   // 👈 back arrow in header

    private ArrayList<Listing> listings = new ArrayList<>();
    private int nextId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_listings);

        recyclerListings = findViewById(R.id.recyclerListings);
        bottomBar = findViewById(R.id.bottomBar);
        emptyState = findViewById(R.id.emptyState);
        contentState = findViewById(R.id.contentState);
        btnAddMore = findViewById(R.id.btnAddMore);
        btnBrowseEmpty = findViewById(R.id.btnBrowseEmpty);
        btnBack = findViewById(R.id.btnBack);

        recyclerListings.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Back arrow: just close this activity and return to previous screen
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // goes back to MainActivity / HomeActivity, wherever you came from
            }
        });

        // Sample listings
        listings.add(new Listing(
                "1",
                "Downtown Apartment with Storage",
                null,
                80.0,
                4.7,
                23,
                true,
                true,
                "Toronto, ON",
                "John Doe",
                true
        ));

        listings.add(new Listing(
                "2",
                "Garage Space Near Campus",
                null,
                50.0,
                4.2,
                10,
                false,
                false,
                "Scarborough, ON",
                "Mary Smith",
                false
        ));

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

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listings.size() >= 3) {
                    Toast.makeText(
                            CompareListingsActivity.this,
                            "You can only compare up to 3 listings.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                int displayNumber = listings.size() + 1;
                Listing newListing = new Listing(
                        String.valueOf(nextId++),
                        "Extra Listing " + displayNumber,
                        null,
                        70.0,
                        4.5,
                        0,
                        true,
                        false,
                        "Toronto, ON",
                        "New Vendor",
                        true
                );

                listings.add(newListing);
                adapter.notifyItemInserted(listings.size() - 1);
                checkEmptyState();
            }
        });

        btnBrowseEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listing newListing = new Listing(
                        String.valueOf(nextId++),
                        "New Listing",
                        null,
                        60.0,
                        4.0,
                        0,
                        true,
                        false,
                        "Toronto, ON",
                        "New Vendor",
                        true
                );
                listings.add(newListing);
                adapter.notifyDataSetChanged();
                checkEmptyState();
            }
        });

        checkEmptyState();
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
