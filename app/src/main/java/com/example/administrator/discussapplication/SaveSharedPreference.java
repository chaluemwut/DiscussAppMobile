package com.example.administrator.discussapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 6/22/2015.
 */
public class SaveSharedPreference {
    static final String PREF_USER_NAME= "username";
    static final String PREF_ROLE_ID= "role_id";
    static final String PREF_CHECK_BOX= "REMEMBER";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName,String roleID,Boolean checkBox)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_ROLE_ID, roleID);
        editor.putBoolean(PREF_CHECK_BOX, true);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static String getroleID(Context ctx)
    {

        return getSharedPreferences(ctx).getString(PREF_ROLE_ID, "");
    }
    public static boolean getheckBox(Context ctx)
    {

        return getSharedPreferences(ctx).getBoolean(PREF_CHECK_BOX, false);
    }
    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
