package com.visenze.visearch.android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.visenze.visearch.android.core.SearchOperation;
import com.visenze.visearch.android.http.HttpClientImp;


/**
 * ViSearcher singleton handles all the search methods.
 *
 * ViSearcher should be initialised with a valid API access/secret key pair before it can be used.
 */
public class ViSearcher {

    private String searchApiHost = "http://visearch.visenze.com";
    private String searchApiPort = "80";

    private HttpClientImp httpClient;
    private SearchOperation searchOperation;

    private ResultListener mListener;

    private static ViSearcher instance;
    private Context mContext;

    /**
     * Return ViSearcher's Singleton
     * @return the singleton ViSearcher instance.
     */
    public static ViSearcher getInstance() {
        if (instance == null) {
            instance = new ViSearcher();
        }

        return instance;
    }

    private ViSearcher() {

    }

    /**
     * Initialise the ViSearcher with a valid access/secret key pair
     * @param context Activity context
     * @param accessKey the Access Key
     * @param secretKey the Secret Key
     */
    public void initSearch(Context context, String accessKey, String secretKey) {
        this.mContext = context;

        httpClient = new HttpClientImp(accessKey, secretKey);
        searchOperation = new SearchOperation(httpClient, searchApiHost+":"+searchApiPort);
    }


    /**
     * Sets the {@link com.visenze.visearch.android.ViSearcher.ResultListener ResultListener} to be notified of the search result
     * @param listener the ViSearcher.ResultListener to notify.
     */
    public void setListener(ResultListener listener) {
        mListener = listener;
    }

    /**
     * Remove the callback listener, stop listening for the result
     */
    public void removeListener() {
        if (mListener != null) {
            mListener = null;
        }
    }

    /**
     * Start search session with index parameters: search by provide the image id
     * @param idSearchParams index parameters.
     */
    public void idSearch(final IdSearchParams idSearchParams){
        new Thread() {
            @Override
            public void run() {
                try {
                    ResultList resultList = searchOperation.search(idSearchParams);

                    handleInUIThread(resultList);

                } catch (final VisenzeError e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Start search session with color parameters: search by providing a color code in hexadecimal
     * @param colorSearchParams color parameters.
     */
    public void colorSearch(final ColorSearchParams colorSearchParams) {
        new Thread() {
            @Override
            public void run() {
                try {
                    final ResultList resultList = searchOperation.colorSearch(colorSearchParams);

                    handleInUIThread(resultList);

                } catch (final VisenzeError e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Start search session with upload parameters: search by uploading a image or using an image url
     * @param uploadSearchParams upload parameters
     */
    public void uploadSearch(final UploadSearchParams uploadSearchParams) {
        new Thread() {
            @Override
            public void run() {
                try {
                    final ResultList resultList = searchOperation.uploadSearch(uploadSearchParams);

                    handleInUIThread(resultList);

                } catch (final VisenzeError e) {
                     e.printStackTrace();
                }
            }
        }.start();
    }

    private void handleInUIThread(final ResultList resultList) {
        Looper.prepare();
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    if (resultList.getErrorMessage() != null)
                        mListener.onSearchError(resultList.getErrorMessage());
                    else
                        mListener.onSearchResult(resultList);
                }
            }
        });
        Looper.loop();
    }

    /**
     * Interface to be notified of results and potential errors
     */
    public static interface ResultListener {

        /**
         * Called after a search session is started and a valid result is returned
         * @param resultList the result list if any, null otherwise.
         */
        public abstract void onSearchResult(final ResultList resultList);

        /**
         * Called after a search session is started and an error occurs
         * @param errorMessage the error message if error occurs.
         */
        public abstract void onSearchError(String errorMessage);

    }

}
