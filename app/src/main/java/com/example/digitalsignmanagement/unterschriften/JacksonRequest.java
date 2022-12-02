package com.example.digitalsignmanagement.unterschriften;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;

public class JacksonRequest<T> extends Request<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private final Response.Listener<T> listener;
    private final Class<T> clazz;
    public JacksonRequest(int method,
                          String url,
                          Class<T> clazz,
                          Response.Listener<T> listener,
                          Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.clazz = clazz;
    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        T responsePayload;
        try {
            responsePayload = objectMapper.readValue(response.data,
                    clazz);
            return Response.success(responsePayload,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}