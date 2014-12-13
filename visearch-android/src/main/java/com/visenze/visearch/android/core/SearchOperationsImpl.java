package com.visenze.visearch.android.core;

import com.visenze.visearch.android.*;
import com.visenze.visearch.android.core.api.SearchOperations;
import com.visenze.visearch.android.core.json.JsonParser;
import com.visenze.visearch.android.http.HttpClientImp;

/**
 * Implementation of search operations interface.
 */
public class SearchOperationsImpl extends BaseOperations implements SearchOperations {
    private final static String ID_SEARCH = "/search";
    private final static String COLOR_SEARCH = "/colorsearch";
    private final static String UPLOAD_SEARCH = "/uploadsearch";


    public SearchOperationsImpl(HttpClientImp httpClient, String apiUrl) {
        super(httpClient, apiUrl);
    }

    /**
     * /search api
     *
     * @param idSearchParams the index search parameter setting
     * @return Result list
     */
    @Override
    public ResultList search(IdSearchParams idSearchParams) {
        String imageId = idSearchParams.getImageName();

        if (imageId == null || imageId.isEmpty()) {
            throw new ViSearchException("Missing parameter, image name is empty");
        }

        String response = mHttpClient.getForObject(apiBase + ID_SEARCH, idSearchParams.toMap());
        return getResult(response);
    }

    /**
     * /colorsearch api
     *
     * @param colorSearchParams the color search parameter setting
     * @return Result list
     */
    @Override
    public ResultList colorSearch(ColorSearchParams colorSearchParams) {
        String color = colorSearchParams.getColor();
        if (color == null || color.isEmpty()) {
            throw new ViSearchException("Missing parameter, color code is empty");
        }
        if (!color.matches("^[0-9a-fA-F]{6}$")) {
            throw new ViSearchException("Invalid parameter, only accept hex color code");
        }

        String response = mHttpClient.getForObject(apiBase + COLOR_SEARCH, colorSearchParams.toMap());
        return getResult(response);
    }

    /**
     * /uploadsearch api
     *
     * @param uploadSearchParams the index search parameter setting
     * @return Result list
     */
    @Override
    public ResultList uploadSearch(UploadSearchParams uploadSearchParams) {
        byte[] imageBytes = uploadSearchParams.getImage().getByteArray();
        String imageUrl = uploadSearchParams.getImageUrl();

        String response;
        if (imageBytes == null && (imageUrl == null || imageUrl.isEmpty())) {
            throw new ViSearchException("Missing parameter, image empty");

        } else if (imageBytes != null) {
            response = mHttpClient.postForObject(apiBase + UPLOAD_SEARCH, uploadSearchParams.toMap(), imageBytes);

        } else {
            response = mHttpClient.postForObject(apiBase + UPLOAD_SEARCH, uploadSearchParams.toMap());

        }
        return getResult(response);
    }

    private ResultList getResult(String jsonResponse) {
        ResultList resultList;

        try {
            resultList = JsonParser.parseResult(jsonResponse);
        } catch (ViSearchException e) {
            throw new ViSearchException("Error: " + e.toString());
        }

        return resultList;
    }
}
