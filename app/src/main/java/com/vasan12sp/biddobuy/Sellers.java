package com.vasan12sp.biddobuy;

public class Sellers {

    String name, phone, email, address, pincode, password;

    public Sellers() {
    }

    public Sellers(String name, String phone, String email, String address, String pincode, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.pincode = pincode;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
