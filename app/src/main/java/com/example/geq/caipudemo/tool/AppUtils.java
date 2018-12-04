package com.example.geq.caipudemo.tool;
/**
 * @author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 * @CSDN博客: http://blog.csdn.net/qq_21376985
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 常用App的工具类，包含版本号、版本名称、安装的应用程序ICON
 */
public class AppUtils {
    private AppUtils() {
    }

    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getPacketName(Context context) {
        return context.getPackageName();
    }



    /**
     * 获取PackageManager对象
     */
    private static PackageManager getPackageManager(Context context) {
        return context.getPackageManager();
    }
    /**
     * 获取VersionName(版本名称)
     *
     * @param context
     * @return 失败时返回""
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = getPackageManager(context);
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPacketName(context), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取VersionCode(版本号)
     *
     * @param context
     * @return 失败时返回-1
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = getPackageManager(context);
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPacketName(context), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
