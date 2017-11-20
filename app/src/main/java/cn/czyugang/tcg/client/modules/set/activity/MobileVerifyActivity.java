package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.RealNameAuth;
import cn.czyugang.tcg.client.modules.set.contract.MobileVerifyContract;
import cn.czyugang.tcg.client.modules.set.presenter.MobileVerifyPresenter;

/**
 * Created by wuzihong on 2017/10/6.
 * 验证验证码
 */

public class MobileVerifyActivity extends BaseActivity implements MobileVerifyContract.View {
    public static final String KEY_TYPE = "type";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_BANK_MOBILE = "bankMobile";
    public static final String KEY_BANK_CARD_INFO = "bankCardInfo";
    public static final String KEY_REAL_NAME_AUTH = "realNameAuth";
    public static final int TYPE_SET_PASSWORD = 0;//设置密码
    public static final int TYPE_SET_PAY_PASSWORD = 1;//设置支付密码
    public static final int TYPE_FORGET_PAY_PASSWORD = 2;//找回支付密码
    public static final int TYPE_BIND_EMAIL = 3;//绑定邮箱
    public static final int TYPE_UNBIND_EMAIL = 4;//解绑邮箱
    public static final int TYPE_BIND_BANK_CARD = 5;//绑定银行卡
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.et_verification_code)
    EditText et_verification_code;
    @BindView(R.id.tv_send_verification_code)
    TextView tv_send_verification_code;
    @BindView(R.id.tv_next)
    TextView tv_next;
    private Resources mResources;

    private MobileVerifyContract.Presenter mPresenter;

    private int mType;
    private String mMobile;
    private String mBankMobile;
    private BankCardInfo mBankCardInfo;
    private RealNameAuth mRealNameAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify);
        ButterKnife.bind(this);
        mResources = getResources();
        Intent intent = getIntent();
        mType = intent.getIntExtra(KEY_TYPE, 0);
        mMobile = intent.getStringExtra(KEY_MOBILE);
        mBankMobile = intent.getStringExtra(KEY_BANK_MOBILE);
        mBankCardInfo = intent.getParcelableExtra(KEY_BANK_CARD_INFO);
        mRealNameAuth = intent.getParcelableExtra(KEY_REAL_NAME_AUTH);
        mPresenter = new MobileVerifyPresenter(this, mType, mMobile);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        switch (mType) {
            case TYPE_SET_PASSWORD:
                initSetPassword();
                break;
            case TYPE_SET_PAY_PASSWORD:
            case TYPE_BIND_BANK_CARD:
                initSetPayPassword();
                break;
            case TYPE_FORGET_PAY_PASSWORD:
                initForgetPayPassword();
                break;
            case TYPE_BIND_EMAIL:
                initBindEmail();
                break;
            case TYPE_UNBIND_EMAIL:
                initUnbindEmail();
                break;
        }
        tv_mobile.setText(mMobile);
    }

    private void initSetPassword() {
        tv_title.setText("设置登录密码");
        tv_hint.setText("尚未设置登录密码，设置登录密码需进行账号验证！");
    }

    private void initSetPayPassword() {
        tv_title.setText("设置支付密码");
        tv_hint.setText("尚未设置支付密码，设置支付密码需进行账号验证！");
    }

    private void initForgetPayPassword() {
        tv_title.setText("找回支付密码");
        tv_hint.setText("通过手机验证码进行验证！");
        tv_hint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        Resources resources = getResources();
        tv_hint.setTextColor(resources.getColor(R.color.text_gray));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_hint.getLayoutParams();
        lp.gravity = Gravity.LEFT;
        int margins = (int) (10 * resources.getDisplayMetrics().density);
        lp.setMargins(margins, margins, margins, margins);
    }

    private void initBindEmail() {
        tv_title.setText("绑定邮箱");
        tv_hint.setText("尚未绑定邮箱，绑定邮箱需进行账号验证！");
    }

    private void initUnbindEmail() {
        tv_title.setText("解绑邮箱");
        tv_hint.setText("解绑邮箱，需通过账号验证！");
        tv_next.setText("解绑");
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
    public void startSetPasswordActivity() {
        Intent intent = new Intent(context, SetPasswordActivity.class);
        intent.putExtra(SetPasswordActivity.KEY_MOBILE, mMobile);
        startActivity(intent);
    }

    @Override
    public void startSetPayPasswordActivity() {
        Intent intent = new Intent(context, SetPayPasswordActivity.class);
        switch (mType) {
            case TYPE_SET_PAY_PASSWORD:
                intent.putExtra(SetPayPasswordActivity.KEY_TYPE, SetPayPasswordActivity.TYPE_SET_PAY_PASSWORD);
                break;
            case TYPE_BIND_BANK_CARD:
                intent.putExtra(SetPayPasswordActivity.KEY_TYPE, SetPayPasswordActivity.TYPE_BIND_BANK_CARD);
                intent.putExtra(SetPayPasswordActivity.KEY_BANK_MOBILE, mBankMobile);
                intent.putExtra(SetPayPasswordActivity.KEY_BANK_CARD_INFO, mBankCardInfo);
                intent.putExtra(SetPayPasswordActivity.KEY_REAL_NAME_AUTH, mRealNameAuth);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void startForgetPayPasswordActivity() {
        Intent intent = new Intent(context, SetPayPasswordActivity.class);
        intent.putExtra(SetPayPasswordActivity.KEY_TYPE, SetPayPasswordActivity.TYPE_FORGET_PAY_PASSWORD);
        startActivity(intent);
    }

    @Override
    public void startBindEmailActivity() {
        startActivity(new Intent(context, BindEmailActivity.class));
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
