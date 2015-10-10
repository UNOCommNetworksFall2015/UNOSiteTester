package com.commnetworks.unositetester.models;

/**
 * Created by Pratik on 10/9/2015
 * Structure used to display call return data on the UI
 */
public class ResponseItem {

    private UNORequest request;
    private boolean success;
    private String httpStatusCode;
    private String responseBody;
    private boolean responseBodyVisible = false;
    private String tag;
    private long responseTime;

    public ResponseItem() {

    }

    public ResponseItem(UNORequest request, String httpStatusCode, String responseBody) {
        this.request = request;
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
    }

    public UNORequest getRequest() {
        return request;
    }

    public void setRequest(UNORequest request) {
        this.request = request;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public boolean isResponseBodyVisible() {
        return responseBodyVisible;
    }

    public void setResponseBodyVisible(boolean responseBodyVisible) {
        this.responseBodyVisible = responseBodyVisible;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
}
