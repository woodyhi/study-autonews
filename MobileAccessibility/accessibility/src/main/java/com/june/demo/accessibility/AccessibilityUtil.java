package com.june.demo.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

//ClassName:android.widget.TextView Desc:null Text:2s | 跳过 ResId:com.songheng.eastnews:id/tt_splash_skip_tv

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
        commonPerformById(rootNodeInfo, "com.songheng.eastnews:id/apn");
    }


    // 立即领取
    public static void takeImmediately(Rect rect) {
        if (rect != null) {
            int x = rect.right / 2;
            int y = (rect.bottom - rect.top) / 4 * 2 / 3 + (rect.bottom - rect.top) / 2 + rect.top;
            ShellUtil.execShellCmd("input tap " + x + " " + y);
        }
    }

    public static void center(AccessibilityService accessibilityService, AccessibilityNodeInfo rootNodeInfo, Rect rect) {
        if (rect != null) {
            Log.d("AccessibilityUtil", "left:" + rect.left + " top:" + rect.top + " right:" + rect.right + " bottom:" + rect.bottom);
            int x = rect.right / 2;
            int y = (rect.bottom - rect.top) / 2 + rect.top;
            ShellUtil.execShellCmd("input tap " + x + " " + y);
//        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription();
        }
    }


    public static void commonPerformByText(AccessibilityNodeInfo rootNodeInfo, String text) {
        List<AccessibilityNodeInfo> all = rootNodeInfo.findAccessibilityNodeInfosByText(text);
        if (all != null) {
            for (int i = 0; i < all.size(); i++) {
                AccessibilityNodeInfo node = all.get(i);
                if (node != null && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    public static void commonPerformById(AccessibilityNodeInfo rootNodeInfo, String id) {
        List<AccessibilityNodeInfo> all = rootNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (all != null) {
            for (int i = 0; i < all.size(); i++) {
                AccessibilityNodeInfo node = all.get(i);
                if (node != null && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    public static AccessibilityNodeInfo findViewByClassname(AccessibilityNodeInfo nodeInfo, String classname) {
        if (nodeInfo == null)
            return null;

        if (classname.equals(nodeInfo.getClassName().toString())) {
            return nodeInfo;
        }

        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = nodeInfo.getChild(i);
                if (child == null)
                    continue;

                AccessibilityNodeInfo res = findViewByClassname(child, classname);
                if (res != null)
                    return res;
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo getNodeInfoById(AccessibilityNodeInfo rootNodeInfo, @NonNull String id) {
        if (rootNodeInfo == null)
            return null;

        // 立即翻倍，观看视频 领取翻倍卡，立即领取
        if (id.equals(rootNodeInfo.getViewIdResourceName())) {
            return rootNodeInfo;
        }

        if (rootNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < rootNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = rootNodeInfo.getChild(i);
                if (child == null)
                    continue;

                AccessibilityNodeInfo res = getNodeInfoById(child, id);
                if (res != null)
                    return res;
            }
        }
        // 立即翻倍，观看视频 领取翻倍卡，立即领取
        return null;
    }

    public static AccessibilityNodeInfo findViewByDesc(AccessibilityNodeInfo nodeInfo, String desc) {
        if (nodeInfo == null)
            return null;

        if (desc.equals(nodeInfo.getContentDescription())) {
            return nodeInfo;
        }

        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = nodeInfo.getChild(i);
                if (child == null)
                    continue;

                AccessibilityNodeInfo res = findViewByDesc(child, desc);
                if (res != null)
                    return res;
            }
        }
        return null;
    }

    public static Rect getRect(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            Rect rect = new Rect();
            nodeInfo.getBoundsInScreen(rect);
            return rect;
        }
        return null;
    }


    public static boolean isAdShown(AccessibilityNodeInfo rootNodeInfo) {
        if(rootNodeInfo == null)
            return false;

        String text = rootNodeInfo.getText() == null ? null : rootNodeInfo.getText().toString();
        if (text != null) {
            if (text.contains("广告 0:"))
                return true;
        }
        if("com.songheng.eastnews:id/apo".equals(rootNodeInfo.getViewIdResourceName())){ // 广告倒计时
//          com.songheng.eastnews:id/apn 关闭按钮
            return true;
        }
        if("com.songheng.eastnews:id/tt_reward_ad_countdown".equals(rootNodeInfo.getViewIdResourceName())) { // 广告倒计时
//          com.songheng.eastnews:id/tt_video_ad_close 关闭按钮
            return true;
        }

        if (rootNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < rootNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = rootNodeInfo.getChild(i);
                if (child == null)
                    continue;

                boolean b = isAdShown(child);
                if (b)
                    return true;
            }
        }

        return false;
    }


    public static void closeAdByDesc(AccessibilityNodeInfo rootNodeInfo) {
//                com.songheng.eastnews 2048
//                ClassName:android.view.View Desc:大多数人知道微信可以聊天，却不知道微信可以这样赚钱！ 致富诀窍 Text:null ResId:null
//        ClassName:android.view.View Desc:90后美女闲时兼职做这个赚钱，收入超出工资两倍 致富诀窍 Text:null ResId:null
//                ClassName:android.widget.ImageButton Desc:null Text:null ResId:null
        String desc = "致富诀窍";
        AccessibilityNodeInfo descNode = AccessibilityUtil.findViewByContainsDesc2(rootNodeInfo, desc);
        if (descNode != null) {
            AccessibilityNodeInfo nodeInfo = AccessibilityUtil.findViewByClassname(rootNodeInfo, "android.widget.ImageButton");
            if (nodeInfo != null)
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    public static AccessibilityNodeInfo findViewByContainsDesc2(AccessibilityNodeInfo nodeInfo, String desc) {
        if (nodeInfo == null)
            return null;
        String text = (String) nodeInfo.getContentDescription();
        if (text != null && text.contains(desc)) {
            return nodeInfo;
        }

        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = nodeInfo.getChild(i);
                if (child == null)
                    continue;

                AccessibilityNodeInfo res = findViewByContainsDesc2(child, desc);
                if (res != null)
                    return res;
            }
        }
        return null;
    }

//    android.widget.Button Desc:null Text:取消 ResId:com.android.packageinstaller:id/cancel_button
public static AccessibilityNodeInfo findCancelButtonOnPackageinstaller(AccessibilityNodeInfo nodeInfo) {
    if (nodeInfo == null)
        return null;

    if (nodeInfo.getText() != null && nodeInfo.getText().toString().contains("取消")
            && "android.widget.Button".equals(nodeInfo.getClassName())
            && "com.android.packageinstaller:id/cancel_button".equals(nodeInfo.getViewIdResourceName())) {
        return nodeInfo;
    }

    if (nodeInfo.getChildCount() > 0) {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null)
                continue;

            AccessibilityNodeInfo res = findCancelButtonOnPackageinstaller(child);
            if (res != null)
                return res;
        }
    }
    return null;
}

}
