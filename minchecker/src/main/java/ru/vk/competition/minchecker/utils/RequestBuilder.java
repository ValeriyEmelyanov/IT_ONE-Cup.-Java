package ru.vk.competition.minchecker.utils;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestBuilder {

    public static final String  API_ROOT =  new SystemVariables().apiRoot();
    
    public static Request postRequestBuilder(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(API_ROOT + url)
                .post(requestBody)
                .build();
    }

    public static Request putRequestBuilder(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(API_ROOT + url)
                .put(requestBody)
                .build();
    }

    public static Request deleteRequestBuilder(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(API_ROOT + url)
                .delete(requestBody)
                .build();
    }

    public static Request deleteRequestBuilder(String url) {
        return new Request.Builder()
                .url(API_ROOT + url)
                .delete()
                .build();
    }

    public static Request getRequestBuilder(String url) {
        return new Request.Builder()
                .url(API_ROOT + url)
                .get()
                .build();
    }
}
