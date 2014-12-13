package com.visenze.visearch.android.http;

import com.visenze.visearch.android.ViSearchException;

import java.util.Map;


public interface HttpClient {

    public String getForObject(String url, Map<String, String> params) throws ViSearchException;

    public String postForObject(String url, Map<String, String> params) throws ViSearchException;

    public String postForObject(String url, Map<String, String> params, byte[] bytes) throws ViSearchException;

    void post(String url, Map<String, String> params) throws ViSearchException;

}
