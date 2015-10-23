package com.commnetworks.unositetester.network;

import android.app.Application;
import android.content.Intent;
import android.location.GpsStatus;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;
import com.commnetworks.unositetester.models.UNORequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Pratik on 10/22/2015.
 */
abstract public class UNOSiteRequest<T> extends Request<T> {

    public static final String AUTHENTICATION_FAILURE = "AUTHENTICATION_FAILURE";

    private static final int MAX_VOLLEY_TIMEOUT_MS = 30000;
    private static final int MAX_VOLLEY_RETRIES = 0;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());

    private String idSignature;

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private static final RequestQueue DEFAULT_QUEUE;

    static {
        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);

        DEFAULT_QUEUE = new RequestQueue(new NoCache(), network);
        DEFAULT_QUEUE.start();
    }

    public static RequestQueue getDefaultQueue() {
        return DEFAULT_QUEUE;
    }

    private final Application application;
    private final Response.Listener<T> mListener;

    /**
     * Call API using an UNORequest
     * @param request
     * @param application
     * @param listener
     */
    public UNOSiteRequest(UNORequest request, final Application application, Response.Listener<T> listener,
                          Response.ErrorListener errorListener) {
        super(request.getMethod(), request.getPath(), errorListener);

        this.application = application;
        this.setShouldCache(false);
        this.mListener = listener;

        this.idSignature = UUID.randomUUID().toString();
    }

    public UNOSiteRequest(int method, String url, final Application application, Response.Listener<T> listener,
                          Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.application = application;
        this.mListener = listener;
    }

    public void send() {

        this.setRetryPolicy(new NoRetryPolicy(
                MAX_VOLLEY_TIMEOUT_MS,
                MAX_VOLLEY_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        logRequest();

        UNOSiteRequest.getDefaultQueue().add(this);
    }

    @Override
    protected final Response<T> parseNetworkResponse(NetworkResponse response) {
        logResponse(response);

        return Response.success(parseSuccessResponse(response), null);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        VolleyError error = super.parseNetworkError(volleyError);

        if (error.networkResponse != null) {
            String strData = new String(error.networkResponse.data);
            String strHeaders = new String(error.networkResponse.headers.toString());

            Log.d(UNOSiteRequest.class.getName(), "Response: statusCode = " + String.valueOf(error.networkResponse.statusCode));
            Log.d(UNOSiteRequest.class.getName(), "Response: data = " + strData);

            if (error.networkResponse.statusCode == 401) {
                if (application.getApplicationContext() != null) {
                    application.getApplicationContext().sendBroadcast(new Intent(AUTHENTICATION_FAILURE));
                }

                return new VolleyError(error.networkResponse);
            } else {
                return new VolleyError(error.networkResponse);
            }

        } else {
            Log.e(UNOSiteRequest.class.getName(), "Network error");
            return new VolleyError("The connection was lost or timed out.  Please try again.", volleyError);
        }
    }

    protected void deliverResponse(T response){
        mListener.onResponse(response);
    }

    protected abstract T parseSuccessResponse(NetworkResponse response);

    private void logRequest() {
        String tag = "API Request [" + this.idSignature + "]"; //WLog.makeLogTag("API Request [" + this.idSignature + "]");

        StringBuilder sb = new StringBuilder();
        sb.append("--== REQUEST ==--\n");
        sb.append("URL: ");
        sb.append(this.getUrl());
        sb.append("\n");

        sb.append("Method: ");
        sb.append(this.getMethod());
        sb.append("\n");

        Log.v(tag, sb.toString());
        //WLog.v(tag, sb.toString());
        //AnalyticsUtils.getInstance().trackEvent(new Event(relativeUrl, Category.API, this.getUrl()));
    }

    private void logResponse(NetworkResponse response) {
        String tag = "API Response [" + this.idSignature + "]"; // WLog.makeLogTag("API Response [" + this.idSignature + "]");
        Boolean logBody = false;

        StringBuilder sb = new StringBuilder();
        sb.append("--== RESPONSE ==--\n");
        sb.append("URL: ");
        sb.append(this.getUrl());
        sb.append("\n");

        sb.append("Method: ");
        sb.append(this.getMethod());
        sb.append("\n");

        sb.append("HEADERS: \n");
        HashMap<String, String> headers = new HashMap<>(response.headers);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append("\t");
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append("\n");

            if (entry.getKey().equalsIgnoreCase("Content-Type") && entry.getValue().contains("application/json")) {
                logBody = true;
            }
        }

        if (logBody) {
            if (response.data != null) {
                sb.append("BODY: ");
                sb.append(new String(response.data));
                sb.append("\n");
            } else {
                sb.append("BODY: Empty body.\n");
            }
        } else {
            sb.append("BODY: Binary data(");
            sb.append(response.data.length);
            sb.append(" bytes\n");
        }

        Log.v(tag, sb.toString());
        //WLog.v(tag, sb.toString());
        //AnalyticsUtils.getInstance().trackEvent(new Event(relativeUrl, Category.API, this.getUrl()));
    }
}
