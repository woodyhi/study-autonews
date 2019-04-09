package com.june.myxposedplugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class A implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("=========Loaded app: " + lpparam.packageName);


        if (lpparam.packageName.equals("com.songheng.eastnews")) {

//            Class clazz = lpparam.classLoader.loadClass("com.songheng.eastfirst.common.view.activity.WebViewActivity");
//            Field[] fields = clazz.getDeclaredFields();
//            XposedBridge.log("------------------------");
//            for (Field field : fields) {
//                XposedBridge.log(field.getName() + " " + field.getType().getName());
//            }




//            XposedHelpers.findAndHookMethod(clazz, "toastMessage", new XC_MethodHook() {
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
//                }
//
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    param.setResult("你已被劫持");
//                }
//            });

            hook(lpparam);
        }
    }

    private Object webview;

    // 大转盘
//    https://resources.dftoutiao.com/appfe/dftt-turn-table/index.html?fr=syfc
    private void hook(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, InterruptedException {

        Thread.sleep(5000); // 因为classnotfound等待加载
        //            com.songheng.eastfirst.common.view.widget.webview.CurlX5WebView
//            com.songheng.eastfirst.common.view.widget.webview.CurlWebView

        final Class clazz = lpparam.classLoader.loadClass("com.songheng.eastfirst.business.nativeh5.view.widget.CommonWebView");

        XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("开启webview degug");
                XposedHelpers.callStaticMethod(clazz, "setWebContentsDebuggingEnabled", true);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                try {
                    XposedBridge.log("执行javascript代码");
                    XposedBridge.log(param.thisObject.getClass().getName());
                    webview = param.thisObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            XposedBridge.log("method name : " + method.getName());
        }


        if(webview != null){
            XposedHelpers.callMethod(webview, "loadUrl", "javascript:document.getElementsByClassName('go_img')[0].click(); alert('hello');");
        }
    }

    private void hook1(){
        final List<Class> classes = new ArrayList<>();
        XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(param.hasThrowable())
                    return;

                String classname = (String) param.args[0];
                if (classes.contains(classname)) {
                    XposedBridge.log("hook class : " + classname);
                    Class<?> clazz = (Class<?>) param.getResult();
                    XposedBridge.log("" + clazz.getName());
//                    XposedHelpers.findAndHookMethod(clazz, "methodName", paramTypes, new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            super.beforeHookedMethod(param);
//                        }
//
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            super.afterHookedMethod(param);
//                        }
//                    });
                }
            }
        });
    }
}
