package com.br.guilhermelp.franquiadesktop;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Guilherme on 24/10/2016.
 */

public interface FranquiaAPI {

    @GET("/franquia-desktop-service/franquia")
    Call<Franquia> getFranquia();
}
