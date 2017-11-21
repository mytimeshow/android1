package cn.czyugang.tcg.client.utils.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.string.StringUtil;

/**
 * Created by ruiaa on 2016/11/6.
 */

public class FileStorage {

    private static Context context;

    public static void register(Context appContext){
        context=appContext;
    }

    /*
     *      缓存/保存 地址
     */
    @NonNull
    public static String getImgCacheDir() {
        return checkOrCreateDir(context.getFilesDir().getAbsolutePath() +"/app_imgCache");
        //return checkOrCreateDir(Environment.getExternalStorageDirectory() +"/"+ ResUtil.getString(R.string.app_name)+"/test_imgCache");
    }

    public static String getAppExternalDir(){
        return checkOrCreateDir(Environment.getExternalStorageDirectory() +"/"+ ResUtil.getString(R.string.app_name));
    }

    public static String getSplashImgPath(){
        return checkOrCreateDir(context.getFilesDir().getAbsolutePath() +"/splash")
                +"/"+"s.jpg";
    }
    /*
     *      清除缓存
     */

    public static String getImgCacheSize(){
        return getDirSize(getImgCacheDir());
    }

    public static void cleanImgCache() {
        File file = new File(getImgCacheDir());
        deleteFilesInDir(file);
    }

    //保存图片
    public static void saveImgExternalStorage(Bitmap bmp, Context context,String fileName){
        File appDir = new File(Environment.getExternalStorageDirectory(), ResUtil.getString(R.string.app_name));
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            //bmp.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            Uri uri = Uri.fromFile(file);
            // 通知图库更新
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            context.sendBroadcast(scannerIntent);
        }catch (Exception e){
            LogRui.e("saveImgExternalStorage####",e);
        }
    }

    public static void saveImg(Context context, Bitmap bmp, String path) throws IOException {
        if (path==null||path.isEmpty()) return;

        File file = new File(path);

        FileOutputStream outputStream = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
    }



    /*
     *      创建
     */
    public static String checkOrCreateDir(String dir) {
        if (dir == null) return dir;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean saveFile(File file, InputStream inputStream){
        if (file==null) return false;
        if (inputStream==null) return false;

        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[4096];
            outputStream = new FileOutputStream(file);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            return true;

        } catch (IOException e) {
            LogRui.e("saveFile--",e);
            return false;


        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }catch (IOException e){
                LogRui.e("saveFile--",e);
            }
        }
    }

    /*
     *      移动或复制
     */
    private static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove) {
        if (srcFile == null || destFile == null) return false;
        // 源文件不存在或者不是文件则返回false
        if (!srcFile.exists() || !srcFile.isFile()) return false;
        // 目标文件存在且是文件则返回false
        if (destFile.exists() && destFile.isFile()) return false;
        // 目标目录不存在返回false
        if (!createOrExistsDir(destFile.getParentFile())) return false;
        try {
            return writeFileFromIS(destFile, new FileInputStream(srcFile), false) && !(isMove && !deleteFile(srcFile));
        } catch (FileNotFoundException e) {
           LogRui.e("copyOrMoveFile--",e);
            return false;
        }
    }

    public static boolean copyFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    public static boolean copyFile(String srcFilePath, String destFilePath) {
        return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    /*
     *      删除
     */
    public static boolean deleteFilesInDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }


    /*
     *      读写
     */
    public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
        if (file == null || is == null) return false;
        if (!createOrExistsFile(file)) return false;
        OutputStream os = null;
        try {
            if (append){
                os = new BufferedOutputStream(new FileOutputStream(file, append));
            }else {
                os=new FileOutputStream(file);
            }
            byte data[] = new byte[1024];
            int len;
            while ((len = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            LogRui.e("writeFileFromIS--",e);
            return false;
        } finally {
            CloseUtils.closeIO(is, os);
        }
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(final String filePath) {
        return StringUtil.isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(final String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }
    /**
     * 判断是否是目录
     *
     * @param dirPath 目录路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * 判断是否是目录
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDir(final File file) {
        return isFileExists(file) && file.isDirectory();
    }

    /**
     * 判断是否是文件
     *
     * @param filePath 文件路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(final String filePath) {
        return isFile(getFileByPath(filePath));
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(final File file) {
        return isFileExists(file) && file.isFile();
    }

    /**
     * 获取目录大小
     *
     * @param dirPath 目录路径
     * @return 文件大小
     */
    public static String getDirSize(final String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }

    /**
     * 获取目录大小
     *
     * @param dir 目录
     * @return 文件大小
     */
    public static String getDirSize(final File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小
     */
    public static String getFileSize(final String filePath) {
        return getFileSize(getFileByPath(filePath));
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    public static String getFileSize(final File file) {
        long len = getFileLength(file);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    @SuppressLint("DefaultLocale")
    private static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format("%.2fB", (double) byteNum + 0.0005);
        } else if (byteNum < 1048576) {
            return String.format("%.2fKB", (double) byteNum / 1024 + 0.0005);
        } else if (byteNum < 1073741824) {
            return String.format("%.2fMB", (double) byteNum / 1048576 + 0.0005);
        } else {
            return String.format("%.2fGB", (double) byteNum / 1073741824 + 0.0005);
        }
    }

    /**
     * 获取目录长度
     *
     * @param dirPath 目录路径
     * @return 目录长度
     */
    public static long getDirLength(final String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }

    /**
     * 获取目录长度
     *
     * @param dir 目录
     * @return 目录长度
     */
    public static long getDirLength(final File dir) {
        if (!isDir(dir)) return -1;
        long len = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    len += getDirLength(file);
                } else {
                    len += file.length();
                }
            }
        }
        return len;
    }

    /**
     * 获取文件长度
     *
     * @param filePath 文件路径
     * @return 文件长度
     */
    public static long getFileLength(final String filePath) {
        return getFileLength(getFileByPath(filePath));
    }

    /**
     * 获取文件长度
     *
     * @param file 文件
     * @return 文件长度
     */
    public static long getFileLength(final File file) {
        if (!isFile(file)) return -1;
        return file.length();
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    public static class CloseUtils {

        private CloseUtils() {
            throw new UnsupportedOperationException("u can't instantiate me...");
        }

        /**
         * 关闭IO
         *
         * @param closeables closeable
         */
        public static void closeIO(Closeable... closeables) {
            if (closeables == null) return;
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 安静关闭IO
         *
         * @param closeables closeable
         */
        public static void closeIOQuietly(Closeable... closeables) {
            if (closeables == null) return;
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }
}
