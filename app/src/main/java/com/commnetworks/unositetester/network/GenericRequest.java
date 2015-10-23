package com.commnetworks.unositetester.network;

import android.app.Application;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.commnetworks.unositetester.models.ResponseItem;
import com.commnetworks.unositetester.models.UNORequest;

/**
 * Created by PGayee@werner.com on 10/23/15
 */
public class GenericRequest extends UNOSiteRequest<ResponseItem> {

    private UNORequest request;
    private String tag;

    public GenericRequest(Application application, UNORequest request, String tag,
                          Response.Listener<ResponseItem> listener, Response.ErrorListener errorListener) {
        super(request, application, listener, errorListener);
        this.request = request;
        this.tag = tag;
    }

    @Override
    protected ResponseItem parseSuccessResponse(NetworkResponse response) {
        String data = new String(response.data);
        ResponseItem item = new ResponseItem();
        item.setRequest(request);
        item.setHttpStatusCode(String.valueOf(response.statusCode));
        item.setTag(tag);
        item.setResponseBody(data);
        item.setHost(request.getHost());
        item.setHeaders(response.headers);
        return item;
    }
}
