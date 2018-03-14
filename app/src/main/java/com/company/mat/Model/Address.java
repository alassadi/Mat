package com.company.mat.Model;

import java.io.Serializable;

/**
 * Created by ivana on 3/12/2018.
 * This class is to store address.
 * It should help to sort and search by address.
 */

public class Address implements Serializable {
    private String street, city, region, postCode;

    /**
     * Mandatory empty constructor
     */
    public Address() {
    }

    public Address(String street, String city, String region, String postCode) {
        this.street = street;
        this.city = city;
        this.region = region;
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getRegion() {
        return region;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return street + "\n" + postCode + " " + city + "\n" + region;
    }
}

