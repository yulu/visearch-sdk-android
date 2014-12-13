package com.visenze.visearch.android.http;

import android.net.http.AndroidHttpClient;
import com.visenze.visearch.android.ViSearchException;
import com.visenze.visearch.android.util.AuthGenerator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpClientImp implements HttpClient {

    private String accessKey;
    private String secretKey;

    public HttpClientImp(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * Get for object
     *
     * @param url    api url
     * @param params parameters
     * @return json response
     */
    @Override
    public String getForObject(String url, Map<String, String> params) throws ViSearchException {
        AndroidHttpClient client = AndroidHttpClient.newInstance("");
        HttpResponse response;
        String jsonResponse;
        HttpGet request = new HttpGet();

        if (null == params) {
            params = new HashMap<String, String>();
        }
        params.putAll(AuthGenerator.getAuthParam(accessKey, secretKey));

        try {
            URI uri = new URIBuilder(new URI(url)).addParameters(mapToNameValuePair(params)).build();
            request.setURI(uri);
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            jsonResponse = EntityUtils.toString(entity);

        } catch (Exception e) {
            throw new ViSearchException("Error in getForObject: " + e.toString());
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonResponse;
    }

    /**
     * Post for object
     *
     * @param url    search url
     * @param params parameters
     * @return json response
     */
    @Override
    public String postForObject(String url, Map<String, String> params) {
        AndroidHttpClient client = AndroidHttpClient.newInstance("");
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        String jsonResponse;

        if (null == params) {
            params = new HashMap<String, String>();
        }
        params.putAll(AuthGenerator.getAuthParam(accessKey, secretKey));
        List<NameValuePair> nameValueList = mapToNameValuePair(params);

        try {
            request.setEntity(new UrlEncodedFormEntity(nameValueList, "UTF-8"));
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            jsonResponse = EntityUtils.toString(entity);

        } catch (Exception e) {
            throw new ViSearchException("Error in postForObject: " + e.toString());
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jsonResponse;
    }

    /**
     * Post for object with byte[]
     *
     * @param url    api url
     * @param params parameters
     * @param bytes  byte array
     * @return json response
     */
    @Override
    public String postForObject(String url, Map<String, String> params, byte[] bytes) {
        AndroidHttpClient client = AndroidHttpClient.newInstance("");
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        String jsonResponse;

        if (null == params) {
            params = new HashMap<String, String>();
        }
        params.putAll(AuthGenerator.getAuthParam(accessKey, secretKey));

        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue());
            }

            ByteArrayBody byteArrayBody = new ByteArrayBody(bytes, ContentType.MULTIPART_FORM_DATA, "image");
            builder.addPart("image", byteArrayBody);

            request.setEntity(builder.build());

            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            jsonResponse = EntityUtils.toString(entity);

        } catch (Exception e) {
            throw new ViSearchException("Error in postForObject: " + e.toString());
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonResponse;
    }

    /**
     * http post
     *
     * @param url    api rul
     * @param params parameters
     */
    @Override
    public void post(String url, Map<String, String> params) {
        AndroidHttpClient client = AndroidHttpClient.newInstance("");
        HttpPost httpPost = new HttpPost(url);
        params.putAll(AuthGenerator.getAuthParam(accessKey, secretKey));
        List<NameValuePair> nameValueList = mapToNameValuePair(params);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValueList, "UTF-8"));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            client.execute(httpPost);
        } catch (Exception e) {
            throw new ViSearchException("Error in post: " + e.toString());
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<NameValuePair> mapToNameValuePair(Map<String, ?> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return pairs;
    }
}
