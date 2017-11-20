package cn.czyugang.tcg.client.utils;


import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.czyugang.tcg.client.BuildConfig;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.utils.storage.FileStorage;
import cn.czyugang.tcg.client.utils.string.TimeUtils;

/**
 * Created by ruiaa on 2016/9/29.
 */

public class LogUtil {

    public static final String USER_NAME = "ruiaa---";
    public static final boolean LOG_OPEN = BuildConfig.LOG_OPEN;
    private static final boolean LOG_FILE_OPEN = false;
    private static final boolean LOG_TOAST = false;

    public static void v(String msg) {
        if (LOG_OPEN) {
            Log.v(USER_NAME + getClassName(), msg);
        }
    }

    public static void d(String msg) {
        d("", msg);
    }

    public static void d(String method, Object msg) {
        if (LOG_OPEN) {
            Log.d(USER_NAME + getClassName(), method + " " + (msg == null ? "null" : msg.toString()));
        }
    }

    public static void i(Object msg) {
        i("", msg);
    }

    public static void i(String method, Object msg) {
        if (LOG_OPEN) {
            String log = method + "" + (msg == null ? "null" : msg.toString());
            /*if (log.length()>2000){
                Log.i(USER_NAME + getClassName(),"超长开始"+log.length());
                for(int i=0;i<log.length();i+=2000){
                    if(i+2000<log.length())
                        Log.i(USER_NAME + getClassName(),log.substring(i, i+2000));
                    else
                        Log.i(USER_NAME + getClassName(),log.substring(i, log.length()));
                }
                Log.i(USER_NAME + getClassName(),"超长结束");
            }else {
                Log.i(USER_NAME + getClassName(), log);
            }*/

            Log.i(USER_NAME + getClassName(), log);
            saveMyLogToFlie(USER_NAME + getClassName(), method + "" + (msg == null ? "null" : msg.toString()));
        }
    }

    public static void w(String msg) {
        w("", msg);
    }

    public static void w(String method, Object msg) {
        if (LOG_OPEN) {
            Log.w(USER_NAME + getClassName(), method + " " + (msg == null ? "" : msg.toString()));
            saveMyLogToFlie(USER_NAME + getClassName(), method + " " + (msg == null ? "" : msg.toString()));
        }
    }

    public static void e(String msg) {
        if (LOG_OPEN) {
            Log.e(USER_NAME + getClassName(), msg);
            saveMyLogToFlie(USER_NAME + getClassName(), msg);
        }
    }

    public static void e(String msg, Throwable throwable) {
        if (LOG_OPEN) {
            String log = "";
            if (throwable == null) {
                log = "null";
            } else {
                log = throwable.getMessage();
            }
            Log.e(USER_NAME + getClassName(), msg + log);
            saveMyLogToFlie(USER_NAME + getClassName(), msg + log);
        }
    }

    /**
     * @return 当前的类名(simpleName)
     */
    private static String getClassName() {

        String result;
        StackTraceElement thisMethodStack = Thread.currentThread().getStackTrace()[4];
        result = thisMethodStack.getClassName();
        if (result.contains("LogUtil")) {
            thisMethodStack = Thread.currentThread().getStackTrace()[5];
            result = thisMethodStack.getClassName();
        }

        //获取类名
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1);
        int i = result.indexOf("$");// 剔除匿名内部类名
        result = (i == -1 ? result : result.substring(0, i));

        //获取行号
        String lineNumber = String.valueOf(thisMethodStack.getLineNumber());


        result = "(" + result + ".java:" + lineNumber + ")";
        while (result.length() < 40) {
            result = "-" + result;
        }
        return result;

    }

    /**
     * 打印 Log 行数位置
     */
    private static String log(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[5];
        String className = targetElement.getClassName();
        className = className.substring(className.lastIndexOf('.') + 1) + ".java";
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) lineNumber = 0;
        return "(" + className + ":" + lineNumber + ") " + message;
    }

    public static void showLogCat(String tag, String msg) {

        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo("showLogCat") == 0) {
                currentIndex = i + 1;
                break;
            }
        }

        String fullClassName = stackTraceElement[currentIndex].getClassName();
        String className = fullClassName.substring(fullClassName
                .lastIndexOf(".") + 1);
        String methodName = stackTraceElement[currentIndex].getMethodName();
        String lineNumber = String
                .valueOf(stackTraceElement[currentIndex].getLineNumber());

        Log.i(tag, msg);
        Log.i(tag + " position", "at " + fullClassName + "." + methodName + "("
                + className + ".java:" + lineNumber + ")");

    }

    public static void printStack() {
        StackTraceElement[] thisMethodStack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : thisMethodStack) {
            if (element != null) {
                String className = element.getClassName();
                String methodName = element.getMethodName();
                String lineNumber = String.valueOf(element.getLineNumber());
                lineNumber = "(" + className + ".java:" + lineNumber + ")";
                lineNumber = ">" + lineNumber;
                while (lineNumber.length() < 80) {
                    lineNumber = "-" + lineNumber;
                }
                lineNumber = lineNumber + methodName;
                Log.e(USER_NAME, lineNumber);
            }
        }
    }

    public static void saveAllLogToFile() {
        saveAllLogTOFile(1);
        saveAllLogTOFile(3);
    }

    private static void saveAllLogTOFile(int level) {
        String levelStr = "-d";
        switch (level) {
            case 0: {
                levelStr = "-v";
                break;
            }
            case 1: {
                levelStr = "-d";
                break;
            }
            case 2: {
                levelStr = "-i";
                break;
            }
            case 3: {
                levelStr = "-w";
                break;
            }
            case 4: {
                levelStr = "-e";
                break;
            }
        }
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            Process process = Runtime.getRuntime().exec("logcat " + levelStr);

            File logFile = new File(FileStorage.getAppExternalDir(), "日志" + levelStr + ".txt");
            outputStream = new FileOutputStream(logFile);
            inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes, 0, 1024)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();

        } catch (Exception e) {
            LogUtil.e("saveAllLogToFile####", e);
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                LogUtil.e("saveAllLogToFile####", e);
            }
        }
    }

    public static void saveMyLogToFlie(String tag, String msg) {
        if (!LOG_FILE_OPEN) return;
        if (tag == null || tag.isEmpty()) return;

        if (LOG_TOAST)
            Toast.makeText(MyApplication.getContext(), tag + msg, Toast.LENGTH_SHORT).show();


        String log = tag + (msg == null ? "null" : msg);
        long time = System.currentTimeMillis();
        log = "\n" + TimeUtils.milliseconds2String(time) + "    " + (time % 1000) + "    " + log;

        File logFile = new File(FileStorage.getAppExternalDir(), TimeUtils.transfer(System.currentTimeMillis(),"yyyy-MM-dd") + "-mylog.txt");
        FileStorage.createOrExistsFile(logFile);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(log);
            writer.flush();
        } catch (Exception e) {
            LogUtil.e("saveMyLog####", e);
        } finally {
            FileStorage.CloseUtils.closeIO(writer);
        }
    }
}
