package com.example.api;

import com.example.apiModels.Field;
import com.example.apiModels.Operator;
import com.example.apiModels.Parcel;
import com.example.apiModels.User;
import com.example.apiModels.YearPlan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetData {
    //Specify the request type and pass the relative URL//

    @Headers({
            "content-type: application/json",
    })
    @POST("authentication_token")
    Call<User> getToken(@Body LoginCredentials login);

    @GET("users.json")
    Call<List<User>> getUser(@Header("authorization") String token);

    @GET("yearplans.json")
    Call<List<YearPlan>> getYearPlans(@Header("authorization") String token);

    @GET("fields.json")
    Call<List<Field>> getFields(@Header("authorization") String token,@Query("yearPlan") Integer id);

    @GET("operators.json")
    Call<List<Operator>> getOperators(@Header("authorization") String token,@Query("yearPlan") Integer id);

    @GET("parcels.json")
    Call<List<Parcel>> getParcels(@Header("authorization") String token,@Query("yearPlan") Integer id);

}
