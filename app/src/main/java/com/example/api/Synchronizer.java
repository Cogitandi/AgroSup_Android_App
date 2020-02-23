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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Synchronizer {

    GetData service;
    Activity activity;
    String token;
    AppDatabase db;
    int userId;

    List<YearPlan> yearPlans;

    Handler handler;
    Handler thisHandler;

    int sendRequest = 0;
    int receivedRequest = 0;

    int currentYearPlanId;
    int lastYearPlanId;
    boolean error = false;


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
                    receivedRequest++;
                    getYearPlansApi();
                }
                if (msg.what == 2) {
                    lastYearPlanId = yearPlans.get(yearPlans.size() - 1).getId();
                    receivedRequest++;
                    for (YearPlan item : yearPlans) {
                        if (!error) {
                            getOperatorsApi(item.getId());
                            getFieldsApi(item.getId());
                            getParcelsApi(item.getId());
                        } else {
                            break;
                        }

                    }
                }
                if (msg.what == 3) {
                    receivedRequest++;
                }
                if (msg.what == 4) {
                    receivedRequest++;
                }
                if (msg.what == 5) {
                    receivedRequest++;
                    if (sendRequest == receivedRequest && currentYearPlanId == lastYearPlanId) {
                        sendMessageToMainActivity(1);
                    }

                }
                if (msg.what == 6) {
                    error = true;
                    sendMessageToMainActivity(2);
                }
                if (msg.what == 7) {
                    sendMessageToMainActivity(3);
                }

            }
        };
    }

    private void getToken(String email, String password) {
        LoginCredentials login = new LoginCredentials("a@b.pl", "ziomek");
        Call<UserApi> call = service.getToken(login);
        call.enqueue(new Callback<UserApi>() {
            @Override
            public void onResponse(Call<UserApi> call, Response<UserApi> response) {
                if (response.isSuccessful()) {
                    token = "bearer " + response.body().getToken();
                    getUserApi();
                } else {
                    sendMessageSync(7);
                    Toast.makeText(activity, activity.getString(R.string.sync_errorTokenApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserApi> call, Throwable t) {
                sendMessageSync(6);
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void getUserApi() {
        Call<List<UserApi>> getUserApi = service.getUser(token);
        getUserApi.enqueue(new Callback<List<UserApi>>() {
            @Override
            public void onResponse(Call<List<UserApi>> call, Response<List<UserApi>> response) {
                if (response.isSuccessful()) {
                    List<UserApi> userApiList = response.body();
                    for (UserApi item : userApiList) {
                        synchronize(item);
                    }
                } else {
                    sendMessageSync(6);
                    Toast.makeText(activity, activity.getString(R.string.sync_errorUserApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserApi>> call, Throwable t) {
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
            public void onResponse(Call<List<YearPlanApi>> call, Response<List<YearPlanApi>> response) {
                if (response.isSuccessful()) {
                    List<YearPlanApi> yearPlansApi = response.body();
                    synchronizeYearPlans(yearPlansApi);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorYearPlanApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(Call<List<YearPlanApi>> call, Throwable t) {
                Log.d("error: 2", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private void getOperatorsApi(int yearPlanApiId) {
        Call<List<OperatorApi>> getOperatorApis = service.getOperators(token, yearPlanApiId);
        getOperatorApis.enqueue(new Callback<List<OperatorApi>>() {
            @Override
            public void onResponse(Call<List<OperatorApi>> call, Response<List<OperatorApi>> response) {
                if (response.isSuccessful()) {
                    List<OperatorApi> operatorApis = response.body();
                    synchronizeOperators(operatorApis, yearPlanApiId);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorOperatorApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(Call<List<OperatorApi>> call, Throwable t) {
                Log.d("error: 3", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private void getFieldsApi(int yearPlanApiId) {
        Call<List<FieldApi>> getFieldApis = service.getFields(token, yearPlanApiId);
        getFieldApis.enqueue(new Callback<List<FieldApi>>() {
            @Override
            public void onResponse(Call<List<FieldApi>> call, Response<List<FieldApi>> response) {
                if (response.isSuccessful()) {
                    List<FieldApi> fieldsApi = response.body();
                    synchronizeFields(fieldsApi, yearPlanApiId);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorFieldApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(Call<List<FieldApi>> call, Throwable t) {
                Log.d("error: 4", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private void getParcelsApi(Integer yearPlanApiId) {
        Call<List<ParcelApi>> getParcelApis = service.getParcels(token, yearPlanApiId);
        getParcelApis.enqueue(new Callback<List<ParcelApi>>() {
            @Override
            public void onResponse(Call<List<ParcelApi>> call, Response<List<ParcelApi>> response) {
                if (response.isSuccessful()) {
                    List<ParcelApi> parcelsApi = response.body();
                    synchronizeParcels(parcelsApi, yearPlanApiId);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.sync_errorParcelApi), Toast.LENGTH_SHORT).show();
                    sendMessageSync(6);
                }
            }

            @Override
            public void onFailure(Call<List<ParcelApi>> call, Throwable t) {
                Log.d("error: 5", t.toString());
                Toast.makeText(activity, activity.getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
                sendMessageSync(6);
            }
        });
    }

    private User userApiToUser(UserApi userApi) {
        User user = new User(userApi.getId(), userApi.getEmail());
        return user;
    }

    private List<YearPlan> yearPlanApiToYearPlan(List<YearPlanApi> yearPlansApi, int userId) {
        List<YearPlan> yearPlanList = new ArrayList<YearPlan>();

        for (YearPlanApi item : yearPlansApi) {
            YearPlan yearPlan = new YearPlan(item.getId(), item.getStartYear(), item.getEndYear(), userId);
            yearPlanList.add(yearPlan);
        }

        return yearPlanList;
    }

    private List<Operator> operatorsApiToOperators(List<OperatorApi> operatorsApi, int yearPlanId) {
        List<Operator> operators = new ArrayList<Operator>();

        for (OperatorApi item : operatorsApi) {
            Operator operator = new Operator(item.getId(), item.getFirstName(), item.getSurname(), yearPlanId);
            operators.add(operator);
        }

        return operators;
    }

    private List<Field> fieldsApiToOperators(List<FieldApi> fieldsApi, int yearPlanId) {
        List<Field> fields = new ArrayList<Field>();

        for (FieldApi item : fieldsApi) {
            Field field = new Field(item.getId(), item.getName(), yearPlanId);
            fields.add(field);
        }

        return fields;
    }

    private List<Parcel> parcelsApiToOperators(List<ParcelApi> parcelsApi, int yearPlanId) {
        List<Parcel> parcels = new ArrayList<Parcel>();

        for (ParcelApi item : parcelsApi) {
            Parcel parcel = new Parcel(item.getId(), item.getParcelNumber(), item.getCultivatedArea(), item.isFuelApplication(), item.getArimrOperatorId(), yearPlanId);
            parcels.add(parcel);
        }

        return parcels;
    }

    private void synchronize(UserApi userApi) {
        sendRequest++;
        User user = userApiToUser(userApi);
        userId = user.getId();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (db.userDao().findByName(user.getEmail()) != null) { //check if exist
                    deleteOld(user);
                }

                db.userDao().insertAll(user);
                sendMessageSync(1);
            }
        });
        thread.start();
    }

    private void synchronizeYearPlans(List<YearPlanApi> yearPlansApi) {
        sendRequest++;
        yearPlans = yearPlanApiToYearPlan(yearPlansApi, userId);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db.yearPlanDao().insertAll(yearPlans.toArray(new YearPlan[0]));
                sendMessageSync(2);
            }
        });
        thread.start();
    }

    private void synchronizeOperators(List<OperatorApi> operatorsApi, int yearPlanId) {
        sendRequest++;
        List<Operator> operators = operatorsApiToOperators(operatorsApi, yearPlanId);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db.operatorDao().insertAll(operators.toArray(new Operator[0]));
                sendMessageSync(3);
            }
        });
        thread.start();
    }

    private void synchronizeFields(List<FieldApi> fieldsApi, int yearPlanId) {
        sendRequest++;
        List<Field> fields = fieldsApiToOperators(fieldsApi, yearPlanId);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db.fieldDao().insertAll(fields.toArray(new Field[0]));
                sendMessageSync(4);
            }
        });
        thread.start();
    }

    private void synchronizeParcels(List<ParcelApi> parcelsApi, int yearPlanId) {
        sendRequest++;
        currentYearPlanId = yearPlanId;
        List<Parcel> parcels = parcelsApiToOperators(parcelsApi, yearPlanId);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db.parcelDao().insertAll(parcels.toArray(new Parcel[0]));
                sendMessageSync(5);
            }
        });
        thread.start();
    }

    private void deleteOld(User user) {
        UserWithYearPlans userWithYearPlans = db.userDao().userWithYearPlans(userId);

        for (YearPlan item : userWithYearPlans.yearPlans) {
            db.operatorDao().deleteOperatorsByYearPlanId(item.getId());
            db.fieldDao().deleteFieldsByYearPlanId(item.getId());
            db.parcelDao().deleteParcelsByYearPlanId(item.getId());
        }

        db.userDao().delete(user);
        db.yearPlanDao().deleteYearPlansByUserId(user.getId());

    }

}
