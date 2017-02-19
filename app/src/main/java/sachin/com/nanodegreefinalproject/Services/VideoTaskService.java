package sachin.com.nanodegreefinalproject.Services;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sachin.com.nanodegreefinalproject.Data.VideoColumns;
import sachin.com.nanodegreefinalproject.Data.VideoProvider;
import sachin.com.nanodegreefinalproject.Utils.JsonParser;

import static sachin.com.nanodegreefinalproject.Utils.AppConstants.API_KEY;
import static sachin.com.nanodegreefinalproject.Utils.AppConstants.PLAYLIST_IDS;

/**
 * Created by kapil pc on 2/13/2017.
 */

public class VideoTaskService extends GcmTaskService {
    private String LOG_TAG = VideoTaskService.class.getSimpleName();

    private OkHttpClient client = new OkHttpClient();
    private Context mContext;
    private StringBuilder mStoredSymbols = new StringBuilder();
    private boolean isUpdate;

    public VideoTaskService() {
    }

    public VideoTaskService(Context context) {
        mContext = context;
    }

    String fetchData(String playListId) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/youtube/v3/playlistItems").newBuilder();
        urlBuilder.addQueryParameter("part", "snippet");
        urlBuilder.addQueryParameter("playlistId", playListId);
        urlBuilder.addQueryParameter("maxResults", "50");
        urlBuilder.addQueryParameter("key", API_KEY);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    @Override
    public int onRunTask(TaskParams params) {
        Cursor initQueryCursor;
        if (mContext == null) {
            mContext = this;
        }

        initQueryCursor = mContext.getContentResolver().query(VideoProvider.Videos.CONTENT_URI,
                new String[]{VideoColumns._ID, VideoColumns.TITLE},
                VideoColumns.VIDEOTYPE + " = ?",
                new String[]{String.valueOf(params.getExtras().getInt("position"))}, null);

        if (initQueryCursor.getCount() > 0 && initQueryCursor != null) {
            isUpdate = true;
        }


        String getResponse;
        int result = GcmNetworkManager.RESULT_FAILURE;
        String playListId = PLAYLIST_IDS[params.getExtras().getInt("position")];


        if (playListId != null) {
            try {
                getResponse = fetchData(playListId);
                result = GcmNetworkManager.RESULT_SUCCESS;
                Log.i(LOG_TAG, getResponse);
                ArrayList<ContentProviderOperation> batchOperations = JsonParser.videoJsonToContentVals(getResponse, params.getExtras().getInt("position"));
                if (isUpdate) {
                    mContext.getContentResolver().delete(VideoProvider.Videos.CONTENT_URI,
                            VideoColumns.VIDEOTYPE + " = ?",
                            new String[]{String.valueOf(params.getExtras().getInt("position"))});
                    Log.i(LOG_TAG, "Previous Records Deleted of the type:- " + params.getExtras().getInt("position"));
                }
                try {
                    if (batchOperations.contains(null)) {
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(mContext.getApplicationContext(), "Something went Wrong !", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        mContext.getContentResolver().applyBatch(VideoProvider.AUTHORITY,
                                batchOperations);
                        Log.i(LOG_TAG, "Records Inserted of the type:- " + params.getExtras().getInt("position"));
                    }

                } catch (RemoteException | OperationApplicationException e) {
                    Log.e(LOG_TAG, "Error applying batch insert", e);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
