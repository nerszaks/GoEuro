package com.nerszaks.goeuro.content;

/**
 * Created by User on 11.06.2016.
 */
public class GeoPosition {

    public double latitude;
    public double longitude;

    public android.location.Location getLocationObj() {
        android.location.Location location = new android.location.Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }


}
