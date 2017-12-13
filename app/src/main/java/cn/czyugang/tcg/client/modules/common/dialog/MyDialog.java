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
    public View rootView;
    private View.OnClickListener toDismissListener = null;

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
        WindowManager.LayoutParams lp = window.getAttributes();
        if (builder.offsetX != 0) lp.x = builder.offsetX;
        if (builder.offsetY != 0) lp.y = builder.offsetY;
        if (builder.dimAmount >= 0 && builder.dimAmount <= 1) lp.dimAmount = builder.dimAmount;
        window.setAttributes(lp);
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
        if (builder.bindView != null) builder.bindView.bindView(this);
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
            if (builder.negativeButtonClick != null) {
                builder.negativeButtonClick.onClick(this);
            } else {
                dismiss();
            }
        });
        onClick(R.id.tv_positive, v -> {
            if (builder.positiveButtonClick != null) {
                builder.positiveButtonClick.onClick(this);
            } else {
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
        onClick(R.id.tv_negative, v -> {
            if (builder.negativeButtonClick != null) {
                builder.negativeButtonClick.onClick(this);
            } else {
                dismiss();
            }
        });
        onClick(R.id.tv_positive, v -> {
            if (builder.positiveButtonClick != null) {
                builder.positiveButtonClick.onClick(this);
            } else {
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

    //底部  电话号码+取消 按钮
    public static void phoneDialog(Activity activity, String phone) {
        MyDialog.Builder.newBuilder(activity)
                .custom(R.layout.dialog_call)
                .width(-1)
                .gravity(Gravity.BOTTOM)
                .bindView(myDialog -> {
                    myDialog.text(R.id.dialog_call, "13138705415")
                            .onClick(R.id.dialog_call, v -> AppUtil.call(activity, phone))
                            .onClick(R.id.dialog_cancel);
                })
                .build()
                .show();
    }

    //正方形 大屏 二维码
    public static void qrCodeDialog(Activity activity, final String qrStr) {
        MyDialog.Builder.newBuilder(activity)
                .custom(R.layout.dialog_image)
                .widthPercent(0.84f)
                .height((int) (ResUtil.getWidthInPx() * 0.84f))
                .bindView(myDialog -> {
                    myDialog.img(R.id.dialog_image, QRCode.createQRImage(qrStr))
                            .onClick(R.id.dialog_image);
                })
                .build()
                .show();
    }

    //recyclerview 长按显示的 收藏 按钮
    public static void collectionBg(Activity activity, View view, final boolean hadCollect, OnButtonClickListener onButtonClickListener) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0];
        int y = location[1] - AppUtil.getStatusBarHeight();
        Builder.newBuilder(activity)
                .custom(R.layout.view_bg_collection)
                .gravity(Gravity.TOP | Gravity.LEFT)
                .offsetX(x)
                .offsetY(y)
                .width(view.getWidth())
                .height(view.getHeight())
                .canceledOnTouchOutside(true)
                .bgAlpha(0)
                .bindView(myDialog -> {
                    TextView textView = myDialog.rootView.findViewById(R.id.view_collection);
                    if (hadCollect) {
                        textView.setText("已收藏");
                        textView.setTextColor(ResUtil.getColor(R.color.main_red));
                    }
                    textView.setOnClickListener(v -> {
                        if (onButtonClickListener != null) onButtonClickListener.onClick(myDialog);
                    });
                })
                .build()
                .show();
    }

    //所有分享类型
    public static void showAllShare(Activity activity, final View.OnClickListener onEachShare) {
        MyDialog.Builder.newBuilder(activity)
                .custom(R.layout.view_all_share)
                .width(-1)
                .height(-1)
                .bindView(myDialog -> {
                    View.OnClickListener onClickListener = v -> {
                        myDialog.dismiss();
                        onEachShare.onClick(v);
                    };
                    myDialog.onClick(R.id.view_share_wechat, onClickListener);
                    myDialog.onClick(R.id.view_share_wechat_circle, onClickListener);
                    myDialog.onClick(R.id.view_share_qq, onClickListener);
                    myDialog.onClick(R.id.view_share_qzone, onClickListener);
                    myDialog.onClick(R.id.view_share_sina_blog, onClickListener);
                    myDialog.onClick(R.id.view_share_scan, onClickListener);
                })
                .build()
                .show();
    }

    //在点击处显示的 气泡toast
    public static void bubbleToast(Activity activity, View view, final String text) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = ResUtil.getHeightInPx() - location[1];
        Builder.newBuilder(activity)
                .custom(R.layout.view_bubble_toast)
                .gravity(Gravity.BOTTOM | Gravity.LEFT)
                .offsetX(x)
                .offsetY(y)
                .width(-2)
                .height(-2)
                .canceledOnTouchOutside(true)
                .bgAlpha(0)
                .bindView(myDialog -> {
                    myDialog.text(R.id.view_bubble_toast_text, text);
                })
                .build()
                .show();
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

        //视图
        private BindView bindView = null;

        //自定义
        private int layoutId = -1;

        //大小
        private int width = WindowManager.LayoutParams.WRAP_CONTENT;
        private int height = WindowManager.LayoutParams.WRAP_CONTENT;

        //位置
        private int gravity = Gravity.CENTER;
        private int offsetX = 0;
        private int offsetY = 0;

        //背景透明度 0~1.0
        private float dimAmount = -1;


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

        public Builder bindView(BindView bindView) {
            this.bindView = bindView;
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

        public Builder bgAlpha(float dimAmount) {
            this.dimAmount = dimAmount;
            return this;
        }
    }

    public static interface OnButtonClickListener {
        void onClick(MyDialog myDialog);
    }
}
