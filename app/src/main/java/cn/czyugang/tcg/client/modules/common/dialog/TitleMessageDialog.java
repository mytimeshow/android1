package cn.czyugang.tcg.client.modules.common.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseDialogFragment;

/**
 * Created by wuzihong on 2017/9/6.
 * 带标题的消息对话框
 */

public class TitleMessageDialog extends BaseDialogFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.tv_negative)
    TextView tv_negative;
    @BindView(R.id.tv_positive)
    TextView tv_positive;

    private String mTitle;
    private String mMessage;
    private String mNegative;
    private String mPositive;
    private OnClickListener mListener;

    public static TitleMessageDialog newInstance() {
        return new TitleMessageDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_title_message, container);
            ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    public TitleMessageDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public TitleMessageDialog setMessage(String message) {
        mMessage = message;
        return this;
    }

    public TitleMessageDialog setNegativeButton(String negative) {
        mNegative = negative;
        return this;
    }

    public TitleMessageDialog setPositiveButton(String positive) {
        mPositive = positive;
        return this;
    }

    public TitleMessageDialog setOnClickListener(OnClickListener listener) {
        mListener = listener;
        return this;
    }

    private void initView() {
        //标题
        tv_title.setText(mTitle);
        //消息内容
        tv_message.setText(mMessage);
        //消极按钮
        if (!TextUtils.isEmpty(mNegative)) {
            tv_negative.setText(mNegative);
            if (TextUtils.isEmpty(mPositive)) {
                //设置为中号样式
                tv_negative.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium));
                tv_negative.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.top_padding_dialog_button_medium),
                        0, getResources().getDimensionPixelOffset(R.dimen.bottom_padding_dialog_button_medium));
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_negative.getLayoutParams();
                lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                lp.leftMargin = 0;
            }
        } else {
            tv_negative.setVisibility(View.GONE);
        }
        //积极按钮
        if (!TextUtils.isEmpty(mPositive)) {
            tv_positive.setText(mPositive);
            if (TextUtils.isEmpty(mNegative)) {
                //设置为中号样式
                tv_positive.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium));
                tv_positive.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.top_padding_dialog_button_medium),
                        0, getResources().getDimensionPixelOffset(R.dimen.bottom_padding_dialog_button_medium));
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_positive.getLayoutParams();
                lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                lp.leftMargin = 0;
            }
        } else {
            tv_positive.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_negative, R.id.tv_positive})
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_negative:
                if (mListener != null) {
                    mListener.onClick(this, Dialog.BUTTON_NEGATIVE);
                }
                break;
            case R.id.tv_positive:
                if (mListener != null) {
                    mListener.onClick(this, Dialog.BUTTON_POSITIVE);
                }
                break;
        }
    }

    public interface OnClickListener {
        void onClick(TitleMessageDialog dialog, int what);
    }
}
