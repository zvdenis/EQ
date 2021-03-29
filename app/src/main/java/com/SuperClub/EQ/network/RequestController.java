package com.SuperClub.EQ.network;

import android.content.Context;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Application.Configs;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestController {

    private static String baseURL = "http://192.168.0.1/";

    static {
        if (Configs.isTestMode) {
            baseURL = "https://049b43a0-5340-41eb-baf6-1277facc6113.mock.pstmn.io/";
        }
    }

    private static String loginURL = baseURL + "login/";
    private static String registerURL = baseURL + "register/";
    private static String createLiveQURL = baseURL + "createQueue/live/";

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

    public void sendMyQueuesRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", Application.getInstance(context).getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerURL, postData, listener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


}
