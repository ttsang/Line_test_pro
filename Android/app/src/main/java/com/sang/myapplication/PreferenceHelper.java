package com.sang.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * This is manager class that support manage set and get preference data of app.
 *
 * @author Sang
 */
public class PreferenceHelper {
    private SharedPreferences mSharedPrefer;
    private static PreferenceHelper mInstance;

    private PreferenceHelper(Context context) {
        mSharedPrefer = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * get current doing preference
     *
     * @return
     */
    public SharedPreferences getPreference() {
        return mSharedPrefer;
    }

    /**
     * get settings instance
     *
     * @param context
     * @return
     */
    public static PreferenceHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferenceHelper(context);
        }
        return mInstance;
    }


    /**
     * Support track custom data
     *
     * @param value
     * @return
     */
    public void setImageLocal(String key, String value) {
        mSharedPrefer.edit().putString(key, value).apply();
    }

    /**
     * Support get value from key
     *
     * @return
     */
    public String getImageLocal(String key) {
        return mSharedPrefer.getString(key, "");
    }

}
