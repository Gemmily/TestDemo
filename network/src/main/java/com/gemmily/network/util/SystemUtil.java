package com.gemmily.network.util;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;

import java.lang.reflect.Method;

/**
 * Created by Gemmily on 2017/5/16.
 */
public class SystemUtil {
    public static int[]getScreenResolution(Activity activity){
        int width,height;
        Display display =activity.getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            width = size.x;
            height = size.y;
        }else {
            width = display.getWidth();
            height = display.getHeight();
        }
        return new int[]{width,height};
    }

    /**
     * 获取本应用的当前版本名
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(versionName)?"":versionName;
    }
    /**
     * 获取本应用的当前版本号
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    /**
     * 获取手机序列号
     * @return result is same to getSerialNumber1()
     */
    public static String getSerialNumber(){
        String serial = null;
        try {
            Class<?> c =Class.forName("android.os.SystemProperties");
            Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
    /**
     * 获取手机型号
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }
    /**
     * 获取系统版本
     * @return
     */
    public static String getSystemVersion() {
        return  android.os.Build.VERSION.RELEASE;
    }
}
