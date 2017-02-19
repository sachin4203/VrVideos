package sachin.com.nanodegreefinalproject.Widget;

/**
 * Created by kapil pc on 12/17/2016.
 */

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;
import java.util.List;

import sachin.com.nanodegreefinalproject.Data.VideoColumns;
import sachin.com.nanodegreefinalproject.Data.VideoProvider;
import sachin.com.nanodegreefinalproject.R;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory {

    List mTitle = new ArrayList<String>();
    List mImage = new ArrayList<String>();
    List mVideoId = new ArrayList<String>();
    List mDescription = new ArrayList<String>();


    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTitle.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        final RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                R.layout.video_widget_item);
        mView.setTextViewText(R.id.title, mTitle.get(position).toString());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("myTag", position);

        Intent i = new Intent();
        Bundle extras = new Bundle();
        extras.putString(VideoColumns.TITLE, mTitle.get(position).toString());
        extras.putString(VideoColumns.DESCRIPTION, mDescription.get(position).toString());
        extras.putString(VideoColumns.VIDEOID, "https://youtube.com/watch?v=" + mVideoId.get(position).toString());
        extras.putString(VideoColumns.URLLARGE, mImage.get(position).toString());

        i.putExtras(extras);
        mView.setOnClickFillInIntent(R.id.title, i);

        return mView;
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData() {
        mTitle.clear();
        mImage.clear();
        mVideoId.clear();
        mDescription.clear();

        Cursor c = mContext.getContentResolver().query(VideoProvider.Videos.CONTENT_URI,
                new String[]{VideoColumns._ID, VideoColumns.TITLE, VideoColumns.DESCRIPTION,
                        VideoColumns.URLDEFAULT, VideoColumns.URLLARGE, VideoColumns.VIDEOTYPE, VideoColumns.VIDEOID},
                VideoColumns.VIDEOTYPE + " = ?",
                new String[]{"0"},
                null);
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            mTitle.add(c.getString(c.getColumnIndex(VideoColumns.TITLE)));
            mImage.add(c.getString(c.getColumnIndex(VideoColumns.URLLARGE)));
            mVideoId.add(c.getString(c.getColumnIndex(VideoColumns.VIDEOID)));
            mDescription.add(c.getString(c.getColumnIndex(VideoColumns.DESCRIPTION)));


        }
        c.close();
    }

    @Override
    public void onDestroy() {

    }

}

