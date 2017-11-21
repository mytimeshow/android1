package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class SelectButton extends RadioButton {
    public SelectButton(Context context) {
        super(context);
    }

    public SelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
