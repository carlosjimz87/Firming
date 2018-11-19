package com.croowly.firming_fv.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.croowly.firming_fv.SplashActivity;
/**
 * Created by carlosjimz on 19/02/2018.
 */

public class FirmingService {

    public enum Modes { SUCCESSFUL, FAILED   }

    public static void testService(final Context context, final SplashActivity callback){

        RequestQueue queue = Volley.newRequestQueue(context);

        String URL = "http://firming.westeurope.cloudapp.azure.com:5000";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if (response.equals("FIRMING"))
                            callback.progressService(Modes.SUCCESSFUL);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.progressService(Modes.FAILED);
            }
        });

        queue.add(stringRequest);


    }

    public interface ProgressListener<String>  {

        void progressService(Modes mode);

    }
}
