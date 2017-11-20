package cn.czyugang.tcg.client.modules.set.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.set.contract.BindEmailContract;
import cn.czyugang.tcg.client.modules.set.presenter.BindEmailPresenter;

/**
 * Created by wuzihong on 2017/10/7.
 * 绑定邮箱
 */

public class BindEmailActivity extends BaseActivity implements BindEmailContract.View {
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_verification_code)
    EditText et_verification_code;
    @BindView(R.id.tv_send_verification_code)
    TextView tv_send_verification_code;
    private Resources mResources;

    private BindEmailContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);
        ButterKnife.bind(this);
        mResources = getResources();
        mPresenter = new BindEmailPresenter(this);
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
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

    @OnClick({R.id.iv_back, R.id.tv_send_verification_code, R.id.tv_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_verification_code:
                mPresenter.sendVerificationCode(et_email.getText().toString());
                break;
            case R.id.tv_bind:
                mPresenter.bindEmail(et_email.getText().toString(), et_verification_code.getText().toString());
                break;
        }
    }
}
