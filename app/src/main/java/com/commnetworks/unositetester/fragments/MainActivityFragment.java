package com.commnetworks.unositetester.fragments;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.commnetworks.unositetester.R;
import com.commnetworks.unositetester.adapters.ResultsAdapter;
import com.commnetworks.unositetester.helpers.RequestCollection;
import com.commnetworks.unositetester.models.ResponseItem;
import com.commnetworks.unositetester.models.UNORequest;
import com.commnetworks.unositetester.network.GenericRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private BaseAdapter mAdapter;
    private List<ResponseItem> mResponseItems;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResponseItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();

        if (view != null) {
            ListView listView = (ListView) view.findViewById(R.id.listView);
            if (listView != null) {
                mAdapter = new ResultsAdapter(getActivity(), mResponseItems);

                listView.setEmptyView(view.findViewById(android.R.id.empty));
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mResponseItems.get(position).setResponseBodyVisible(!mResponseItems.get(position).isResponseBodyVisible());
                        mAdapter.notifyDataSetChanged();
                    }
                });

                performCollectionTests();

            }
        }
    }

    private void performCollectionTests() {
        mResponseItems.clear();

        RequestCollection collection = new RequestCollection();

        for(int i = 0; i<collection.getCollection().size(); i++){
            mResponseItems.add(new ResponseItem());
            callAPI(collection.getCollection().get(i), String.valueOf(i));
        }
    }

    public void callAPI(final UNORequest request, final String tag) {

        final int intTag = Integer.parseInt(tag);
        final long startTime = System.currentTimeMillis();

        new GenericRequest(getActivity().getApplication(), request, tag, new Response.Listener<ResponseItem>() {

            @Override
            public void onResponse(ResponseItem responseItem) {
                responseItem.setResponseTime(System.currentTimeMillis() - startTime);
                responseItem.setSuccess(true);
                replaceResponseItem(intTag, responseItem);
                mAdapter.notifyDataSetChanged();
                writeToFile(responseItem);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ResponseItem errorResponseItem = new ResponseItem();
                errorResponseItem.setResponseTime(System.currentTimeMillis() - startTime);
                errorResponseItem.setSuccess(false);
                errorResponseItem.setTag(tag);
                errorResponseItem.setRequest(request);
                errorResponseItem.setHost(request.getHost());

                if (error.networkResponse != null) {
                    errorResponseItem.setHttpStatusCode(String.valueOf(error.networkResponse.statusCode));
                    errorResponseItem.setHeaders(error.networkResponse.headers);
                    errorResponseItem.setResponseBody(new String(error.networkResponse.data));
                } else {
                    errorResponseItem.setHttpStatusCode("--");
                    errorResponseItem.setResponseBody("No response");
                }

                errorResponseItem.setResponseBodyVisible(false);
                replaceResponseItem(intTag, errorResponseItem);
                mAdapter.notifyDataSetChanged();
                writeToFile(errorResponseItem);
            }

        }).send();
    }

    private void replaceResponseItem(int position, ResponseItem newItem) {
        mResponseItems.remove(position);
        mResponseItems.add(position, newItem);
    }

    private void writeToFile(ResponseItem responseItem) {
        String content = responseItem.getRequest().getPath() + "\t" + responseItem.getRequest().getMethodName() + "\t"
                + responseItem.getHttpStatusCode() + "\t" + responseItem.getResponseTime() + "ms";
        String tag = "Error in printing to File: ";

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "data.csv");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException ioe) {
            Log.e(tag, ioe.getStackTrace().toString());
        }
    }

}
