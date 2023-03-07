package com.example.jokes;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiService {
    @GET("random")
    Single<Joke> loadJoke();
}
