package sachin.com.nanodegreefinalproject.Data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by kapil pc on 2/11/2017.
 */

@Database(version = VideoDatabase.VERSION)
public class VideoDatabase {
    private VideoDatabase(){}

    public static final int VERSION = 7;

    @Table(VideoColumns.class) public static final String VIDEOS = "videos";
}
