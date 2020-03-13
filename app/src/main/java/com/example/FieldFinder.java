package com.example;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.api.GetData;
import com.example.api.RetrofitClient;
import com.example.apiModels.FeatureCollection;
import com.example.apiModels.Layer;
import com.example.apiModels.TransformationApi;
import com.example.database.AppDatabase;
import com.example.entities.Field;
import com.example.entities.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldFinder {

    double longitude;
    double latitude;
    Context context;
    Handler mainHandler;
    User user;

    public FieldFinder(Location location, Context context, Handler mainHandler, User user) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.context = context;
        this.mainHandler = mainHandler;
        this.user = user;
        convertCordinates(longitude, latitude);
    }

    // Convert coordinates from epsg:4326 to epsg:2180
    private void convertCordinates(double x, double y) {
        GetData service = RetrofitClient.getRetrofitTransInstance().create(GetData.class);
        Call<TransformationApi> call = service.getConverted(x, y, 0, 4326, 2180);
        call.enqueue(new Callback<TransformationApi>() {
            @Override
            public void onResponse(Call<TransformationApi> call, Response<TransformationApi> response) {
                if (response.isSuccessful()) {
                    TransformationApi transformationApi = response.body();
                    getParcelEwApi(transformationApi);
                }
            }

            @Override
            public void onFailure(Call<TransformationApi> call, Throwable t) {
                // Error with coordinates convert
            }
        });
    }

    // Fetch parcel information
    private void getParcelEwApi(TransformationApi transformationApi) {
        GetData retro = RetrofitClient.getRetrofitInstanceXML().create(GetData.class);
        Call<FeatureCollection> call = retro.getParcelEw(transformationApi.getCoordinates());
        call.enqueue(new Callback<FeatureCollection>() {
            @Override
            public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {
                if (response.isSuccessful()) {
                    Layer layer = response.body().getFeatureMember().getLayer();
                    findFieldByParcelNumber(layer.toString());
                }

            }

            @Override
            public void onFailure(Call<FeatureCollection> call, Throwable t) {
                // Error with fetching information from GUGIK
            }
        });
    }

    // Find field by Parcel number
    private void findFieldByParcelNumber(String number) {
        AppDatabase db = AppDatabase.getInstance(context);
        Thread thread = new Thread(() -> {
            if (user != null) {
                int fieldId = db.parcelDao().findFieldIdByParcelNumber(user.getSelectedYearPlanId(), number);
                if (fieldId != 0) {
                    Field field = db.fieldDao().getFieldById(fieldId);
                     sendMessage(1,field);
                } else {
                    // Field not matched
                }

            }

        });
        thread.start();
    }

    private <T> void sendMessage(int code, T object) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = object;
        mainHandler.sendMessage(msg);
    }
}
