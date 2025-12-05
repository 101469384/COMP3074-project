package com.example.moveinn;

import java.util.ArrayList;
import java.util.List;

public class CartManager {


    private static final List<CartItem> cartItems = new ArrayList<>();


    public static synchronized void addOrIncrement(String title, double pricePerDay) {
        for (CartItem item : cartItems) {
            if (item.getTitle().equals(title)
                    && item.getPricePerDay() == pricePerDay) {

                item.increment();
                return;
            }
        }

        cartItems.add(new CartItem(title, pricePerDay, 1));
    }


    public static synchronized void addItem(CartItem newItem) {
        if (newItem == null) return;

        for (CartItem existing : cartItems) {
            if (existing.getTitle().equals(newItem.getTitle())
                    && existing.getPricePerDay() == newItem.getPricePerDay()) {


                int times = Math.max(1, newItem.getQuantity());
                for (int i = 0; i < times; i++) {
                    existing.increment();
                }
                return;
            }
        }

        cartItems.add(newItem);
    }


    public static synchronized void removeItem(CartItem item) {
        cartItems.remove(item);
    }


    public static synchronized void clear() {
        cartItems.clear();
    }


    public static synchronized List<CartItem> getItems() {
        return new ArrayList<>(cartItems);
    }


    public static synchronized CartItem getCartItem() {
        if (cartItems.isEmpty()) return null;
        return cartItems.get(0);
    }


    public static synchronized double getSubtotal() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
