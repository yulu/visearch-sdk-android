package com.visenze.visearch.android.core;

import com.visenze.visearch.android.ColorSearchParams;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.VisenzeError;
import com.visenze.visearch.android.core.api.SearchOperationsTemplate;
import com.visenze.visearch.android.core.json.JsonParser;
import com.visenze.visearch.android.http.HttpClientImp;

/**
 * Implementation of search operation
 * Created by visenze on 10/17/14.
 */
public class SearchOperation extends BaseOperations implements SearchOperationsTemplate {
    private final static String ID_SEARCH = "/search";
    private final static String COLOR_SEARCH = "/colorsearch";
    private final static String UPLOAD_SEARCH = "/uploadsearch";


    public SearchOperation(HttpClientImp httpClient, String apiUrl) {
        super(httpClient, apiUrl);
    }

    /**
     * /search api
     *
     * @param idSearchParams the index search parameter setting
     * @return Result list
     * @throws VisenzeError
     */
    @Override
    public ResultList search(IdSearchParams idSearchParams) throws VisenzeError {
        String imageId = idSearchParams.getImageName();

        if (imageId == null || imageId.isEmpty()) {
            throw new VisenzeError("Missing parameter, image name is empty", VisenzeError.Code.ERROR);
        }

        String response = mHttpClient.getForObject(apiBase + ID_SEARCH, idSearchParams.toMap());
        return getResult(response);
    }

    /**
     * /colorsearch api
     *
     * @param colorSearchParams the color search parameter setting
     * @return Result list
     * @throws VisenzeError
     */
    @Override
    public ResultList colorSearch(ColorSearchParams colorSearchParams) throws VisenzeError {
        String color = colorSearchParams.getColor();
        if (color == null || color.isEmpty()) {
            throw new VisenzeError("Missing parameter, color code is empty", VisenzeError.Code.ERROR);
        }
        if (!color.matches("^[0-9a-fA-F]{6}$")) {
            throw new VisenzeError("Invalid parameter, only accept hex color code", VisenzeError.Code.ERROR);
        }

        String response = mHttpClient.getForObject(apiBase + COLOR_SEARCH, colorSearchParams.toMap());
        return getResult(response);
    }

    /**
     * /uploadsearch api
     *
     * @param uploadSearchParams the index search parameter setting
     * @return Result list
     * @throws VisenzeError
     */
    @Override
    public ResultList uploadSearch(UploadSearchParams uploadSearchParams) throws VisenzeError {
        byte[] imageBytes = uploadSearchParams.getImage().getByteArray();
        String imageUrl = uploadSearchParams.getImageUrl();

        String response;
        if (imageBytes == null && (imageUrl == null || imageUrl.isEmpty())) {
            throw new VisenzeError("Missing parameter, image empty", VisenzeError.Code.ERROR);

        } else if (imageBytes != null){
            response = mHttpClient.postForObject(apiBase + UPLOAD_SEARCH, uploadSearchParams.toMap(), imageBytes);

        } else {
            response = mHttpClient.postForObject(apiBase + UPLOAD_SEARCH, uploadSearchParams.toMap());

        }
        return getResult(response);
    }

    private ResultList getResult(String jsonResponse) throws VisenzeError {
        ResultList resultList;

        try {
            resultList = JsonParser.parseResult(jsonResponse);
        } catch (VisenzeError e) {
            throw new VisenzeError("Error: " + e.toString(), VisenzeError.Code.ERROR);
        }

        return resultList;
    }
}
