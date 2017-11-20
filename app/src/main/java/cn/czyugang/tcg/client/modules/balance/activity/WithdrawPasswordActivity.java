package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.set.activity.MobileVerifyActivity;
import cn.czyugang.tcg.client.widget.PayPasswordEditText;

/**
 * Created by wuzihong on 2017/10/16.
 * 充值支付密码
 */

public class WithdrawPasswordActivity extends BaseActivity implements PayPasswordEditText.OnEntryCompleteListener {
    @BindView(R.id.tv_withdraw_money)
    TextView tv_withdraw_money;
    @BindView(R.id.payPasswordEditText)
    PayPasswordEditText payPasswordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        payPasswordEditText.setOnEntryCompleteListener(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_forgot_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_forgot_password: {
                Intent intent = new Intent(context, MobileVerifyActivity.class);
                intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_FORGET_PAY_PASSWORD);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onEntryComplete(String password) {
        startActivity(new Intent(context, WithdrawSucceedActivity.class));
        finish();
    }
}
