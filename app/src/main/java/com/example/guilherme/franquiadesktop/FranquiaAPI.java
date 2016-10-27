package com.example.guilherme.franquiadesktop;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by Guilherme on 24/10/2016.
 */

public interface FranquiaAPI {

    @GET("/franquia-desktop-service/franquia")
    Call<Franquia> getFranquia();
}
