package com.commnetworks.unositetester.helpers;

import com.android.volley.Request;
import com.commnetworks.unositetester.models.UNORequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pratik on 10/9/2015
 */
public class RequestCollection {

    List<UNORequest> collection;

    public RequestCollection(){
        collection = new ArrayList<>();

        generateRequests();
    }

    private void generateRequests(){
        String blackboard = "https://blackboard.unomaha.edu";
        collection.add(new UNORequest(Request.Method.HEAD, blackboard, "unomaha.edu"));
        collection.add(new UNORequest(Request.Method.GET, blackboard, "unomaha.edu"));

        String mavlink = "https://mavlink.nebraska.edu";
        collection.add(new UNORequest(Request.Method.HEAD, mavlink, "nebraska.edu"));
        collection.add(new UNORequest(Request.Method.GET, mavlink, "nebraska.edu"));

        String mavlink2 = "https://mavlink.unomaha.edu";
        collection.add(new UNORequest(Request.Method.HEAD, mavlink2, "unomaha.edu"));
        collection.add(new UNORequest(Request.Method.GET, mavlink2, "unomaha.edu"));

        String mavtrack = "http://mavtrack.unomaha.edu";
        collection.add(new UNORequest(Request.Method.HEAD, mavtrack, "unomaha.edu"));
        collection.add(new UNORequest(Request.Method.GET, mavtrack, "unomaha.edu"));

        String firefly = "https://firefly.nebraska.edu";
        collection.add(new UNORequest(Request.Method.HEAD, firefly, "nebraska.edu"));
        collection.add(new UNORequest(Request.Method.GET, firefly, "nebraska.edu"));

        String main = "http://www.unomaha.edu";
        collection.add(new UNORequest(Request.Method.HEAD, main, "unomaha.edu"));
        collection.add(new UNORequest(Request.Method.GET, main, "unomaha.edu"));
    }

    public List<UNORequest> getCollection() {
        return collection;
    }

    public void setCollection(List<UNORequest> collection) {
        this.collection = collection;
    }

}
