package com.example.moveinn;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {

    public interface OnViewListingListener {
        void onViewListing(Listing listing);
    }

    public interface OnRemoveListingListener {
        void onRemoveListing(Listing listing);
    }

    private List<Listing> items;
    private OnViewListingListener viewListener;
    private OnRemoveListingListener removeListener;

    public ListingAdapter(List<Listing> items,
                          OnViewListingListener viewListener) {
        this.items = items;
        this.viewListener = viewListener;
    }

    public ListingAdapter(List<Listing> items,
                          OnViewListingListener viewListener,
                          OnRemoveListingListener removeListener) {
        this.items = items;
        this.viewListener = viewListener;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listing_summary, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        final Listing listing = items.get(position);

        holder.txtTitle.setText(listing.getTitle());
        holder.txtRating.setText("★ " + listing.getRating());
        holder.txtReviews.setText("(" + listing.getReviews() + ")");
        holder.txtPrice.setText("$" + listing.getPrice() + " / day");

        holder.txtVerified.setText(listing.isVerified() ? "✓ Verified" : "✗ Not verified");
        holder.txtInsurance.setText(listing.hasInsurance() ? "✓ Insurance included" : "✗ No insurance");
        holder.txtAvailability.setText(listing.isAvailable() ? "✓ Available" : "✗ Unavailable");

        holder.btnViewDetails.setEnabled(listing.isAvailable());
        holder.btnViewDetails.setText(listing.isAvailable() ? "View Details" : "Unavailable");

        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewListener != null) {
                    viewListener.onViewListing(listing);
                }
            }
        });

        // Comparison Logic
        // Remove listener to avoid triggering during state change
        holder.cbCompare.setOnCheckedChangeListener(null);
        holder.cbCompare.setChecked(MockDatabase.isInComparison(listing));

        holder.cbCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.cbCompare.isChecked();
                if (isChecked) {
                    boolean success = MockDatabase.addToCompare(listing);
                    if (!success) {
                        holder.cbCompare.setChecked(false); // Revert
                        android.widget.Toast.makeText(v.getContext(), "You can only compare up to 3 listings.", android.widget.Toast.LENGTH_SHORT).show();
                    }
                } else {
                    MockDatabase.removeFromCompare(listing);
                    if (removeListener != null) {
                        removeListener.onRemoveListing(listing);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ListingViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtRating;
        TextView txtReviews;
        TextView txtPrice;
        TextView txtVerified;
        TextView txtInsurance;
        TextView txtAvailability;
        Button btnViewDetails;
        android.widget.CheckBox cbCompare;

        ListingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtReviews = itemView.findViewById(R.id.txtReviews);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtVerified = itemView.findViewById(R.id.txtVerified);
            txtInsurance = itemView.findViewById(R.id.txtInsurance);
            txtAvailability = itemView.findViewById(R.id.txtAvailability);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            cbCompare = itemView.findViewById(R.id.cbCompare);
        }
    }
}
