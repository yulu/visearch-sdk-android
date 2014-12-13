package com.visenze.visearch.android.core;

import com.visenze.visearch.android.http.HttpClientImp;


public abstract class BaseOperations {

    protected String apiBase;
    protected HttpClientImp mHttpClient;


    public BaseOperations(HttpClientImp httpClient, String apiUrl) {
        this.mHttpClient = httpClient;
        this.apiBase = apiUrl;
    }


}
