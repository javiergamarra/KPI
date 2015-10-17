package com.nhpatt.kpi.service;

import com.nhpatt.kpi.XML;

import retrofit.Call;
import retrofit.http.GET;

/**
 * @author Javier Gamarra
 */
public interface ShowsService {

    @GET("rss.php?user_id=158034&hd=null&proper=null")
    Call<XML> showToWatch();

}
