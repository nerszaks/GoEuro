package com.nerszaks.goeuro.api;


import com.nerszaks.goeuro.content.Location;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by User on 11.06.2016.
 */
public interface ApiMethods {

    @GET("position/suggest/{locale}/{term}")
    Observable<List<Location>> listLocations(@Path("locale") String user, @Path("term") String filterText);

}
