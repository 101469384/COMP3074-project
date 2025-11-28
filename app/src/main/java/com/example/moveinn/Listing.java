package com.example.moveinn;

public class Listing {
    private String id;
    private String title;
    private String imageUrl;
    private double price;
    private double rating;
    private int reviews;
    private boolean available;
    private boolean insurance;
    private String location;
    private String vendor;
    private boolean verified;

    public Listing(String id,
                   String title,
                   String imageUrl,
                   double price,
                   double rating,
                   int reviews,
                   boolean available,
                   boolean insurance,
                   String location,
                   String vendor,
                   boolean verified) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
        this.reviews = reviews;
        this.available = available;
        this.insurance = insurance;
        this.location = location;
        this.vendor = vendor;
        this.verified = verified;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public int getReviews() {
        return reviews;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean hasInsurance() {
        return insurance;
    }

    public String getLocation() {
        return location;
    }

    public String getVendor() {
        return vendor;
    }

    public boolean isVerified() {
        return verified;
    }
}

