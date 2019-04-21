package com.example.laptophome.sufra.data.local.SharedPrerefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrerefrencesManager {
    public static void SetSharedPrerefrences(String FileName,String key, String value, Context context)
    {
        SharedPreferences preferences =context.getSharedPreferences(FileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String GetSharedPrerefrences(String FileName,String key, Context context)
    {
        SharedPreferences preferences =context.getSharedPreferences(FileName,Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }
    public static void clearsharedprefrences(String FileName, Context context,String key)
    {
        SharedPreferences preferences =context.getSharedPreferences(FileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }
    public static void ClearSharedPrefrenceFile(String FileName,Context context)
    {
        SharedPreferences preferences =context.getSharedPreferences(FileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
