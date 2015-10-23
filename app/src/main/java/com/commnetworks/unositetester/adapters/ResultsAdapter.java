package com.commnetworks.unositetester.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.commnetworks.unositetester.R;
import com.commnetworks.unositetester.helpers.NicePrintingMap;
import com.commnetworks.unositetester.models.ResponseItem;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Pratik on 10/22/2015.
 */
public class ResultsAdapter extends BaseAdapter {

    private static final int COLOR_SUCCESS = 0xFF00FF00;
    private static final int COLOR_FAIL = 0xFFFF0000;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ResponseItem> mResponseItems;

    public ResultsAdapter(Context context, List<ResponseItem> responseItems) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mResponseItems = responseItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = mInflater.inflate(R.layout.main_results_row, parent, false);
            holder = new ViewHolder();
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            holder.tvRequestHost = (TextView)view.findViewById(R.id.tvRequestHost);
            holder.tvRequestTitle = (TextView)view.findViewById(R.id.tvRequestTitle);
            holder.tvResponseStatusCode = (TextView)view.findViewById(R.id.tvResponseStatusCode);
            holder.tvResponseHeaders = (TextView)view.findViewById(R.id.tvResponseHeaders);
            holder.tvResponseBody = (TextView)view.findViewById(R.id.tvResponseBody);
            holder.tvResponseTime = (TextView)view.findViewById(R.id.tvResponseTime);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        ResponseItem responseItem = mResponseItems.get(position);

        if (responseItem.getRequest() == null) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.tvRequestHost.setVisibility(View.GONE);
            holder.tvRequestTitle.setVisibility(View.GONE);
            holder.tvResponseStatusCode.setVisibility(View.GONE);
            holder.tvResponseHeaders.setVisibility(View.GONE);
            holder.tvResponseBody.setVisibility(View.GONE);
            holder.tvResponseTime.setVisibility(View.GONE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.tvRequestHost.setVisibility(View.VISIBLE);
            holder.tvRequestTitle.setVisibility(View.VISIBLE);
            holder.tvResponseStatusCode.setVisibility(View.VISIBLE);
            holder.tvResponseTime.setVisibility(View.VISIBLE);

            holder.tvResponseBody.setVisibility(responseItem.isResponseBodyVisible() ? View.VISIBLE : View.GONE);
            holder.tvResponseHeaders.setVisibility(responseItem.isResponseBodyVisible() ? View.VISIBLE : View.GONE);

            holder.tvRequestTitle.setText(String.format("%s\n%s", responseItem.getRequest().getMethodName(), responseItem.getRequest().getPath()));
            holder.tvRequestHost.setText(responseItem.getHost());
            holder.tvResponseStatusCode.setText(responseItem.getHttpStatusCode());
            holder.tvResponseHeaders.setText(new NicePrintingMap(responseItem.getHeaders()).toString());
            holder.tvResponseBody.setText(responseItem.getResponseBody());
            holder.tvResponseTime.setText(String.valueOf(responseItem.getResponseTime()) + "ms");

            holder.tvResponseStatusCode.setBackgroundColor(responseItem.isSuccess() ? COLOR_SUCCESS : COLOR_FAIL);
        }

        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return mResponseItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    private class ViewHolder {
        public ProgressBar progressBar;
        public TextView tvRequestTitle;
        public TextView tvResponseStatusCode;
        public TextView tvResponseHeaders;
        public TextView tvResponseBody;
        public TextView tvResponseTime;
        public TextView tvRequestHost;
    }
}
