package com.example.moveinn;

public class User {
    private String name;
    private String email;
    private String password;
    private String accountType; // "Customer" or "Vendor"

    public User(String name, String email, String password, String accountType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }
}
