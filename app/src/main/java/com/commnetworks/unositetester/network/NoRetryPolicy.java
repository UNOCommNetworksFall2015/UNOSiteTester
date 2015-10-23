package com.commnetworks.unositetester.network;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;

/**
 * Created by Pratik on 10/23/2015.
 */
public class NoRetryPolicy extends DefaultRetryPolicy {

    public NoRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    public NoRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        super(initialTimeoutMs, maxNumRetries, backoffMultiplier);
    }

    /**
     * Never attempt a retry
     * @param error The error code of the last attempt.
     */
    @Override
    public void retry(VolleyError error) throws VolleyError {
        Log.e(NoRetryPolicy.class.getName(), "Entering retry() -> " + error.getMessage());
        throw error;
    }
}
