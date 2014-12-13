package com.visenze.visearch.android.core.api;

import com.visenze.visearch.android.ColorSearchParams;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.VisenzeError;


/**
 * Search operations
 * Created by visenze on 10/17/14.
 */
public interface SearchOperationsTemplate {
    /**
     * ID Search (GET /search)
     * Searching against the image collection using an existing image in the collection
     *
     * @param idSearchParams the index search parameter setting
     * @return the result list
     */
    public ResultList search(IdSearchParams idSearchParams) throws VisenzeError;

    /**
     * Color Search (GET /colorsearch)
     * Searching for image matching a color
     *
     * @param colorSearchParams the color search parameter setting
     * @return the result list
     */
    public ResultList colorSearch(ColorSearchParams colorSearchParams) throws VisenzeError;

    /**
     * Upload Search (POST /uploadsearch)
     * Searching against the image collection with an uploading image.
     *
     * @param uploadSearchParams the index search parameter setting
     * @return the result list
     */
    public ResultList uploadSearch(UploadSearchParams uploadSearchParams) throws VisenzeError;
}
