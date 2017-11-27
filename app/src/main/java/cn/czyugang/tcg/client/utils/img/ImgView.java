package cn.czyugang.tcg.client.utils.img;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

public class ImgView extends SimpleDraweeView {
    public ImgView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public ImgView(Context context) {
        super(context);
    }

    public ImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void url(String url){
        setImageURI(url);
    }

    public void id(String imgId){
        ImageLoader.loadImageToView(imgId,this);
    }
}
