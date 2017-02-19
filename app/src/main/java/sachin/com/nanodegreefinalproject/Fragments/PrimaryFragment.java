package sachin.com.nanodegreefinalproject.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sachin.com.nanodegreefinalproject.Activity.VideoDetailActivity;
import sachin.com.nanodegreefinalproject.Adapters.VideoCursorAdapter;
import sachin.com.nanodegreefinalproject.Data.VideoColumns;
import sachin.com.nanodegreefinalproject.Data.VideoProvider;
import sachin.com.nanodegreefinalproject.R;
import sachin.com.nanodegreefinalproject.Services.VideoIntentService;
import sachin.com.nanodegreefinalproject.Utils.AppConstants;


/**
 * Created by Ratan on 7/29/2015.
 */


public class PrimaryFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CURSOR_LOADER_ID = 0;
    // Store instance variables
    private String title;
    private int mPosition;
    private boolean isViewShown = false;
    private boolean isFragmentLoaded = false;
    private Intent mServiceIntent;
    private RecyclerView mRecyclerView;
    private VideoCursorAdapter mCursorAdapter;

    // newInstance constructor for creating fragment with arguments
    public static PrimaryFragment newInstance(int page, String title) {
        PrimaryFragment fragmentFirst = new PrimaryFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_layout, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mCursorAdapter = new VideoCursorAdapter(getActivity(), null);
        mCursorAdapter.setOnItemClickListener(new VideoCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                Cursor c = getActivity().getContentResolver().query(VideoProvider.Videos.CONTENT_URI,
                        new String[]{VideoColumns._ID, VideoColumns.TITLE, VideoColumns.DESCRIPTION,
                                VideoColumns.URLDEFAULT, VideoColumns.URLLARGE, VideoColumns.VIDEOTYPE, VideoColumns.VIDEOID},
                        VideoColumns.VIDEOTYPE + " = ?",
                        new String[]{String.valueOf(mPosition)},
                        null);
                c.moveToPosition(pos);
                String title = c.getString(c.getColumnIndex(VideoColumns.TITLE));
                String description = c.getString(c.getColumnIndex(VideoColumns.DESCRIPTION));
                String videoUrl = c.getString(c.getColumnIndex(VideoColumns.VIDEOID));
                String imageUrl = c.getString(c.getColumnIndex(VideoColumns.URLLARGE));
                c.close();
                Intent next = new Intent(getActivity(), VideoDetailActivity.class);
                next.putExtra(VideoColumns.TITLE, title);
                next.putExtra(VideoColumns.DESCRIPTION, description);
                next.putExtra(VideoColumns.URLLARGE, imageUrl);
                next.putExtra(VideoColumns.VIDEOID, videoUrl);
                getActivity().startActivity(next);
            }
        });
        mRecyclerView.setAdapter(mCursorAdapter);


        if (!isViewShown && !isFragmentLoaded) {
            fetchData();
            isFragmentLoaded = true;
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null && isVisibleToUser && !isFragmentLoaded) {
            isViewShown = true;
            // fetchdata() contains logic to show data when mPosition is selected mostly asynctask to fill the data
            fetchData();
            isFragmentLoaded = true;
        } else {
            isViewShown = false;
        }
    }

    private void fetchData() {

        mServiceIntent = new Intent(getActivity(), VideoIntentService.class);
        mServiceIntent.putExtra("type", title);
        mServiceIntent.putExtra("position", mPosition);
        if (AppConstants.checkforConnectivity(getActivity()))
            getActivity().startService(mServiceIntent);
        else {
            AppConstants.networkToast(getActivity());
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), VideoProvider.Videos.CONTENT_URI,
                new String[]{VideoColumns._ID, VideoColumns.TITLE, VideoColumns.DESCRIPTION,
                        VideoColumns.URLDEFAULT, VideoColumns.URLLARGE, VideoColumns.VIDEOTYPE, VideoColumns.VIDEOID},
                VideoColumns.VIDEOTYPE + " = ?",
                new String[]{String.valueOf(mPosition)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}







