package com.rohim.rohimmodule;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    private UserSession session;
    private ItemValidation iv = new ItemValidation();
    String token0 = "", token1 = "", token2 = "", token3 = "",token4 = "", token5 = "";

    public void request(final Context context, final FetchDataListener listener, JSONObject jsonObject, final String ApiURL, String methode) throws JSONException {
        session = new UserSession(context);
        token0  = iv.encodeMD5(iv.encodeBase64(iv.getCurrentDate("SSSHHMMddmmyyyyss")));
        token1  = session.getSpUserId();
        token2  = iv.getCurrentDate("SSSHHyyyyssMMddmm");
        token3  = iv.sha256(token1+"&"+token2,token1+"die");
        if (listener != null) {
            listener.onFetchStart();
        }
        int method;
        switch (methode){
            case "GET":
                method = Request.Method.GET;
                break;
            case "POST":
                method = Request.Method.POST;
                break;
            case "PUT":
                method = Request.Method.PUT;
                break;
            default:
                method = Request.Method.DELETE;
                break;
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(method, ApiURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (listener != null) {
                                if(response.has("response")) {
                                    listener.onFetchComplete(response);
                                }else if(response.has("error")){
                                    listener.onFetchFailure(response.getString("error"));
                                } else{
                                    listener.onFetchComplete(null);
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            listener.onFetchFailure("Network Connectivity Problem");
                        } else if (error.networkResponse != null && error.networkResponse.data != null) {
                            VolleyError volley_error = new VolleyError(new String(error.networkResponse.data));
                            String errorMessage      = "";
                            try {
                                JSONObject errorJson = new JSONObject(volley_error.getMessage().toString());
                                if(errorJson.has("error")) errorMessage = errorJson.getString("error");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (errorMessage.isEmpty()) {
                                errorMessage = volley_error.getMessage();
                            }

                            if (listener != null) listener.onFetchFailure(errorMessage);
                        } else {
                            listener.onFetchFailure("Something went wrong. Please try again later");
                        }

                    }
                }
            ){
            // Request Header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Client-Service", "lpjk");
                params.put("Auth-Key", "rohim");
                params.put("User-Id", token1);
                params.put("Timestamp", token2);
                params.put("Signature", token3);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };

        RequestQueueService.getInstance(context).addToRequestQueue(postRequest.setShouldCache(false));
    }
}

