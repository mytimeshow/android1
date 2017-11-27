package cn.czyugang.tcg.client.widget;

import android.app.Dialog;
import android.view.View;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class DialogHelper {

    public Dialog dialog;
    private String title = null;
    private String contentStr = null;
    private String oneButton = "我知道了";
    private String leftButton = "取消";
    private String rightButton = "确定";
    private View.OnClickListener oneButtonClick = null;
    private View.OnClickListener leftButtonClick = null;
    private View.OnClickListener rightButtonClick = null;

    public DialogHelper title(String title) {
        this.title = title;
        return this;
    }

    public DialogHelper contentStr(String contentStr) {
        this.contentStr = contentStr;
        return this;
    }

    public DialogHelper oneButton(String oneButton) {
        this.oneButton = oneButton;
        return this;
    }

    public DialogHelper leftButton(String leftButton) {
        this.leftButton = leftButton;
        return this;
    }

    public DialogHelper rightButton(String rightButton) {
        this.rightButton = rightButton;
        return this;
    }

    public DialogHelper onOneButton(View.OnClickListener oneButtonClick) {
        this.oneButtonClick = oneButtonClick;
        return this;
    }

    public DialogHelper onLeftButton(View.OnClickListener leftButtonClick) {
        this.leftButtonClick = leftButtonClick;
        return this;
    }

    public DialogHelper onRightButton(View.OnClickListener rightButtonClick) {
        this.rightButtonClick = rightButtonClick;
        return this;
    }

    public DialogHelper onOneButton() {
        this.oneButtonClick = v -> hide();
        return this;
    }

    public DialogHelper onLeftButton() {
        this.leftButtonClick = v -> hide();
        return this;
    }

    public DialogHelper onRightButton() {
        this.rightButtonClick = v -> hide();
        return this;
    }

    public DialogHelper build(){
        return this;
    }

    public DialogHelper show(){
        dialog.show();
        return this;
    }

    public DialogHelper hide() {
        dialog.dismiss();
        return this;
    }
}
