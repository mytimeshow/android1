package cn.czyugang.tcg.client.utils.string;

import android.util.Base64;

import java.security.MessageDigest;

/**
 * Created by wuzihong on 2017/9/20.
 * 加密工具类（Base64、MD5）
 */

public class EncryptUtils {
    /**
     * Base64编码
     *
     * @param string
     * @return
     */
    public static String encodeBase64(String string) {
        try {
            return Base64.encodeToString(string.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Base64解码
     *
     * @param string
     * @return
     */
    public static String decodeBase64(String string) {
        try {
            return new String(Base64.decode(string, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hex = md5.digest(string.getBytes("UTF-8"));
            String hexString = "";
            for (byte b : hex) {
                int temp = b & 255;
                if (temp < 16 && temp >= 0) {
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对密码进行加密
     *
     * @param password
     * @param timestamp
     * @return
     */
    public static String password(String password, String timestamp) {
        return encodeBase64(timestamp.replaceAll("[0-4]", "") + "k1#" + md5(password) + "#k2" + timestamp.replaceAll("[5-9]", ""));
    }

    /**
     * 生成校验码
     *
     * @param dynamicCode
     * @return
     */
    public static String checkCode(String dynamicCode) {
        return md5(new StringBuilder(("TCG@2017#check#code" + dynamicCode).replaceAll("[^0-9a-zA-Z]", "")).reverse().toString());
    }
}
