package cn.czyugang.tcg.client.utils.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

public class ImgView extends ImageView {
/*    public ImgView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }*/

    public ImgView(Context context) {
        super(context);
        setScaleType(ScaleType.FIT_XY);
    }

    public ImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.FIT_XY);
        parse(context, attrs);
    }

    public ImgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.FIT_XY);
        parse(context, attrs);
    }

    private void parse(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImgView);
        try {
            isRound = a.getBoolean(R.styleable.ImgView_round, false);
            noCache = a.getBoolean(R.styleable.ImgView_noCache, false);
        } finally {
            a.recycle();
        }
    }

    private Paint paint = null;
    private String disCount = "";
    private boolean isRound = false;
    private boolean noCache = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (disCount != null) {
            if (paint == null) {
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(ResUtil.getDimenInPx(R.dimen.sp_10));
            }
            if (disCount!=null&&!disCount.isEmpty()){
                paint.setColor(0x7F000000);
                canvas.drawRect(0, getHeight() * 0.75f, getWidth(), getHeight(), paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(disCount, getWidth() / 2, getHeight() * 0.94f, paint);
            }
        }
    }



    /*
    *       图片链接
    * */
    public void url(String url) {
        //setImageURI(url);
        glide(url);
    }

    public void id(String imgId) {
        //ImageLoader.loadImageToView(imgId,this);
        if (getWidth() != 0 && getWidth() != 0) {
            url(ImageLoader.getImageUrl(imgId, getWidth(), getHeight()));
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    url(ImageLoader.getImageUrl(imgId, getWidth(), getHeight()));
                }
            });
        }
    }

    public void imgFile(String path) {
        //setImageURI("file://"+path);
        glide(path);
    }

    public void videoFile(String path) {
        glide(Uri.fromFile(new File(path)));
    }

    public void drawableId(@DrawableRes int resId) {
        //setImageURI("res://drawable/" + resId);
        glide(resId);
    }

    public void mipmapId(@DrawableRes int resId) {
        //setImageURI("res://mipmap/" + resId);
        glide(resId);
    }

    /*
    *   样式
    * */

    public ImgView setDisCount(String disCount) {
        this.disCount = disCount;
        return this;
    }

    public ImgView setRound(boolean round) {
        isRound = round;
        return this;
    }

    public ImgView setNoCache(boolean noCache) {
        this.noCache = noCache;
        return this;
    }

    //glide
    private void glide(Object url) {
        GlideRequest<Drawable> request = GlideApp.with(this).load(url);
        if (noCache) request.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
        if (isRound) request.apply(RequestOptions.bitmapTransform(new CircleCrop()));
        request.transition(new DrawableTransitionOptions().crossFade(200)).into(this);
    }
}
