package cn.czyugang.tcg.client.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by wuzihong on 2017/10/18.
 * PopupWindow 基类
 */

public class BasePopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    private final float ALPHA = 0.5f;
    protected Context context;
    protected Activity activity;
    private OnDismissListener mListener;

    public BasePopupWindow(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);
        super.setOnDismissListener(this);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        setBackgroundAlpha(ALPHA);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        setBackgroundAlpha(ALPHA);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        setBackgroundAlpha(ALPHA);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setBackgroundAlpha(ALPHA);
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mListener = onDismissListener;
    }

    private void setBackgroundAlpha(float alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
        if (mListener != null) {
            mListener.onDismiss();
        }
    }
}
