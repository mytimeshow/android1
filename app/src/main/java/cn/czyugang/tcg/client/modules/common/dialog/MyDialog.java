package cn.czyugang.tcg.client.modules.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.QRCode;

/**
 * @author ruiaa
 * @date 2017/11/29
 */

public class MyDialog extends DialogFragment {

    private Builder builder;
    private View rootView;
    private View.OnClickListener toDismissListener = null;
    private BindView bindView = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(builder.canceledOnTouchOutside);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(builder.width, builder.height);
        window.setGravity(builder.gravity);
        if (builder.offsetX != 0 && builder.offsetY != 0) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.x = builder.offsetX;
            lp.y = builder.offsetY;
            window.setAttributes(lp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (builder.layoutId > 0) {
            initCustom(inflater, container);
        } else if (builder.title == null) {
            initMsg(inflater, container);
        } else {
            initTitleMsg(inflater, container);
        }
        if (bindView != null) bindView.bindView(this);
        return rootView;
    }

    public MyDialog show() {
        super.show(builder.activity.getSupportFragmentManager(), builder.createTime);
        return this;
    }

    private void initMsg(LayoutInflater inflater, @Nullable ViewGroup container) {
        rootView = inflater.inflate(R.layout.dialog_message, container);
        text(R.id.tv_message, builder.contentStr);
        text(R.id.tv_negative, builder.negativeButton);
        text(R.id.tv_positive, builder.positiveButton);
        onClick(R.id.tv_negative, v -> {
            if (builder.negativeButtonClick != null){
                builder.negativeButtonClick.onClick(this);
            }else {
                dismiss();
            }
        });
        onClick(R.id.tv_positive,  v -> {
            if (builder.positiveButtonClick != null){
                builder.positiveButtonClick.onClick(this);
            }else {
                dismiss();
            }
        });
    }

    private void initTitleMsg(LayoutInflater inflater, @Nullable ViewGroup container) {
        rootView = inflater.inflate(R.layout.dialog_title_message, container);
        text(R.id.tv_title, builder.title);
        text(R.id.tv_message, builder.contentStr);
        text(R.id.tv_negative, builder.negativeButton);
        text(R.id.tv_positive, builder.positiveButton);
        onClick(R.id.tv_negative,  v -> {
            if (builder.negativeButtonClick != null){
                builder.negativeButtonClick.onClick(this);
            }else {
                dismiss();
            }
        });
        onClick(R.id.tv_positive,  v -> {
            if (builder.positiveButtonClick != null){
                builder.positiveButtonClick.onClick(this);
            }else {
                dismiss();
            }
        });
    }

    private void initCustom(LayoutInflater inflater, @Nullable ViewGroup container) {
        rootView = inflater.inflate(builder.layoutId, container);
    }

    public MyDialog onClick(int id, View.OnClickListener onClickListener) {
        rootView.findViewById(id).setOnClickListener(onClickListener);
        return this;
    }

    public MyDialog onClick(int id) {
        if (toDismissListener == null) toDismissListener = v -> dismiss();
        rootView.findViewById(id).setOnClickListener(toDismissListener);
        return this;
    }

    public MyDialog text(int id, String str) {
        ((TextView) rootView.findViewById(id)).setText(str);
        return this;
    }

    public MyDialog img(int id, int imgRes) {
        ((ImageView) rootView.findViewById(id)).setImageResource(imgRes);
        return this;
    }

    public MyDialog img(int id, Bitmap bitmap) {
        ((ImageView) rootView.findViewById(id)).setImageBitmap(bitmap);
        return this;
    }

    public MyDialog bindView(BindView bindView) {
        this.bindView = bindView;
        return this;
    }

    public static void phoneDialog(Activity activity, String phone) {
        MyDialog.Builder.newBuilder(activity)
                .custom(R.layout.dialog_call)
                .width(-1)
                .gravity(Gravity.BOTTOM)
                .build()
                .show()
                .bindView(myDialog -> {
                    myDialog.text(R.id.dialog_call, "13138705415")
                            .onClick(R.id.dialog_call, v -> AppUtil.call(activity, phone))
                            .onClick(R.id.dialog_cancel);
                });
    }

    public static void qrCodeDialog(Activity activity, final String qrStr) {
        MyDialog.Builder.newBuilder(activity)
                .custom(R.layout.dialog_image)
                .widthPercent(0.84f)
                .height((int) (ResUtil.getWidthInPx() * 0.84f))
                .build()
                .show()
                .bindView(myDialog -> {
                    myDialog.img(R.id.dialog_image, QRCode.createQRImage(qrStr))
                            .onClick(R.id.dialog_image);
                });
    }

    public static interface BindView {
        void bindView(MyDialog myDialog);
    }

    public static class Builder {

        private MyDialog dialog;
        private String createTime;
        private BaseActivity activity;

        //标准消息框
        private String title = null;
        private String contentStr = null;
        private String oneButton = "我知道了";
        private String negativeButton = "取消";
        private String positiveButton = "确定";
        private OnButtonClickListener oneButtonClick = null;
        private OnButtonClickListener negativeButtonClick = null;
        private OnButtonClickListener positiveButtonClick = null;

        //自定义
        private int layoutId = -1;

        //大小
        private int width = 100;
        private int height = WindowManager.LayoutParams.WRAP_CONTENT;

        //位置
        private int gravity = Gravity.CENTER;
        private int offsetX = 0;
        private int offsetY = 0;

        private boolean canceledOnTouchOutside = false;

        public Builder(Activity activity) {
            width = ResUtil.getWidthInPx() - ResUtil.getDimenInPx(R.dimen.dp_40);
            this.activity = (BaseActivity) activity;
        }

        public static Builder newBuilder(Activity activity) {
            return new Builder(activity);
        }

        public MyDialog build() {
            createTime = String.valueOf(System.currentTimeMillis());
            dialog = new MyDialog();
            dialog.builder = this;
            return dialog;
        }

        /*
        *   设置 标题，信息体
        *        一个按钮
        *        左右两个按钮
        * */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder contentStr(String contentStr) {
            this.contentStr = contentStr;
            return this;
        }

        public Builder oneButton(String oneButton) {
            this.oneButton = oneButton;
            return this;
        }

        public Builder positiveButton(String positiveButton) {
            this.positiveButton = positiveButton;
            return this;
        }

        public Builder negativeButton(String negativeButton) {
            this.negativeButton = negativeButton;
            return this;
        }

        public Builder onOneButton() {
            this.oneButtonClick = myDialog -> myDialog.dismiss();
            return this;
        }

        public Builder onPositiveButton() {
            this.positiveButtonClick = myDialog -> myDialog.dismiss();
            return this;
        }

        public Builder onNegativeButton() {
            this.negativeButtonClick = myDialog -> myDialog.dismiss();
            return this;
        }

        public Builder onOneButton(OnButtonClickListener oneButtonClick) {
            this.oneButtonClick = oneButtonClick;
            return this;
        }

        public Builder onPositiveButton(OnButtonClickListener positiveButtonClick) {
            this.positiveButtonClick = positiveButtonClick;
            return this;
        }

        public Builder onNegativeButton(OnButtonClickListener negativeButtonClick) {
            this.negativeButtonClick = negativeButtonClick;
            return this;
        }

        public Builder onOneButton(String oneButton, OnButtonClickListener oneButtonClick) {
            this.oneButton = oneButton;
            this.oneButtonClick = oneButtonClick;
            return this;
        }

        public Builder onPositiveButton(String positiveButton, OnButtonClickListener positiveButtonClick) {
            this.positiveButton = positiveButton;
            this.positiveButtonClick = positiveButtonClick;
            return this;
        }

        public Builder onNegativeButton(String negativeButton, OnButtonClickListener negativeButtonClick) {
            this.negativeButtonClick = negativeButtonClick;
            this.negativeButton = negativeButton;
            return this;
        }

        /*
        *   自定义布局
        * */

        public Builder custom(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        /*
         *   整体大小
         * */
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder widthPercent(float widthPercent) {
            this.width = (int) (ResUtil.getWidthInPx() * widthPercent);
            return this;
        }

        public Builder heightPercent(float heightPercent) {
            this.height = (int) (ResUtil.getHeightInPx() * heightPercent);
            return this;
        }

        /*
        *   位置
        * */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder offsetX(int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public Builder offsetY(int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }
    }

    public static interface OnButtonClickListener {
        void onClick(MyDialog myDialog);
    }
}
