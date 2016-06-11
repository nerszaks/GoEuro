package com.nerszaks.goeuro.content;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11.06.2016.
 */
public class Location {

    @SerializedName("_id")
    public long id;

    public String name;
    public String country;

    @SerializedName("geo_position")
    public GeoPosition geoPosition;

    public float distance;

    @Override
    public String toString() {
        return name;
    }

}
