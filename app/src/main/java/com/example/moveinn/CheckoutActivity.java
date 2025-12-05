package com.example.moveinn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvCheckoutTitle;
    private TextView tvCheckoutPricePerDay;
    private TextView tvCheckoutQuantity;
    private TextView tvCheckoutSubtotal;

    private EditText etNameOnCard;
    private EditText etCardNumber;
    private EditText etExpiry;
    private EditText etCvv;

    private Button btnConfirm;
    private Button btnCancel;

    private List<CartItem> cartItems;
    private String userType;


    private String userEmail = "demo.user@example.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tvCheckoutTitle = findViewById(R.id.tvCheckoutTitle);
        tvCheckoutPricePerDay = findViewById(R.id.tvCheckoutPricePerDay);
        tvCheckoutQuantity = findViewById(R.id.tvCheckoutQuantity);
        tvCheckoutSubtotal = findViewById(R.id.tvCheckoutSubtotal);

        etNameOnCard = findViewById(R.id.etNameOnCard);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCvv = findViewById(R.id.etCvv);

        btnConfirm = findViewById(R.id.btnConfirmBooking);
        btnCancel = findViewById(R.id.btnCancelCheckout);

        userType = getIntent().getStringExtra("USER_TYPE");

        cartItems = CartManager.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindCartSummary();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etNameOnCard.getText().toString().trim();
                String card = etCardNumber.getText().toString().trim();
                String expiry = etExpiry.getText().toString().trim();
                String cvv = etCvv.getText().toString().trim();

                if (TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(card) ||
                        TextUtils.isEmpty(expiry) ||
                        TextUtils.isEmpty(cvv)) {

                    Toast.makeText(CheckoutActivity.this,
                            "Please fill in all payment fields.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (card.length() < 12) {
                    Toast.makeText(CheckoutActivity.this,
                            "Please enter a valid card number.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cvv.length() < 3 || cvv.length() > 4) {
                    Toast.makeText(CheckoutActivity.this,
                            "Please enter a valid CVV.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                Toast.makeText(CheckoutActivity.this,
                        "Payment successful! Booking confirmed.",
                        Toast.LENGTH_LONG).show();


                sendConfirmationEmail();


                CartManager.clear();


                Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                if (userType != null) {
                    intent.putExtra("USER_TYPE", userType);
                }
                startActivity(intent);

                finish();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void bindCartSummary() {
        int itemCount = cartItems.size();
        int totalQuantity = 0;
        for (CartItem item : cartItems) {
            totalQuantity += item.getQuantity();
        }

        double subtotal = CartManager.getSubtotal();

        if (itemCount == 1) {
            CartItem first = cartItems.get(0);
            tvCheckoutTitle.setText(first.getTitle());
            tvCheckoutPricePerDay.setText(String.format("$%.2f / day", first.getPricePerDay()));
            tvCheckoutQuantity.setText(String.valueOf(first.getQuantity()));
        } else {
            tvCheckoutTitle.setText(itemCount + " items in your booking");
            tvCheckoutPricePerDay.setText("Multiple listings");
            tvCheckoutQuantity.setText(String.valueOf(totalQuantity));
        }

        tvCheckoutSubtotal.setText(String.format("$%.2f", subtotal));
    }

    private void sendConfirmationEmail() {
        // Build email body text
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Hi,\n\n");
        messageBuilder.append("Your booking has been confirmed.\n\n");
        messageBuilder.append("Items:\n");

        for (CartItem item : cartItems) {
            messageBuilder.append("- ")
                    .append(item.getTitle())
                    .append(" | $")
                    .append(String.format("%.2f", item.getPricePerDay()))
                    .append(" / day x ")
                    .append(item.getQuantity())
                    .append(" = $")
                    .append(String.format("%.2f", item.getTotalPrice()))
                    .append("\n");
        }

        messageBuilder.append("\nTotal: $")
                .append(String.format("%.2f", CartManager.getSubtotal()))
                .append("\n\nThank you for using MoveInn!");

        String subject = "MoveInn Booking Confirmation";
        String message = messageBuilder.toString();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        // If there is an email app, open chooser; otherwise do nothing (no Toast).
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Send booking confirmation email..."));
        }
    }
}
