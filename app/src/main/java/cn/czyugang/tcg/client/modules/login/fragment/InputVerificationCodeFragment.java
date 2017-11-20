package cn.czyugang.tcg.client.modules.login.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.login.contract.InputVerificationCodeContract;

/**
 * Created by wuzihong on 2017/9/22.
 * 输入验证码界面
 */

public class InputVerificationCodeFragment extends BaseFragment implements
        InputVerificationCodeContract.View {
    public static final int TYPE_FORGET_PASSWORD = 0;//找回密码
    public static final int TYPE_BIND_MOBILE = 1;//绑定手机
    public static final int TYPE_MODIFY_MOBILE = 2;//更换手机
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_send_verification_code)
    TextView tv_send_verification_code;
    @BindView(R.id.et_verification_code)
    EditText et_verification_code;
    @BindView(R.id.tv_commit)
    TextView tv_commit;

    private Resources mResources;
    private int mType;

    private InputVerificationCodeContract.Presenter mPresenter;

    public static InputVerificationCodeFragment newInstance() {
        return new InputVerificationCodeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_input_verification_code, container, false);
            ButterKnife.bind(this, rootView);
            mResources = getResources();
            initView();
        }
        return rootView;
    }

    public InputVerificationCodeFragment setPresenter(InputVerificationCodeContract.Presenter presenter) {
        mPresenter = presenter;
        return this;
    }

    public InputVerificationCodeFragment setType(int type) {
        mType = type;
        return this;
    }

    private void initView() {
        switch (mType) {
            case TYPE_FORGET_PASSWORD:
                tv_commit.setText("提交验证");
                break;
            case TYPE_BIND_MOBILE:
                tv_commit.setText("绑定");
                break;
            case TYPE_MODIFY_MOBILE:
                tv_commit.setText("提交");
                break;
        }
    }

    @Override
    public void updateVerificationCodeCountdown(int time) {
        if (time == 0) {
            tv_send_verification_code.setText("获取验证码");
            tv_send_verification_code.setEnabled(true);
            //设置为可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.text_main_button_red_border_normal));
            tv_send_verification_code.setBackgroundResource(R.drawable.bg_main_button_red_border_normal);
        } else {
            tv_send_verification_code.setText(String.format("%ds后重新获取", time));
            tv_send_verification_code.setEnabled(false);
            //设置为不可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.text_main_button_red_border_disable));
            tv_send_verification_code.setBackgroundResource(R.drawable.bg_main_button_red_border_disable);
        }
    }

    @Override
    public void updateMobile(String mobile) {
        tv_mobile.setText(mobile);
    }

    @OnClick({R.id.tv_send_verification_code, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_verification_code:
                mPresenter.sendVerificationCode();
                break;
            case R.id.tv_commit:
                mPresenter.commitVerificationCode(et_verification_code.getText().toString());
                break;
        }
    }
}
