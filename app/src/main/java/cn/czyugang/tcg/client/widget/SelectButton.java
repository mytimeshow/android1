package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;

import cn.czyugang.tcg.client.R;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class SelectButton extends RadioButton {

    private int bgDrawable=R.drawable.selector_checkbox_red;

    public SelectButton(Context context) {
        super(context);
        init();
    }

    public SelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parse(context, attrs);
        init();
    }

    public SelectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parse(context, attrs);
        init();
    }

    private void parse(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectButton);
        try {
            bgDrawable=a.getResourceId(R.styleable.SelectButton_bg_drawable,R.drawable.selector_checkbox_red);
        } finally {
            a.recycle();
        }
    }

    private void init(){
        setButtonDrawable(bgDrawable);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
