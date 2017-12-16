package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.czyugang.tcg.client.R;
import io.reactivex.annotations.Nullable;

/**
 * Created by ruiaa on 2016/10/30.
 */

public class LabelLayout extends FlowLayout {

    private boolean haveAdd=false;
    private int labelId= R.layout.view_label;
    private OnClickItemListener onClickItemListener=null;
    public TextView lastSelectTextView =null;

    public LabelLayout(Context context) {
        super(context);
    }

    public LabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelLayout);
        try {
            labelId=a.getResourceId(R.styleable.LabelLayout_child_layout,R.layout.view_label);
        } finally {
            a.recycle();
        }
    }

    public LabelLayout setLabelId(int labelId) {
        this.labelId = labelId;
        return this;
    }

    public void clear(){
        removeAllViews();
    }

    public void setTexts(String[] strings){
        if (strings==null) return;
        setTexts(Arrays.asList(strings));
    }

    public void setTexts(@Nullable List<String> list){
        if (list==null) return;
        if (haveAdd){
            removeAllViews();
        }else {
            haveAdd=true;
        }
        LayoutInflater inflater=LayoutInflater.from(getContext());
        for (String s:list){
            TextView textView=(TextView)inflater.inflate(labelId,this,false);
            textView.setText(s);
            textView.setOnClickListener(v -> {
                if (onClickItemListener!=null) onClickItemListener.onClick(((TextView)v).getText().toString(),(TextView)v);
                lastSelectTextView =(TextView) v;
            });
            if (lastSelectTextView==null) lastSelectTextView=textView;
            addView(textView);
        }
    }

    public static void setTextList(LabelLayout labelLayout, List<String> list){
        labelLayout.setTexts(list);
    }

    public static void setTextList(LabelLayout labelLayout, String[] strings){
        labelLayout.setTexts(strings);
    }

    public LabelLayout setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
        return this;
    }

    public static interface OnClickItemListener{
        void onClick(String text,TextView textView);
    }
}
