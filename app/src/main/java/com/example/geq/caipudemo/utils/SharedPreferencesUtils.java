package com.example.geq.caipudemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static SharedPreferences sp;

    public  static void saveBoolean(Context context,String key ,boolean value){
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
       sp.edit().putBoolean(key,value).commit();
    }

    public  static boolean  getBoolean(Context context,String key ,boolean defvalue){
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return  sp.getBoolean(key,defvalue);
    }


}
