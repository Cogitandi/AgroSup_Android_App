package com.example.api;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.agrosup_app.R;
import com.example.apiModels.FieldApi;
import com.example.apiModels.OperatorApi;
import com.example.apiModels.ParcelApi;
import com.example.apiModels.UserApi;
import com.example.apiModels.YearPlanApi;
import com.example.database.AppDatabase;
import com.example.database.UserWithYearPlans;
import com.example.entities.Field;
import com.example.entities.Operator;
import com.example.entities.Parcel;
import com.example.entities.User;
import com.example.entities.YearPlan;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Synchronizer {

    private GetData service;
    private Activity activity;
    private String token;
    private AppDatabase db;
    private int userId;

    private List<YearPlan> yearPlans;

    private Handler handler;
    private Handler thisHandler;

    private int sendRequest = 0;
    private int receivedRequest = 0;

    private int currentYearPlanId;
    private int lastYearPlanId;
    private boolean error = false;
    private LoginCredentials login;

    private int previousSelectedYearPlanId;
    private int previousSelectedOperatorId;

    private boolean syncParcels = false;
    private boolean syncFields = false;
    private boolean syncOperators = false;


    // msg
    // 6 - fail synchronization

    public Synchronizer(Activity activity, Handler handler) {

        this.service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        this.activity = activity;
        this.handler = handler;
        this.db = AppDatabase.getInstance(activity.getApplicationContext());
        this.thisHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    // successfull synchronize user
                    receivedRequest++;
                    getYearPlansApi();
                }
                if (msg.what == 2) {
                    // successfull synchronize yearplans
                    //lastYearPlanId = yearPlans.get(yearPlans.size() - 1).getId();
                    receivedRequest++;
                    getOperatorsApi();
                    getFieldsApi();
                    getParcelsApi();
                }
                if (msg.what == 3) {
                    // sync operators
                    syncOperators = true;
                    if (syncOperators && syncFields && syncParcels) sendMessageToMainActivity(1);
                }
                if (msg.what == 4) {
                    syncFields = true;
                    if (syncOperators && syncFields && syncParcels) sendMessageToMainActivity(1);
                }
                if (msg.what == 5) {
                    syncParcels = true;
                    if (syncOperators && syncFields && syncParcels) sendMessageToMainActivity(1);

                }
                if (msg.what == 6) {
                    // error with connection
                    error = true;
                    sendMessageToMainActivity(2);
                }
                if (msg.what == 7) {
                    // fail get token
                    sendMessageToMainActivity(3);
                }

            }
        };
    }

    private void getToken(String email, String password) {
        login = new LoginCredentials(email, password);
        Call<UserApi> call = service.getToken(login);
        call.enqueue(new Callback<UserApi>() {
            @Override
            public void onResponse(@NotNull Call<UserApi> call, @NotNull Response<UserApi> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        token = "bearer " + response.body().getToken();
                        getUserApi();
                    }
                } else {
                    sendMessageSync(7);
                    Toast.makeText(activity, activity.getString(R.string.sync_errorTokenApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserApi> call, @NotNull Throwable t) {
                sendMessageSync(6);
                Log.d("test", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserApi() {
        Call<List<UserApi>> getUserApi = service.getUser(token);
        getUserApi.enqueue(new Callback<List<UserApi>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserApi>> call, @NotNull Response<List<UserApi>> response) {
                if (response.isSuccessful()) {
                    List<UserApi> userApiList = response.body();
                    if (userApiList != null) {
                        for (UserApi item : userApiList) {
                            synchronize(item);
                        }
                    }
                } else {
                    sendMessageSync(6);
                    Toast.makeText(activity, activity.getString(R.string.sync_errorUserApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<UserApi>> call, @NotNull Throwable t) {
                Log.d("error:1 ", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private void getYearPlansApi() {
        Call<List<YearPlanApi>> getYearPlanApis = service.getYearPlans(token);

        getYearPlanApis.enqueue(new Callback<List<YearPlanApi>>() {
            @Override
            public void onResponse(@NotNull Call<List<YearPlanApi>> call, @NotNull Response<List<YearPlanApi>> response) {
                if (response.isSuccessful()) {
                    List<YearPlanApi> yearPlansApi = response.body();
                    synchronizeYearPlans(yearPlansApi);
                    Log.d("test:","dotarlem");
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorYearPlanApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<YearPlanApi>> call, @NotNull Throwable t) {
                Log.d("error: 2", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private void getOperatorsApi() {
        Call<List<OperatorApi>> getOperatorApis = service.getOperators(token);
        getOperatorApis.enqueue(new Callback<List<OperatorApi>>() {
            @Override
            public void onResponse(@NotNull Call<List<OperatorApi>> call, @NotNull Response<List<OperatorApi>> response) {
                if (response.isSuccessful()) {
                    List<OperatorApi> operatorApis = response.body();
                    synchronizeOperators(operatorApis);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorOperatorApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<OperatorApi>> call, @NotNull Throwable t) {
                Log.d("error: 3", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private void getFieldsApi() {
        Call<List<FieldApi>> getFieldApis = service.getFields(token);
        getFieldApis.enqueue(new Callback<List<FieldApi>>() {
            @Override
            public void onResponse(@NotNull Call<List<FieldApi>> call, @NotNull Response<List<FieldApi>> response) {
                if (response.isSuccessful()) {
                    List<FieldApi> fieldsApi = response.body();
                    synchronizeFields(fieldsApi);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorFieldApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<FieldApi>> call, @NotNull Throwable t) {
                Log.d("error: 4", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private void getParcelsApi() {
        Call<List<ParcelApi>> getParcelApis = service.getParcels(token);
        getParcelApis.enqueue(new Callback<List<ParcelApi>>() {
            @Override
            public void onResponse(@NotNull Call<List<ParcelApi>> call, @NotNull Response<List<ParcelApi>> response) {
                if (response.isSuccessful()) {
                    List<ParcelApi> parcelsApi = response.body();
                    synchronizeParcels(parcelsApi);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorParcelApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ParcelApi>> call, @NotNull Throwable t) {
                Log.d("error: 5", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private User userApiToUser(UserApi userApi) {
        return new User(userApi.getId(), userApi.getEmail(), login.getPassword());
    }

    private List<YearPlan> yearPlanApiToYearPlan(List<YearPlanApi> yearPlansApi, int userId) {
        List<YearPlan> yearPlanList = new ArrayList<>();

        for (YearPlanApi item : yearPlansApi) {
            YearPlan yearPlan = new YearPlan(item.getId(), item.getStartYear(), item.getEndYear(), userId);
            yearPlanList.add(yearPlan);
        }

        return yearPlanList;
    }

    private List<Operator> operatorsApiToOperators(List<OperatorApi> operatorsApi) {
        List<Operator> operators = new ArrayList<>();

        for (OperatorApi item : operatorsApi) {
            Operator operator = new Operator(item);
            operators.add(operator);
        }

        return operators;
    }

    private List<Field> fieldsApiToOperators(List<FieldApi> fieldsApi) {
        List<Field> fields = new ArrayList<>();

        for (FieldApi item : fieldsApi) {
            Field field = new Field(item);
            fields.add(field);
        }

        return fields;
    }

    private List<Parcel> parcelsApiToOperators(List<ParcelApi> parcelsApi) {
        List<Parcel> parcels = new ArrayList<>();

        for (ParcelApi item : parcelsApi) {
            Parcel parcel = new Parcel(item);
            parcels.add(parcel);
        }

        return parcels;
    }

    private void synchronize(UserApi userApi) {
        sendRequest++;
        User user = userApiToUser(userApi);
        userId = user.getId();

        Thread thread = new Thread(() -> {
            User pUser = db.userDao().findByName(user.getEmail());
            if (pUser != null) { //check if exist
                previousSelectedYearPlanId = pUser.getSelectedYearPlanId();
                previousSelectedOperatorId = pUser.getSelectedOperatorId();
                deleteOld(user);
            } else {
                db.userDao().insertAll(user);
            }

            sendMessageSync(1);
        });
        thread.start();
    }

    private void synchronizeYearPlans(List<YearPlanApi> yearPlansApi) {
        sendRequest++;
        yearPlans = yearPlanApiToYearPlan(yearPlansApi, userId);
        Thread thread = new Thread(() -> {
            db.yearPlanDao().insertAll(yearPlans.toArray(new YearPlan[0]));
            sendMessageSync(2);
        });
        thread.start();
    }

    private void synchronizeOperators(List<OperatorApi> operatorsApi) {
        sendRequest++;
        List<Operator> operators = operatorsApiToOperators(operatorsApi);
        Thread thread = new Thread(() -> {
            db.operatorDao().insertAll(operators.toArray(new Operator[0]));
            sendMessageSync(3);
        });
        thread.start();
    }

    private void synchronizeFields(List<FieldApi> fieldsApi) {
        sendRequest++;
        List<Field> fields = fieldsApiToOperators(fieldsApi);
        Thread thread = new Thread(() -> {
            db.fieldDao().insertAll(fields.toArray(new Field[0]));
            sendMessageSync(4);
        });
        thread.start();
    }

    private void synchronizeParcels(List<ParcelApi> parcelsApi) {
        sendRequest++;
        List<Parcel> parcels = parcelsApiToOperators(parcelsApi);
        Thread thread = new Thread(() -> {
            db.parcelDao().insertAll(parcels.toArray(new Parcel[0]));
            sendMessageSync(5);
        });
        thread.start();
    }

    private void sendMessageSync(int id) {
        Message msg = new Message();
        msg.what = id;
        thisHandler.sendMessage(msg);
    }

    private void sendMessageToMainActivity(int id) {
        Message message = new Message();
        message.what = id;
        handler.sendMessage(message);
    }

    public void startSynchronization(String email, String password) {
        getToken(email, password);
    }

    private void deleteOld(User user) {
        user.setSelectedYearPlanId(previousSelectedYearPlanId);
        user.setSelectedOperatorId(previousSelectedOperatorId);

        UserWithYearPlans userWithYearPlans = db.userDao().userWithYearPlans(userId);

        for (YearPlan item : userWithYearPlans.yearPlans) {
            db.operatorDao().deleteOperatorsByYearPlanId(item.getId());
            db.fieldDao().deleteFieldsByYearPlanId(item.getId());
            db.parcelDao().deleteParcelsByYearPlanId(item.getId());
        }

        db.yearPlanDao().deleteYearPlansByUserId(user.getId());
        db.userDao().updateUsers(user);

    }

}
