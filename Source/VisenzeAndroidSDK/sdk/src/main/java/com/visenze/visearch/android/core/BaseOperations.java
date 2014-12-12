package com.visenze.visearch.android.core;

import com.visenze.visearch.android.http.HttpClientImp;

/**
 * Base http operations
 * Created by visenze on 10/17/14.
 */
public abstract class BaseOperations {
    protected String apiBase;
    protected HttpClientImp mHttpClient;


    public BaseOperations(HttpClientImp httpClient, String apiUrl) {
        this.mHttpClient = httpClient;
        this.apiBase = apiUrl;
    }


}
