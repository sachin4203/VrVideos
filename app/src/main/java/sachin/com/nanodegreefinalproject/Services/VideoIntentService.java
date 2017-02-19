package sachin.com.nanodegreefinalproject.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by kapil pc on 2/13/2017.
 */

public class VideoIntentService extends IntentService {

    public VideoIntentService() {
        super(VideoIntentService.class.getName());
    }

    public VideoIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(VideoIntentService.class.getSimpleName(), "Video Intent Service");
        VideoTaskService videoTaskService = new VideoTaskService(this);
        Bundle args = new Bundle();
        args.putInt("position", intent.getIntExtra("position", 0));
        int result = videoTaskService.onRunTask(new TaskParams(intent.getStringExtra("type"), args));
        if (result == GcmNetworkManager.RESULT_SUCCESS) {
            Log.i("Data server fetch", String.valueOf(result));
        }
    }
}