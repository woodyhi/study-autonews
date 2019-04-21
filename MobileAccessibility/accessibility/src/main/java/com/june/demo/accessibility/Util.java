package com.june.demo.accessibility;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class Util {
    public static String getTopActivityPackageName(Context context) {
        String topActivityPackage = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityPackage = f.getPackageName();
            Log.d("ddddd", f.getClassName() + " \n " + f.getPackageName());
        }
        return topActivityPackage;
    }

    //    TopActivityInfo, pkgName: com.songheng.eastnews activityName: com.songheng.eastnews/com.songheng.eastfirst.business.nativeh5.view.activity.CommonH5Activity callingPackage:   bstSpecialAppKeyboardHandlingEnabled = false

    public static boolean isH5Shown(Context context){
        String topActivityPackage = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityPackage = f.getPackageName();
        }
        return false;
    }
}
