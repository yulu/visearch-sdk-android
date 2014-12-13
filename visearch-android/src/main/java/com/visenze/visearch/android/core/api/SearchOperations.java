package com.visenze.visearch.android.core.api;

import com.visenze.visearch.android.ColorSearchParams;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;


/**
 * Search operations interface.
 */
public interface SearchOperations {
    /**
     * ID Search (GET /search)
     * Searching against the image collection using an existing image in the collection
     *
     * @param idSearchParams the index search parameter setting
     * @return the result list
     */
    public ResultList search(IdSearchParams idSearchParams);

    /**
     * Color Search (GET /colorsearch)
     * Searching for image matching a color
     *
     * @param colorSearchParams the color search parameter setting
     * @return the result list
     */
    public ResultList colorSearch(ColorSearchParams colorSearchParams);

    /**
     * Upload Search (POST /uploadsearch)
     * Searching against the image collection with an uploading image.
     *
     * @param uploadSearchParams the index search parameter setting
     * @return the result list
     */
    public ResultList uploadSearch(UploadSearchParams uploadSearchParams);
}
