package com.avisit.vijayam.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by User on 4/28/2015.
 */
public class VersionUtils {

    public static int getVersionCode(Context context) {
        PackageInfo manager = null;
        try {
            manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return manager!=null ? manager.versionCode : 0;
    }
}