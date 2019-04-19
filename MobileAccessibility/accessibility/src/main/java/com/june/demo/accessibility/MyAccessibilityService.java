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
        if(!"com.songheng.eastnews".equals(pkgName))
            return;

        int eventType = event.getEventType();
        Log.e("111111111111", pkgName + " " + eventType);

        AccessibilityNodeInfo rootNodeInfo
//                = event.getSource();
                = getRootInActiveWindow();
        listNodeInfo(rootNodeInfo);

        if (webRect == null) {
            AccessibilityNodeInfo webNode = AccessibilityUtil.findViewByClassname(rootNodeInfo, "android.webkit.WebView");
            if (webNode != null) {
                for (int i = 0; i < webNode.getChildCount(); i++) {
                    AccessibilityNodeInfo child = webNode.getChild(i);
                    if(child == null)
                        continue;

                    if("android.webkit.WebView".equals(child.getClassName())) {
                        Rect rect = AccessibilityUtil.getRect(webNode);
                        if (rect != null) {
                            webRect = rect;
                            Log.d("ddddd", "dddd web bounds " + rect);
                        }
                    }
                }
            }
        }

        if (dialogBtnRect == null) {
            // 对话框 立即领取，立即翻倍 left:150 top:1311 right:930 bottom:1449
            AccessibilityNodeInfo gainBtn = AccessibilityUtil.getNodeInfoById(rootNodeInfo, "gainBtn");
            if (gainBtn != null) {
                Rect rect = AccessibilityUtil.getRect(gainBtn);
                if (rect != null) {
                    dialogBtnRect = rect;
                    Log.d("ddddd", "dddd dialog btn bounds " + rect);
                }
            }
        }

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

//                AccessibilityUtil.clickAll(rootNodeInfo);
                break;

            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:

//                AccessibilityUtil.clickAll(rootNodeInfo);

                // 点击弹出框
                AccessibilityNodeInfo gainBtn = AccessibilityUtil.getNodeInfoById(rootNodeInfo, "gainBtn");
                if (gainBtn != null) {
                    gainBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }

                AccessibilityUtil.closeAdByDesc(rootNodeInfo);

                if (AccessibilityUtil.isAdShown(rootNodeInfo)) {
                    Log.d("ggggg", "正在播放广告。。。");

                } else {
                    if (webRect != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            MyAccessibility.clickCenter(this, webRect);
                        }
                    }
                }
                break;
        }

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
