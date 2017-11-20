package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
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
import cn.czyugang.tcg.client.modules.login.activity.BindMobileActivity;
import cn.czyugang.tcg.client.modules.set.presenter.CurrentMobileCodePresenter;
import cn.czyugang.tcg.client.modules.set.contract.CurrentMobileCodeContract;

/**
 * Created by wuzihong on 2017/10/5.
 * 填写当前手机验证码
 */

public class CurrentMobileCodeActivity extends BaseActivity implements CurrentMobileCodeContract.View {
    public static final String KEY_MOBILE = "mobile";
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.et_verification_code)
    EditText et_verification_code;
    @BindView(R.id.tv_send_verification_code)
    TextView tv_send_verification_code;
    private Resources mResources;

    private CurrentMobileCodeContract.Presenter mPresenter;

    private String mMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_mobile_code);
        ButterKnife.bind(this);
        mResources = getResources();
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        mPresenter = new CurrentMobileCodePresenter(this, mMobile);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        tv_mobile.setText(String.format("请输入 %s 收到的短信验证码", mMobile));
    }

    @Override
    public void updateVerificationCodeCountdown(int time) {
        if (time == 0) {
            tv_send_verification_code.setText("获取验证码");
            tv_send_verification_code.setEnabled(true);
            //设置为可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.main_light_blue));
        } else {
            tv_send_verification_code.setText(String.format("重新获取（%d秒）", time));
            tv_send_verification_code.setEnabled(false);
            //设置为不可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.text_gray));
        }
    }

    @Override
    public void startBindMobileActivity() {
        Intent intent = new Intent(context, BindMobileActivity.class);
        intent.putExtra(BindMobileActivity.KEY_TYPE, BindMobileActivity.TYPE_MODIFY_MOBILE);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.iv_back, R.id.tv_send_verification_code, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_verification_code:
                mPresenter.sendVerificationCode();
                break;
            case R.id.tv_next:
                mPresenter.commitVerificationCode(et_verification_code.getText().toString());
                break;
        }
    }
}
