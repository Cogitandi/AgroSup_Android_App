package com.example.api;

import com.example.apiModels.FieldApi;
import com.example.apiModels.OperatorApi;
import com.example.apiModels.ParcelApi;
import com.example.apiModels.UserApi;
import com.example.apiModels.YearPlanApi;

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
    Call<UserApi> getToken(@Body LoginCredentials login);

    @GET("users.json")
    Call<List<UserApi>> getUser(@Header("authorization") String token);

    @GET("year_plans.json")
    Call<List<YearPlanApi>> getYearPlans(@Header("authorization") String token);

    @GET("fields.json")
    Call<List<FieldApi>> getFields(@Header("authorization") String token, @Query("yearPlan") Integer id);

    @GET("operators.json")
    Call<List<OperatorApi>> getOperators(@Header("authorization") String token, @Query("yearPlan") Integer id);

    @GET("parcels.json")
    Call<List<ParcelApi>> getParcels(@Header("authorization") String token, @Query("yearPlan") Integer id);

}
