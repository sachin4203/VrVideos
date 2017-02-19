package sachin.com.nanodegreefinalproject.Activity;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import sachin.com.nanodegreefinalproject.R;

/**
 * Created by kapil pc on 2/10/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));

    }
}