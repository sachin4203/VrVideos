package sachin.com.nanodegreefinalproject.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kapil pc on 2/19/2017.
 */
public  class SharedPrefs {

    private static String MY_PREFERENCE = "MyPrefs";
    private static String IS_LOGGED_IN = "loginStatus";


  public static void setUserLoggedIn(Context context){
      SharedPreferences settingsfile= context.getSharedPreferences(MY_PREFERENCE,0);
      SharedPreferences.Editor myeditor = settingsfile.edit();
      myeditor.putBoolean(IS_LOGGED_IN, true);
      myeditor.commit();

  }

    public static Boolean getUserLoggedIn(Context context){
        SharedPreferences settingsfile= context.getSharedPreferences(MY_PREFERENCE,0);

       return (settingsfile.getBoolean(IS_LOGGED_IN, false));


    }


}
