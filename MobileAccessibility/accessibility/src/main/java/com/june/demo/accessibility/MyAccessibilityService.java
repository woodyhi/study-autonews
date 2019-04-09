package com.june.demo.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = MyAccessibilityService.class.getSimpleName();

    private Rect webRect;

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
        int eventType = event.getEventType();
        Log.e("111111111111", pkgName + " " + eventType);

        AccessibilityNodeInfo rootNodeInfo
//                = event.getSource();
                = getRootInActiveWindow();
        listNodeInfo(rootNodeInfo);

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                AccessibilityUtil.clickAll(rootNodeInfo);
                break;

            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                AccessibilityNodeInfo webviewNode = findWebView(rootNodeInfo);
                if (webviewNode != null) {
                    Log.e(TAG, "开始遍历webview中的view");
                    getWebNode(webviewNode);
                }

                AccessibilityUtil.clickAll(rootNodeInfo);

                break;
        }

    }

    private void listNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null)
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
                if("gainBtn".equals(child.getViewIdResourceName())) {
                    Rect rect = new Rect();
                    child.getBoundsInScreen(rect);
//                    child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d("ddddd", "left:" + rect.left + " top:" + rect.top + " right:" + rect.right + " bottom:" + rect.bottom);
                }
            }
        }
    }

    private AccessibilityNodeInfo findWebView(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null)
            return null;

        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null)
                continue;

            if ("android.webkit.WebView".equals(child.getClassName().toString())) {
                Log.d(TAG, "找到了");
                return child;
            } else {
                if (child.getChildCount() > 0)
                    return findWebView(child);
            }
        }
        return null;
    }

    private AccessibilityNodeInfo getWebNode(AccessibilityNodeInfo nodeInfo) {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null)
                continue;

            if(webRect == null){
                Rect tmp = new Rect();
                child.getBoundsInScreen(tmp);
                if(! (tmp.left == 0 && tmp.top == 0 && tmp.right == 0 && tmp.bottom == 0)){
                    webRect = tmp;
                }
            }


            Log.e("bbbbbbbb", child.getClassName() + " " + child.getContentDescription() + " " + child.getText() + " " + child.getViewIdResourceName()
                    + " " + webRect.left + " " + webRect.top + " " + webRect.right + " " + webRect.bottom);
        }
        return null;
    }

    @Override
    public void onInterrupt() {

    }

}
