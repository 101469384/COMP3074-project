package com.example.moveinn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private TextView tvListingTitle;
    private TextView tvPricePerDay;
    private TextView tvQuantity;
    private TextView tvItemTotal;
    private TextView tvSubtotal;
    private TextView tvCartSubtitle;
    private TextView tvCartItemsList;   // shows all items in cart
    private TextView btnBackCart;

    private View layoutCartItem;
    private Button btnCheckout;
    private ImageButton btnRemove;
    private TextView btnPlus;
    private TextView btnMinus;

    private List<CartItem> cartItems;
    private CartItem currentItem;

    private String userType;
    private boolean isGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvListingTitle = findViewById(R.id.tvListingTitle);
        tvPricePerDay = findViewById(R.id.tvPricePerDay);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvItemTotal = findViewById(R.id.tvItemTotal);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvCartSubtitle = findViewById(R.id.tvCartSubtitle);
        tvCartItemsList = findViewById(R.id.tvCartItemsList);
        layoutCartItem = findViewById(R.id.layoutCartItem);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnRemove = findViewById(R.id.btnRemove);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnBackCart = findViewById(R.id.btnBackCart);


        btnBackCart.setOnClickListener(v -> finish());


        userType = getIntent().getStringExtra("USER_TYPE");
        isGuest = (userType == null) || userType.equalsIgnoreCase("guest");

        if (isGuest) {
            Toast.makeText(
                    this,
                    "Please log in or sign up to use the cart and checkout.",
                    Toast.LENGTH_LONG
            ).show();

            Intent loginIntent = new Intent(CartActivity.this, AuthActivity.class);
            loginIntent.putExtra("FROM_CART", true);
            startActivity(loginIntent);

            finish();
            return;
        }

        // Logged-in user: load all items in cart
        cartItems = CartManager.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            layoutCartItem.setVisibility(View.GONE);
            btnCheckout.setEnabled(false);
            tvSubtotal.setText("$0.00");
            if (tvCartSubtitle != null) {
                tvCartSubtitle.setText("Your cart is empty.");
            }
            if (tvCartItemsList != null) {
                tvCartItemsList.setText("");
            }
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
        } else {
            // e.g. "You have 2 items in your cart."
            if (tvCartSubtitle != null) {
                int count = cartItems.size();
                tvCartSubtitle.setText(
                        "You have " + count + " item" + (count > 1 ? "s" : "") + " in your cart."
                );
            }


            updateItemsListText();


            currentItem = cartItems.get(0);
            bindCurrentItem();
        }

        // Increase quantity (for current item)
        btnPlus.setOnClickListener(v -> {
            if (currentItem != null) {
                currentItem.increment();
                updateTotals();
                updateItemsListText();
            }
        });

        // Decrease quantity (for current item)
        btnMinus.setOnClickListener(v -> {
            if (currentItem != null) {
                currentItem.decrement();
                updateTotals();
                updateItemsListText();
            }
        });

        // Remove current item from cart
        btnRemove.setOnClickListener(v -> {
            if (currentItem != null) {
                CartManager.removeItem(currentItem);
                cartItems = CartManager.getItems();

                if (cartItems.isEmpty()) {
                    currentItem = null;
                    layoutCartItem.setVisibility(View.GONE);
                    btnCheckout.setEnabled(false);
                    tvSubtotal.setText("$0.00");
                    if (tvCartSubtitle != null) {
                        tvCartSubtitle.setText("Your cart is empty.");
                    }
                    if (tvCartItemsList != null) {
                        tvCartItemsList.setText("");
                    }
                    Toast.makeText(this,
                            "Item removed. Cart is now empty.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    currentItem = cartItems.get(0);
                    bindCurrentItem();
                    updateItemsListText();
                    Toast.makeText(this,
                            "Item removed from cart",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Proceed to Checkout (all items)
        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("USER_TYPE", userType);
                startActivity(intent);
            }
        });
    }

    private void bindCurrentItem() {
        if (currentItem == null) return;

        layoutCartItem.setVisibility(View.VISIBLE);
        btnCheckout.setEnabled(true);

        tvListingTitle.setText(currentItem.getTitle());
        tvPricePerDay.setText(
                String.format("$%.2f / day", currentItem.getPricePerDay())
        );
        updateTotals();
    }

    private void updateTotals() {
        if (currentItem == null) {
            tvQuantity.setText("0");
            tvItemTotal.setText("$0.00");
            tvSubtotal.setText("$0.00");
            return;
        }

        tvQuantity.setText(String.valueOf(currentItem.getQuantity()));
        tvItemTotal.setText(String.format("$%.2f", currentItem.getTotalPrice()));
        tvSubtotal.setText(String.format("$%.2f", CartManager.getSubtotal()));
    }


    private void updateItemsListText() {
        if (tvCartItemsList == null) return;

        if (cartItems == null || cartItems.isEmpty()) {
            tvCartItemsList.setText("");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (CartItem item : cartItems) {
            sb.append("• ")
                    .append(item.getTitle())
                    .append("  (x")
                    .append(item.getQuantity())
                    .append(")\n");
        }

        tvCartItemsList.setText(sb.toString().trim());
    }
}
