package com.visenze.visearch.android.http;

import com.visenze.visearch.android.VisenzeError;

import java.util.Map;

/**
 * http template
 * Created by visenze on 10/17/14.
 */
public interface HttpTemplate {
    public String getForObject(String url, Map<String, String> params) throws VisenzeError;

    public String postForObject(String url, Map<String, String> params) throws VisenzeError;

    public String postForObject(String url, Map<String, String> params, byte[] bytes) throws VisenzeError;

    void post(String url, Map<String, String> params) throws VisenzeError;
}
