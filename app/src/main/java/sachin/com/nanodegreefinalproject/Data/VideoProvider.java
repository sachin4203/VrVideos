package sachin.com.nanodegreefinalproject.Data;

/**
 * Created by kapil pc on 2/11/2017.
 */

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;




@ContentProvider(authority = VideoProvider.AUTHORITY, database = VideoDatabase.class)
public class VideoProvider {
    public static final String AUTHORITY = "sachin.com.nanodegreefinalproject.Data.VideoProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String VIDEOS = "videos";
    }

    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path:paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = VideoDatabase.VIDEOS)
    public static class Videos{
        @ContentUri(
                path = Path.VIDEOS,
                type = "vnd.android.cursor.dir/video"
        )
        public static final Uri CONTENT_URI = buildUri(Path.VIDEOS);

        @InexactContentUri(
                name = "VIDEO_ID",
                path = Path.VIDEOS + "/*",
                type = "vnd.android.cursor.item/video",
                whereColumn = VideoColumns.TITLE,
                pathSegment = 1
        )
        public static Uri withSymbol(String symbol){
            return buildUri(Path.VIDEOS, symbol);
        }
    }
}
