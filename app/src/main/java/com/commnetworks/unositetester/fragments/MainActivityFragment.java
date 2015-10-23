package com.commnetworks.unositetester.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.commnetworks.unositetester.R;
import com.commnetworks.unositetester.adapters.ResultsAdapter;
import com.commnetworks.unositetester.helpers.RequestCollection;
import com.commnetworks.unositetester.models.ResponseItem;
import com.commnetworks.unositetester.models.UNORequest;
import com.commnetworks.unositetester.network.GenericRequest;
import com.commnetworks.unositetester.network.UNOSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private BaseAdapter mAdapter;
    private List<ResponseItem> mResponseItems;

    RequestQueue mQueue;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResponseItems = new ArrayList<>();
        mQueue = UNOSingleton.getInstance(this.getActivity().getApplicationContext()).getRequestQueue();
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

        /*StringRequest stringRequest = new StringRequest(request.getMethod(), request.getPath(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ResponseItem rp = new ResponseItem(request, "200", response);
                rp.setSuccess(true);
                rp.setResponseTime(System.currentTimeMillis() - startTime);
                replaceResponseItem(intTag, rp);
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResponseItem rp = new ResponseItem(request, String.valueOf(error.networkResponse.statusCode), error.getLocalizedMessage());
                rp.setSuccess(false);
                rp.setResponseTime(System.currentTimeMillis() - startTime);
                rp.setTag(tag);
                rp.setResponseBodyVisible(false);
                replaceResponseItem(intTag, rp);
                mAdapter.notifyDataSetChanged();
            }
        });

        mQueue.add(stringRequest);*/

        new GenericRequest(getActivity().getApplication(), request, tag, new Response.Listener<ResponseItem>() {

            @Override
            public void onResponse(ResponseItem responseItem) {
                responseItem.setResponseTime(System.currentTimeMillis() - startTime);
                responseItem.setSuccess(true);
                replaceResponseItem(intTag, responseItem);
                mAdapter.notifyDataSetChanged();
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
            }

        }).send();
    }

    private void replaceResponseItem(int position, ResponseItem newItem) {
        mResponseItems.remove(position);
        mResponseItems.add(position, newItem);
    }

}
