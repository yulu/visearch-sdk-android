package com.visenze.visearch.android.core.json;

import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.ViSearchException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Json Parser using json interpreter
 */
public class JsonParser {

    public static ResultList parseResult(String jsonResponse) throws ViSearchException {
        ResultList resultList = new ResultList();

        try {
            JSONObject resultObj = new JSONObject(jsonResponse);
            resultList.setErrorMessage(checkStatus(resultObj));

            resultList.setTotal(resultObj.getInt("total"));
            resultList.setPage(resultObj.getInt("page"));
            resultList.setLimit(resultObj.getInt("limit"));

            if (resultObj.has("qinfo")) {
                JSONObject qinfoObj = resultObj.getJSONObject("qinfo");
                resultList.setQueryInfo(parseQuery(qinfoObj));
            }

            JSONArray resultArray = resultObj.getJSONArray("result");
            resultList.setImageList(parseImageList(resultArray));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ViSearchException e) {
            throw new ViSearchException("Error: " + e.toString());
        }

        return resultList;
    }

    private static Map<String, String> parseQuery(JSONObject qinfoObj) throws ViSearchException {
        Map<String, String> queryInfo = new HashMap<String, String>();
        try {
            Iterator<String> nameItr = qinfoObj.keys();
            while (nameItr.hasNext()) {
                String name = nameItr.next();
                queryInfo.put(name, qinfoObj.getString(name));
            }
        } catch (JSONException e) {
            throw new ViSearchException("JsonParse Error: " + e.toString());
        }

        return queryInfo;
    }

    private static List<ResultList.ImageResult> parseImageList(JSONArray resultArray) throws ViSearchException {
        List<ResultList.ImageResult> resultList = new ArrayList<ResultList.ImageResult>();
        int size = resultArray.length();

        try {
            for (int i = 0; i < size; i++) {
                JSONObject imageObj = resultArray.getJSONObject(i);
                ResultList.ImageResult imageResult = new ResultList.ImageResult();
                imageResult.setImageName(imageObj.getString("im_name"));

                if (imageObj.has("score")) {
                    imageResult.setScore(imageObj.getDouble("score"));
                }

                if (imageObj.has("value_map")) {
                    JSONObject valueObj = imageObj.getJSONObject("value_map");

                    Map<String, String> fieldList = new HashMap<String, String>();

                    Iterator<String> nameItr = valueObj.keys();
                    while (nameItr.hasNext()) {
                        String name = nameItr.next();
                        fieldList.put(name, valueObj.getString(name));

                        if (name.equals("im_url")) {
                            imageResult.setImageUrl(valueObj.getString(name));
                        }
                    }

                    imageResult.setFieldList(fieldList);
                }

                resultList.add(imageResult);
            }
        } catch (JSONException e) {
            throw new ViSearchException("JsonParse Error: " + e.toString());
        }

        return resultList;
    }

    private static String checkStatus(JSONObject jsonObj) throws ViSearchException {
        try {
            String status = jsonObj.getString("status");
            if (status == null) {
                throw new ViSearchException("Receiving api Response Error");
            } else {
                if (!status.equals("OK")) {
                    if (!jsonObj.has("error")) {
                        throw new ViSearchException("Receiving api Response Error");
                    } else {
                        return jsonObj.getJSONArray("error").get(0).toString();
                    }
                }
            }
        } catch (JSONException e) {
            throw new ViSearchException("JsonParse Error: " + e.toString());
        }

        return null;
    }

}
