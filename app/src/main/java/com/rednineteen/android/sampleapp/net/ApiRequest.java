package com.rednineteen.android.sampleapp.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rednineteen.android.sampleapp.models.Content;

import java.io.UnsupportedEncodingException;

/**
 * Created on 2/02/2018 by Juan Velasquez - email:  juan@rednineteen.com.
 */

public class ApiRequest extends Request<Content> {

    private final Gson gson = new Gson();
    private final Response.Listener<Content> listener;

    public ApiRequest(int method, String url, Response.Listener<Content> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<Content> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, Content.class),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Content response) {
        listener.onResponse(response);
    }
}
