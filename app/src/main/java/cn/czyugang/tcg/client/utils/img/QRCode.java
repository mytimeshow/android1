package cn.czyugang.tcg.client.utils.img;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QRCode {

    private static Bitmap bitmap;

    /*传入字符串，生成二维码*/
    @Nullable
    public static Bitmap createQRImage(String url) {
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Map hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            //图像数据转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);
            int[] pixels = new int[300 * 300];
            //按照二维码的算法，逐个生成二维码的图片
            for (int y = 0; y < 300; y++) {
                for (int x = 0; x < 300; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * 300 + x] = 0xff000000;
                    } else {
                        pixels[y * 300 + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 300, 0, 0, 300, 300);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //返回二维码图片
        return bitmap;
    }
}
