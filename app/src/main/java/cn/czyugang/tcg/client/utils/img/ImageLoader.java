package cn.czyugang.tcg.client.utils.img;

import android.text.TextUtils;
import android.view.ViewTreeObserver;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.string.EncryptUtils;

/**
 * Created by wuzihong on 2017/10/24.
 */
public class ImageLoader {
    public static void loadImageToView(String fileId, SimpleDraweeView view) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (width != 0 && height != 0) {
            view.setImageURI(getImageUrl(fileId, width, height));
        } else {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    view.setImageURI(getImageUrl(fileId, view.getWidth(), view.getHeight()));
                }
            });
        }
    }

    /**
     * 获取图片请求链接
     *
     * @param fileId
     * @param width
     * @param height
     * @return
     */
    public static String getImageUrl(String fileId, int width, int height) {
        if (TextUtils.isEmpty(fileId)) {
            return null;
        }
        return Config.getApiUrl() +
                "api/auth/v1/file/file?id=" + fileId +
                "&w=" + width +
                "&h=" + height +
                "&watermark=NO&code=" + EncryptUtils.md5(fileId + "w" + width + "h" + +height + "NO");
    }

    public static String getImageUrl(String fileId) {
        return getImageUrl(fileId, ResUtil.getWidthInPx(),ResUtil.getHeightInPx());
    }
}
