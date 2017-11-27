package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import cn.czyugang.tcg.client.R;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class SelectButton extends RadioButton {
    public SelectButton(Context context) {
        super(context);
        init();
    }

    public SelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setButtonDrawable(R.drawable.selector_checkbox_red);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
