package com.ankit_1107.project.bookyourcar;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMUtil {

    static void sendNotification(RequestQueue requestQueue , String personId, String body){

        String URL = "https://fcm.googleapis.com/fcm/send";
        JSONObject mainObj = new JSONObject();
        try {
            Log.e("ankit","person id :  "+personId);
            mainObj.put("to","/topics/"+personId);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Book Your Car");
            notificationObj.put("body",body);
            mainObj.put("notification",notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAgWBE1xw:APA91bGgOzwnhZ4VGlvTChuI2Q5y8MB6BdzOM_lQQK-vTab_AFOEIKXdrxUjFtAgZM2Hmcd_d9ygf7cX6XHAXch1eMX36s7svFZ8vvmWvLPlVgXReyIOdgsc1KcBN7ptyS-sOQ4EJKAN");

                    return header;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
