package android.webinnovatives.com.seefgodriver.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hp on 3/31/2019.
 */

public class Common {
    public static void savePrefs(String email, String password, Context context) {
        SharedPreferences.Editor writer = context.getSharedPreferences(ConstantManager.SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        writer.putString(ConstantManager.EMAIL, email);
        writer.putString(ConstantManager.PASSWORD, password);
        writer.apply();
    }

    public static void resetPrefs(Context context) {
        context.getSharedPreferences(ConstantManager.SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();

    }
}
