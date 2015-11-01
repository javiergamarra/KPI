package com.nhpatt.kpi.service;

import com.nhpatt.kpi.models.Commit;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface GitHubService {

    @GET("/repos/{user}/{repo}/stats/commit_activity")
    Call<List<Commit>> commitsPerWeek(@Path("user") String user, @Path("repo") String repo);
}