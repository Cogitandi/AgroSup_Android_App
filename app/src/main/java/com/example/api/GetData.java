package com.example.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetData {
    //Specify the request type and pass the relative URL//

    @Headers({
            "content-type: application/json",
    })
    @POST("authentication_token")
    Call<User> getToken(@Body Login login);

    @GET("users.json")
    Call <User> getUser(@Header("authorization") String token);

}
