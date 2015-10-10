package com.commnetworks.unositetester.models;

import com.android.volley.Request;

/**
 * Created by Pratik on 10/9/2015
 */
public class UNORequest {

    private int method;

    private String path;

    public UNORequest(int method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethodName() {
        switch (this.method) {
            case Request.Method.GET:
                return "GET";
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.DELETE:
                return "DELETE";
            case Request.Method.PATCH:
                return "PATCH";
            case Request.Method.HEAD:
                return "HEAD";
            default:
                return "";
        }
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
