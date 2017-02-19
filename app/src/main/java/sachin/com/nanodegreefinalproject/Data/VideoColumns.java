package sachin.com.nanodegreefinalproject.Data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by kapil pc on 2/11/2017.
 */

public class VideoColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "title";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String DESCRIPTION = "description";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String URLDEFAULT = "url_default";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String URLLARGE = "url_large";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String VIDEOID = "video_id";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String VIDEOTYPE = "video_type";
}
