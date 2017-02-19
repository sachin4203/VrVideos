package sachin.com.nanodegreefinalproject.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import sachin.com.nanodegreefinalproject.R;

/**
 * Created by kapil pc on 2/13/2017.
 */

public class AppConstants {

    public static String[] TAB_NAMES = {"Best of VR", "Travel", "Sports", "Music", "Gaming", "Movies", "Horror"};

    public static String[] PLAYLIST_IDS = {"PLU8wpH_LfhmsSVRA8bSknO4-2wXvYXS4C", "PLU8wpH_LfhmvMe2QPJpNnrUB4mlSC6QCw", "PLytPVni3m4-wU-3qqGwunBZ-0TCsytY6Z", "PLU8wpH_LfhmsBj0cG38m3cydQAIdFoO9v", "PLU8wpH_LfhmshvzpzSspjIGudX2b-vH3p",
            "PLU8wpH_LfhmvWzER0Cb8AFxvpmUKMTvPU", "PLU8wpH_LfhmsVEqgkZJhIdPLMPP6fDBZJ"};

    public static String API_KEY = "AIzaSyD26OLdLppqsCvgbE1fXJMwu-aO0fGlezI";


    public static boolean checkforConnectivity(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public static void networkToast(Context context) {
        Toast.makeText(context, context.getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
    }
}
