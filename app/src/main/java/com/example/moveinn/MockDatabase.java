package com.example.moveinn;

import java.util.ArrayList;
import java.util.List;

public class MockDatabase {

    private static List<User> users = new ArrayList<>();
    private static List<Listing> listings = new ArrayList<>();

    static {
        // Initialize Dummy Users
        users.add(new User("John Customer", "customer@test.com", "123456", "I need moving services"));
        users.add(new User("Jane Vendor", "vendor@test.com", "123456", "I am truck owner/ vendor"));

        // Initialize Dummy Listings
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
        
        listings.add(new Listing(
                "3",
                "Climate Controlled Unit",
                null,
                120.0,
                4.9,
                50,
                true,
                true,
                "Mississauga, ON",
                "SecureStorage Inc.",
                true
        ));

        listings.add(new Listing(
                "4",
                "15ft Moving Truck",
                null,
                45.0,
                4.0,
                15,
                true,
                true,
                "Brampton, ON",
                "U-Move Rentals",
                true
        ));

        listings.add(new Listing(
                "5",
                "Small Cargo Van",
                null,
                29.99,
                4.3,
                8,
                true,
                false,
                "Toronto, ON",
                "City Vans",
                false
        ));

        listings.add(new Listing(
                "6",
                "Large Warehouse Space",
                null,
                200.0,
                4.8,
                32,
                true,
                true,
                "Vaughan, ON",
                "Industrial Storage Co.",
                true
        ));

        listings.add(new Listing(
                "7",
                "Pickup Truck with Driver",
                null,
                60.0,
                4.6,
                21,
                false,
                true,
                "Etobicoke, ON",
                "Mike's Moving",
                true
        ));

        listings.add(new Listing(
                "8",
                "Basement Storage",
                null,
                25.0,
                3.5,
                3,
                true,
                false,
                "North York, ON",
                "Private Homeowner",
                false
        ));
    }

    private static List<Listing> comparisonList = new ArrayList<>();

    public static List<User> getAllUsers() {
        return users;
    }

    public static List<Listing> getListings() {
        return listings;
    }
    
    public static void addListing(Listing listing) {
        listings.add(listing);
    }

    // Comparison Logic
    public static List<Listing> getComparisonList() {
        return comparisonList;
    }

    public static boolean addToCompare(Listing listing) {
        if (comparisonList.size() >= 3) {
            return false;
        }
        if (!comparisonList.contains(listing)) {
            comparisonList.add(listing);
        }
        return true;
    }

    public static void removeFromCompare(Listing listing) {
        comparisonList.remove(listing);
    }

    public static boolean isInComparison(Listing listing) {
        return comparisonList.contains(listing);
    }
}
