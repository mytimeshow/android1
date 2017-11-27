package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class ActivityTextView extends FrameLayout {

    private TextView typeT;
    private TextView textT;

    public ActivityTextView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ActivityTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        parseAttr(context, attrs);
    }

    public ActivityTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        parseAttr(context, attrs);
    }

    private void init(@NonNull Context context){
        LayoutInflater.from(context).inflate(R.layout.view_activity_text,this,true);
        textT=findViewById(R.id.view_activity_text);
        typeT=findViewById(R.id.view_activity_type);
    }

    private void parseAttr(@NonNull Context context,@Nullable AttributeSet attrs){
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ActivityTextView);
        String text=mTypedArray.getString(R.styleable.ActivityTextView_activity_text);
        String type=mTypedArray.getString(R.styleable.ActivityTextView_activity_type);
        setType(type);
        setText(text);
        mTypedArray.recycle();
    }

    public void setType(String type){
        if (type==null) return;
        typeT.setText(type);
        typeT.setBackgroundResource(R.color.main_light_red);
    }

    public void setText(String text){
        if (text==null) return;
        textT.setText(text);
    }

    public void setText(@ColorRes int color,@DimenRes int size){
        textT.setTextSize(TypedValue.COMPLEX_UNIT_PX,ResUtil.getDimenInPx(size));
        textT.setTextColor(ResUtil.getColor(color));
    }

    public static ActivityTextView create(Context context,String type,String text){
        ActivityTextView t=new ActivityTextView(context);
        t.setText(text);
        t.setType(type);
        return t;
    }

    public static ActivityTextView create(ViewGroup parent, String type, String text, @DimenRes int marginTop){
        ActivityTextView t=create(parent.getContext(), type, text);
        parent.addView(t);
        ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams)t.getLayoutParams();
        lp.topMargin= ResUtil.getDimenInPx(marginTop);
        t.setLayoutParams(lp);
        return t;
    }
}
