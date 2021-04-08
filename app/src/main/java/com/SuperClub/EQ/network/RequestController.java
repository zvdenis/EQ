package com.SuperClub.EQ.network;

import android.content.Context;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Application.Configs;
import com.SuperClub.EQ.Data.QueueInfo;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RequestController {

    private static String baseURL = "http://192.168.0.1/";
    private Application applicationInstance;

    static {
        if (Configs.isTestMode) {
            baseURL = "https://049b43a0-5340-41eb-baf6-1277facc6113.mock.pstmn.io/";
        }
    }

    private static String loginURL = baseURL + "login/";
    private static String registerURL = baseURL + "register/";
    private static String createLiveQURL = baseURL + "createQueue/live/";
    private static String myQueuesURL = baseURL + "myQueues/";
    private static String registerForQueueURL = baseURL + "registerForQueue/";
    private static String leaveQueueURL = baseURL + "leaveQueue/";
    private static String getQueueByCodeURL = baseURL + "getQueueByCode/";

    static {
        if (Configs.isTestMode) {
            myQueuesURL = "https://bf20b5a0-8fe7-41a4-b3b7-fb49d8d8b939.mock.pstmn.io/myQueues";
            createLiveQURL = "https://222605fb-74f1-4d33-8467-83810ce4ab32.mock.pstmn.io/createQueue/live";
        }
    }

    private static RequestController instance = null;
    private Context context;

    private RequestController(Context context) {

        this.context = context;
        applicationInstance = Application.getInstance(context);
    }

    public static RequestController getInstance(Context context) {
        if (instance == null) {
            instance = new RequestController(context);
        }
        return instance;
    }

    public void sendLoginRequest(String email, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void sendRegisterRequest(String email, String password, String name, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", password);
            postData.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void sendRegisterForQueueRequest(String qID, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", applicationInstance.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerURL + qID.toString(), postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void sendLeaveQueueRequest(String qID, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", applicationInstance.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, leaveQueueURL + qID.toString(), postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendMyQueuesRequest(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", applicationInstance.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest(Request.Method.POST, myQueuesURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void updateMyQueues() {

        sendMyQueuesRequest(response -> {
            Type itemListType = new TypeToken<List<QueueInfo>>() {
            }.getType();
            ArrayList<QueueInfo> queues = new Gson().fromJson(response.toString(), itemListType);
            applicationInstance.setMyQueues(queues);
        }, error -> {
            System.out.println();
        });
    }

    public void sendCreateQueueRequest(String title, String description, Date start, Date end, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        try {
            postData.put("token", applicationInstance.getToken());
            postData.put("title", title);
            postData.put("description", description);
            postData.put("maxUsers", 1000);
            postData.put("dateStart", dateFormat.format(start));
            postData.put("dateEnd", dateFormat.format(end));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createLiveQURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendQueueByCodeRequest(String code, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest(Request.Method.POST, getQueueByCodeURL + code, postData, listener, errorListener);
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
