package com.jiadu.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/23.
 */
public class SharePreferenceUtil {


    public static final String CONNECTIP = "connectip";
    public static final String INTERVAL = "interval";
    public static final String NAME = "share";
    /**
     * @param context 上下文
     * @param key 键
     * @return 如果有对应的键,放回value,否则返回""
     */
    public  static String getSring(Context context, String key){
        SharedPreferences share = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);


       String temp = share.getString(key, "");

        return temp;
    }



    public static boolean putString(Context context, String key, String value){

        SharedPreferences share = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        boolean issuccess = share.edit().putString(key, value)
                .commit();

        return issuccess;
    }

    public  static int getInt(Context context, String key){
        SharedPreferences share = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        int temp = share.getInt(key,0);

        return temp;
    }
    public  static boolean putInt(Context context, String key, int value){
        SharedPreferences share = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        boolean issuccess =share.edit().putInt(key,value).commit();

        return issuccess;
    }

}
