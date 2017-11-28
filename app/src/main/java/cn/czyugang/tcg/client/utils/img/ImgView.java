package cn.czyugang.tcg.client.utils.img;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.app.ResUtil;

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

    private Paint paint=null;
    private String disCount="七折";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (disCount!=null){
            if (paint==null){
                paint=new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(ResUtil.getDimenInPx(R.dimen.sp_10));
            }
            paint.setColor(0x7F000000);
            canvas.drawRect(0,getHeight()*0.75f,getWidth(),getHeight(),paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(disCount,getWidth()/2,getHeight()*0.94f,paint);
        }
    }

    public void url(String url){
        setImageURI(url);
    }

    public void id(String imgId){
        ImageLoader.loadImageToView(imgId,this);
    }


}
