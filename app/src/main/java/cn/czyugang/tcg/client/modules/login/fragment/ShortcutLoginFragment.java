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
import cn.czyugang.tcg.client.modules.login.contract.ShortcutLoginContract;
import cn.czyugang.tcg.client.modules.login.presenter.ShortcutLoginPresenter;

/**
 * Created by wuzihong on 2017/9/16.
 * 手机号快捷登录
 */

public class ShortcutLoginFragment extends BaseFragment implements ShortcutLoginContract.View {
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.tv_send_verification_code)
    TextView tv_send_verification_code;
    @BindView(R.id.et_verification_code)
    EditText et_verification_code;

    private Resources mResources;

    private ShortcutLoginContract.Presenter mPresenter;

    public static ShortcutLoginFragment newInstance() {
        return new ShortcutLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_shortcut_login, container, false);
            ButterKnife.bind(this, rootView);
            mPresenter = new ShortcutLoginPresenter(this);
            mResources = getResources();
            mPresenter.subscribe();
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
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

    @OnClick({R.id.tv_send_verification_code, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_verification_code:
                mPresenter.sendVerificationCode(et_mobile.getText().toString());
                break;
            case R.id.tv_login:
                mPresenter.login(et_mobile.getText().toString(), et_verification_code.getText().toString());
                break;
        }
    }
}
