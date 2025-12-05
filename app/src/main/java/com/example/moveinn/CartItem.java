package com.example.moveinn;

public class CartItem {

    private String title;
    private double pricePerDay;
    private int quantity;

    public CartItem(String title, double pricePerDay, int quantity) {
        this.title = title;
        this.pricePerDay = pricePerDay;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increment() {
        quantity++;
    }

    public void decrement() {
        if (quantity > 1) {
            quantity--;
        }
    }

    public double getTotalPrice() {
        return pricePerDay * quantity;
    }
}


