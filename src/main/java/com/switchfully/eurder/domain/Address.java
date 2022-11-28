package com.switchfully.eurder.domain;

public class Address {

    private final String streetName;
    private final String houseNumber;

    private final String postalCode;

    private final String cityName;
    public Address(String streetName, String houseNumber, String postalCode, String cityName) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.cityName = cityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCityName() {
        return cityName;
    }
}
