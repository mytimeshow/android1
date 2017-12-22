package cn.czyugang.tcg.client.modules.set.activity;

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

/**
 * Created by wuzihong on 2017/10/7.
 * 支付密码管理
 */

public class PayPasswordManageActivity extends BaseActivity {
    public static final String KEY_MOBILE = "mobile";
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;

    private String mMobile;

    public static void startPayPasswordManageActivity(String phone){
        Intent intent=new Intent(getTopActivity(),PayPasswordManageActivity.class);
        intent.putExtra(KEY_MOBILE,phone);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_password_manage);
        ButterKnife.bind(this);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        initView();
    }

    private void initView() {
        tv_mobile.setText(String.format("通过手机 %s 获取验证码进行验证", mMobile));
    }

    @OnClick({R.id.iv_back, R.id.tv_forgot_pay_password, R.id.tv_modify_pay_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_forgot_pay_password: {
                Intent intent = new Intent(context, MobileVerifyActivity.class);
                intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_FORGET_PAY_PASSWORD);
                intent.putExtra(MobileVerifyActivity.KEY_MOBILE, mMobile);
                startActivity(intent);
                break;
            }
            case R.id.tv_modify_pay_password: {
                Intent intent = new Intent(context, ModifyPayPasswordActivity.class);
                intent.putExtra(ModifyPayPasswordActivity.KEY_MOBILE, mMobile);
                startActivity(intent);
                break;
            }
        }
    }
}
