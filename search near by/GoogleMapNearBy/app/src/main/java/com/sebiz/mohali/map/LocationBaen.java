package com.sebiz.mohali.map;

import java.io.Serializable;

/**
 * Created by Pankaj on 9/3/2015.
 */
public class LocationBaen implements Serializable {
    private String place_id;
    private String formatted_address;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
}
