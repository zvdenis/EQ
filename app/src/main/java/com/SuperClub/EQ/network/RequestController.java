package com.SuperClub.EQ.network;

import android.content.Context;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Application.Configs;
import com.SuperClub.EQ.Data.QueueInfo;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.SuperClub.EQ.Application.Configs.fullFormat;

public class RequestController {

    private static String baseURL = "http://192.168.43.31:8080/";

    public Application getApplicationInstance() {
        return Application.getInstance(context);
    }


    static {
        if (Configs.isTestMode) {
            baseURL = "https://049b43a0-5340-41eb-baf6-1277facc6113.mock.pstmn.io/";
        }
    }

    private static String loginURL = baseURL + "login/";
    private static String registerURL = baseURL + "register/";
    private static String createLiveQURL = baseURL + "createQueue/live/";
    private static String myQueuesURL = baseURL + "myQueues/";
    private static String adminQueuesURL = baseURL + "myAdminsQueues/";
    private static String registerForQueueURL = baseURL + "registerForQueue/";
    private static String leaveQueueURL = baseURL + "leaveQueue/";
    private static String getQueueByCodeURL = baseURL + "getQueueByCode/";
    private static String nextUserURL = baseURL + "next/";
    private static String updateUserURL = baseURL + "updateUser/";
    private static String offlineUserURL = baseURL + "registerWithoutQueue/";
    private static String peopleInQueueURL = baseURL + "queueUsers/";

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
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerForQueueURL + qID.toString(), postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void sendLeaveQueueRequest(String qID, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, leaveQueueURL + qID.toString(), postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendMyQueuesRequest(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest(Request.Method.POST, myQueuesURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void sendAdminQueuesRequest(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest(Request.Method.POST, adminQueuesURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void updateMyQueues() {

        sendMyQueuesRequest(response -> {
            Type itemListType = new TypeToken<List<QueueInfo>>() {
            }.getType();
            ArrayList<QueueInfo> queues = new Gson().fromJson(response.toString(), itemListType);
            getApplicationInstance().setMyQueues(queues);
        }, error -> {
            System.out.println();
        });


        sendAdminQueuesRequest(response -> {
            Type itemListType = new TypeToken<List<QueueInfo>>() {
            }.getType();
            ArrayList<QueueInfo> queues = new Gson().fromJson(response.toString(), itemListType);
            getApplicationInstance().setAdminQueues(queues);
        }, error -> {
            System.out.println();
        });
    }

    public void sendCreateQueueRequest(String title, String description, Date start, Date end, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        fullFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        try {
            postData.put("token", getApplicationInstance().getToken());
            postData.put("title", title);
            postData.put("description", description);
            postData.put("maxUsers", 1000);
            postData.put("dateStart", fullFormat.format(start));
            postData.put("dateEnd", fullFormat.format(end));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createLiveQURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendQueueByCodeRequest(String code, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getQueueByCodeURL + code, postData, listener, errorListener);
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendNextUserRequest(String queueID, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();

        try {
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, nextUserURL + queueID, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendUpdateUserRequest(String email, String name, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", password);
            postData.put("name", name);
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, updateUserURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendOfflineUserRequest(String queueID, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();

        try {
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, offlineUserURL + queueID, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void sendPeopleInQueuesRequest(String qID, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", getApplicationInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest(Request.Method.POST, peopleInQueueURL + qID, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
