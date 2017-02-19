package sachin.com.nanodegreefinalproject.Widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import sachin.com.nanodegreefinalproject.Activity.VideoDetailActivity;
import sachin.com.nanodegreefinalproject.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static final String MyOnClick = "myOnClickTag";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MyOnClick.equals(intent.getAction())) {
            Toast.makeText(context, "Video's Refreshed", Toast.LENGTH_SHORT).show();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.video_widget);
            ComponentName watchWidget = new ComponentName(context, WidgetProvider.class);
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);


            Intent ntent = new Intent(context, VideoDetailActivity.class);
            ntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ntent);


        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);
            //mView.setOnClickPendingIntent(R.id.refresh_button, getPendingSelfIntent(context, MyOnClick));
            Intent startActivityIntent = new Intent(context, VideoDetailActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.widgetListView, startActivityPendingIntent);
            appWidgetManager.updateAppWidget(widgetId, mView);
        }


        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.video_widget);
        // mView.setOnClickPendingIntent(R.id.refresh_button, getPendingSelfIntent(context, MyOnClick));


        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.widgetListView, intent);


        return mView;
    }


    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
