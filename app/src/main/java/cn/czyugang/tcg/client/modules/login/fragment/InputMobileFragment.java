package cn.czyugang.tcg.client.modules.login.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.login.contract.InputMobileContract;

/**
 * Created by wuzihong on 2017/9/22.
 * 输入手机号码界面
 */

public class InputMobileFragment extends BaseFragment implements InputMobileContract.View {
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.cb_user_agreement)
    CheckBox cb_user_agreement;
    @BindView(R.id.tv_send_verification_code)
    TextView tv_send_verification_code;

    private Resources mResources;

    private InputMobileContract.Presenter mPresenter;

    public static InputMobileFragment newInstance() {
        return new InputMobileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_input_mobile, container, false);
            ButterKnife.bind(this, rootView);
            mResources = getResources();
        }
        return rootView;
    }

    public InputMobileFragment setPresenter(InputMobileContract.Presenter presenter) {
        mPresenter = presenter;
        return this;
    }

    @Override
    public void updateVerificationCodeCountdown(int time) {
        if (time == 0) {
            tv_send_verification_code.setText("获取验证码");
            tv_send_verification_code.setEnabled(true);
            //设置为可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.text_main_button_red_normal));
            tv_send_verification_code.setBackgroundResource(R.drawable.bg_main_button_red_normal);
        } else {
            tv_send_verification_code.setText(String.format("%ds后重新获取", time));
            tv_send_verification_code.setEnabled(false);
            //设置为不可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.text_main_button_red_disable));
            tv_send_verification_code.setBackgroundResource(R.drawable.bg_main_button_red_disable);
        }
    }

    @OnClick(R.id.tv_send_verification_code)
    public void onClick() {
        mPresenter.sendVerificationCode(et_mobile.getText().toString(), cb_user_agreement.isChecked());
    }
}
