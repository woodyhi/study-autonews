package com.june.demo.accessibility;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class ShellUtil {

    /**
     * 执行shell命令
     *
     * @param cmd
     */
    public static void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
//            process.waitFor();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void instrument(final int x, final int y) {
        new Thread(() -> {
            Instrumentation inst = new Instrumentation();
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis();
            MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
            MotionEvent event2 = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
            inst.sendPointerSync(event);
            inst.sendPointerSync(event2);
        }).start();
    }
}
