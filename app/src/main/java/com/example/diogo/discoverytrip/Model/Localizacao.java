package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 07/02/17.
 */
public class Localizacao {

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("city")
    private String city;

    @SerializedName("country")
    private String country;

    @SerializedName("countryCode")
    private String countryCode;

    @SerializedName("streetName")
    private String streetName;

    @SerializedName("streetNumber")
    private String streetNumber;

    @SerializedName("zipcode")
    private String zipCode;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
