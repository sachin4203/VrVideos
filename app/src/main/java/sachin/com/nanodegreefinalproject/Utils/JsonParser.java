package sachin.com.nanodegreefinalproject.Utils;

import android.content.ContentProviderOperation;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sachin.com.nanodegreefinalproject.Data.VideoColumns;
import sachin.com.nanodegreefinalproject.Data.VideoProvider;

/**
 * Created by kapil pc on 2/13/2017.
 */

public class JsonParser {
    private static String LOG_TAG = JsonParser.class.getSimpleName();

    private static int mPosition;


    public static ArrayList videoJsonToContentVals(String JSON, int position) {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONArray resultsArray = null;
        mPosition = position;
        try {
            jsonObject = new JSONObject(JSON);
            if (jsonObject != null && jsonObject.length() != 0) {
                resultsArray = jsonObject.getJSONArray("items");
                if (resultsArray != null && resultsArray.length() != 0) {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        jsonObject = resultsArray.getJSONObject(i);
                        ContentProviderOperation batchOperation = buildBatchOperation(jsonObject);
                        if (batchOperation != null) {
                            batchOperations.add(batchOperation);
                        }
                    }
                }


            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "String to JSON failed: " + e);
        }
        return batchOperations;
    }


    public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                VideoProvider.Videos.CONTENT_URI);

        JSONObject snippetObject, thumbnailObject;


        try {
            snippetObject = jsonObject.optJSONObject("snippet");
            thumbnailObject = snippetObject.optJSONObject("thumbnails");
            Log.i("Values", snippetObject.getString("title"));
            if (snippetObject == null || thumbnailObject == null) {
                Log.i("Check", "Should be null");
                return null;
            }

            Log.i("Check", "Not null");
            builder.withValue(VideoColumns.TITLE, snippetObject.optString("title"));
            builder.withValue(VideoColumns.DESCRIPTION, snippetObject.optString("description"));
            builder.withValue(VideoColumns.URLDEFAULT, thumbnailObject.optJSONObject("medium").optString("url", "http://vignette2.wikia.nocookie.net/main-cast/images/5/5b/Sorry-image-not-available.png"));
            builder.withValue(VideoColumns.URLLARGE, thumbnailObject.optJSONObject("high").optString("url", "https://vignette2.wikia.nocookie.net/main-cast/images/5/5b/Sorry-image-not-available.png"));
            builder.withValue(VideoColumns.VIDEOID, snippetObject.optJSONObject("resourceId").optString("videoId"));
            builder.withValue(VideoColumns.VIDEOTYPE, mPosition);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builder.build();
    }
}