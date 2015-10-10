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
        collection.add(new UNORequest(Request.Method.HEAD, blackboard));
        collection.add(new UNORequest(Request.Method.GET, blackboard));

        String mavlink = "https://mavlink.unomaha.edu";
        collection.add(new UNORequest(Request.Method.HEAD, mavlink));
        collection.add(new UNORequest(Request.Method.GET, mavlink));

        String mavtrack = "http://mavtrack.unomaha.edu";
        collection.add(new UNORequest(Request.Method.HEAD, mavtrack));
        collection.add(new UNORequest(Request.Method.GET, mavtrack));

        String firefly = "https://firefly.nebraska.edu";
        collection.add(new UNORequest(Request.Method.HEAD, firefly));
        collection.add(new UNORequest(Request.Method.GET, firefly));
    }

    public List<UNORequest> getCollection() {
        return collection;
    }

    public void setCollection(List<UNORequest> collection) {
        this.collection = collection;
    }

}
