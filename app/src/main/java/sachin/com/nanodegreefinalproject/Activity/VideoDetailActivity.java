package sachin.com.nanodegreefinalproject.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import sachin.com.nanodegreefinalproject.Data.VideoColumns;
import sachin.com.nanodegreefinalproject.R;

public class VideoDetailActivity extends AppCompatActivity {

    // Identifier for the permission request
    private static final int WRITE_EXTERNAL_STORAGE = 1;
    private TextView mTitle, mDescription;
    private ImageView mVideoImage;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        mTitle = (TextView)findViewById(R.id.title);
        mDescription = (TextView)findViewById(R.id.description);
        mVideoImage = (ImageView)findViewById(R.id.high_res_videoImage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Watch Video");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initViews();

        mVideoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playVideo(mIntent.getStringExtra(VideoColumns.VIDEOID));
            }
        });


    }

    private void startDownload(){

        Intent next = new Intent(VideoDetailActivity.this, SampleDownloadActivity.class);
        next.putExtra("url", "https://youtube.com/watch?v=" + mIntent.getStringExtra(VideoColumns.VIDEOID));
        next.putExtra("type", "download");
        startActivity(next);
    }

    private void  initViews(){

         mIntent = getIntent();
        mTitle.setText(mIntent.getStringExtra(VideoColumns.TITLE));
        mDescription.setText(mIntent.getStringExtra(VideoColumns.DESCRIPTION));
        Picasso.with(this).load(mIntent.getStringExtra(VideoColumns.URLLARGE)).into(mVideoImage);


    }

    public void playVideo(String key){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));

        // Check if the youtube app exists on the device
        if (intent.resolveActivity(getPackageManager()) == null) {
            // If the youtube app doesn't exist, then use the browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
        }

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == R.id.action_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Watch " + mIntent.getStringExtra(VideoColumns.TITLE));
            shareIntent.putExtra(Intent.EXTRA_TEXT,  "https://youtube.com/watch?v=" + mIntent.getStringExtra(VideoColumns.VIDEOID));
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Share this video link with..."));
        }

        else if(item.getItemId() == R.id.action_download){
            if (ContextCompat.checkSelfPermission(VideoDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionToExternalStorage();
            }else {
                startDownload();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Called when the user is performing an action which requires the app to read the
    // user's contacts
    public void getPermissionToExternalStorage() {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE);
        }


    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(  Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (showRationale) {
                    // do something here to handle degraded mode
                } else {
                    Toast.makeText(this, "Write to storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
