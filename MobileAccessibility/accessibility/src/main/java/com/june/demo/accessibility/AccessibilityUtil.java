package com.june.demo.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class AccessibilityUtil {

    public static void clickAll(AccessibilityNodeInfo rootNodeInfo) {
        clickIgnore(rootNodeInfo);
        close3sAd(rootNodeInfo);
        closeVideoAd(rootNodeInfo);

        // 关闭广告详情页
        commonPerformById(rootNodeInfo, "com.songheng.eastnews:id/tt_titlebar_close");
    }

    // 忽略按钮
    public static void clickIgnore(AccessibilityNodeInfo nodeInfo) {
        commonPerformById(nodeInfo, "com.songheng.eastnews:id/y3");
    }

    // 30秒视频广告
    public static void closeVideoAd(AccessibilityNodeInfo rootNodeInfo) {
        commonPerformById(rootNodeInfo, "com.songheng.eastnews:id/tt_video_ad_close");
    }

    // 3秒广告
    public static void close3sAd(AccessibilityNodeInfo rootNodeInfo) {
        commonPerformById(rootNodeInfo, "com.songheng.eastnews:id/as4");
        commonPerformById(rootNodeInfo, "com.songheng.eastnews:id/as9");
    }


    // 立即领取
    public static void takeImmediately(Rect rect){
        if(rect !=  null){
            int x = rect.right / 2;
            int y = (rect.bottom - rect.top) / 4 * 2 / 3 + (rect.bottom - rect.top) / 2 + rect.top;
            ShellUtil.execShellCmd("input tap " + x + " " + y);
        }
    }

    public static void center(AccessibilityService accessibilityService, AccessibilityNodeInfo rootNodeInfo, Rect rect){
        if(rect != null) {
            Log.d("AccessibilityUtil", "left:" + rect.left + " top:" + rect.top + " right:" + rect.right + " bottom:" + rect.bottom);
            int x = rect.right / 2;
            int y = (rect.bottom - rect.top) / 2 + rect.top;
            ShellUtil.execShellCmd("input tap " + x + " " + y);
//        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription();
        }
    }


    public static void commonPerformByText(AccessibilityNodeInfo rootNodeInfo, String text) {
        List<AccessibilityNodeInfo> all = rootNodeInfo.findAccessibilityNodeInfosByText(text);
        if(all != null){
            for(int i = 0; i < all.size(); i++) {
                AccessibilityNodeInfo node = all.get(i);
                if (node != null && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    public static void commonPerformById(AccessibilityNodeInfo rootNodeInfo, String id) {
        List<AccessibilityNodeInfo> all = rootNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if(all != null){
            for(int i = 0; i < all.size(); i++) {
                AccessibilityNodeInfo node = all.get(i);
                if (node != null && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }
}
