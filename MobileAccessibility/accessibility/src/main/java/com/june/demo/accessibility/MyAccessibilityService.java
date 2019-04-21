package com.june.demo.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = MyAccessibilityService.class.getSimpleName();

    // webview bounds
    private Rect webRect;

    // dialog bounds
    private Rect dialogBtnRect;

    private boolean isH5Shown;

    public MyAccessibilityService() {
    }

    @Override
    protected void onServiceConnected() {
//        动态设置 4.0开始增加meta-data方式
//        AccessibilityServiceInfo info = getServiceInfo();
//        info.packageNames = new String[]{};
//        info.flags = info.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
//        setServiceInfo(info);

        super.onServiceConnected();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String pkgName = event.getPackageName().toString();
        if (!"com.songheng.eastnews".equals(pkgName))
            return;

        int eventType = event.getEventType();
        Log.e("1111111111", pkgName + " " + eventType + " " + event.getClassName() + " " + isH5Shown + " " + webRect);

        AccessibilityNodeInfo rootNodeInfo
//                = event.getSource();
                = getRootInActiveWindow();
        listNodeInfo(rootNodeInfo);

        AccessibilityNodeInfo btn = AccessibilityUtil.getNodeInfoById(rootNodeInfo, "com.songheng.eastnews:id/apm");
        if (btn != null) {
            Log.e("ccccc", "dianji anni........");
            btn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }


        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                isH5Shown = "com.songheng.eastfirst.business.nativeh5.view.activity.CommonH5Activity".equals(event.getClassName());
                if (isH5Shown) {
                    if (webRect == null) {
                        AccessibilityNodeInfo webNode = AccessibilityUtil.findViewByClassname(rootNodeInfo, "android.webkit.WebView");
                        if (webNode != null) {
                            for (int i = 0; i < webNode.getChildCount(); i++) {
                                AccessibilityNodeInfo child = webNode.getChild(i);
                                if (child == null)
                                    continue;

                                if ("android.webkit.WebView".equals(child.getClassName())) {
                                    Rect rect = AccessibilityUtil.getRect(webNode);
                                    if (rect != null) {
                                        webRect = rect;
                                        Log.d("ddddd", "dddd web bounds " + rect);
                                    }
                                }
                            }
                        }
                    }

                }

                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if (isH5Shown) {
                    handleEventWhenH5(rootNodeInfo);
                }

                break;
        }

    }


    //        if (dialogBtnRect == null) {
//             对话框 立即领取，立即翻倍 left:150 top:1311 right:930 bottom:1449
//            AccessibilityNodeInfo gainBtn = AccessibilityUtil.getNodeInfoById(rootNodeInfo, "gainBtn");
//            if (gainBtn != null) {
//                Rect rect = AccessibilityUtil.getRect(gainBtn);
//                if (rect != null) {
//                    dialogBtnRect = rect;
//                    Log.d("ddddd", "dddd dialog btn bounds " + rect);
//                }
//            }
//        }

    private void handleEventWhenH5(AccessibilityNodeInfo rootNodeInfo) {
        // 取消 安装
        AccessibilityNodeInfo cancel = AccessibilityUtil.findCancelButtonOnPackageinstaller(rootNodeInfo);
        if (cancel != null) {
            cancel.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d("ddddd", "取消安装。。。");
            return;
        }

        // 弹出框
        AccessibilityNodeInfo gainBtn = AccessibilityUtil.getNodeInfoById(rootNodeInfo, "gainBtn");
        if (gainBtn != null) {
            gainBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d("ddddd", "弹出框。。。");
            return;
        }

        // 弹出框：继续努力
        AccessibilityNodeInfo gainBtn1 = AccessibilityUtil.getNodeInfoById(rootNodeInfo, "gainBtn1");
        if (gainBtn1 != null) {
            gainBtn1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d("ddddd", "弹出框1。。。");
            return;
        }

        AccessibilityUtil.clickAll(rootNodeInfo);
        AccessibilityUtil.closeAdByDesc(rootNodeInfo);

        if (AccessibilityUtil.isAdShown(rootNodeInfo)) {
            Log.d("ggggg", "正在播放广告。。。");

        } else {
            if (isH5Shown && webRect != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d("ddddd", "点击中心领取金币。。。");
                    MyAccessibility.clickCenter(this, webRect);
                }
            }
        }
    }

    private void handleEventWhenInstall(AccessibilityNodeInfo rootNodeInfo) {

    }

    // 遍历所有
    private void listNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null)
            return;

        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null)
                continue;

            if (child.getChildCount() > 0) {
                listNodeInfo(child);
            } else {
                Log.e("cccccc", "ClassName:" + child.getClassName() + " Desc:" + child.getContentDescription() + " Text:" + child.getText() + " ResId:" + child.getViewIdResourceName());

                // 立即翻倍，观看视频 领取翻倍卡，
                if ("gainBtn".equals(child.getViewIdResourceName())) {
                    Rect rect = new Rect();
                    child.getBoundsInScreen(rect);
//                    child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d("ddddd", "left:" + rect.left + " top:" + rect.top + " right:" + rect.right + " bottom:" + rect.bottom);
                }
            }
        }
    }


    @Override
    public void onInterrupt() {

    }

}
