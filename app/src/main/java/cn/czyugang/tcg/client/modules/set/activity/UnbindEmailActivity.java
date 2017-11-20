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
 * 解绑邮箱
 */

public class UnbindEmailActivity extends BaseActivity {
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE = "mobile";
    @BindView(R.id.tv_email)
    TextView tv_email;

    private String mEmail;
    private String mMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbind_email);
        ButterKnife.bind(this);
        mEmail = getIntent().getStringExtra(KEY_EMAIL);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        initView();
    }

    private void initView() {
        tv_email.setText(mEmail);
    }

    @OnClick({R.id.iv_back, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                Intent intent = new Intent(context, MobileVerifyActivity.class);
                intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_UNBIND_EMAIL);
                intent.putExtra(MobileVerifyActivity.KEY_MOBILE, mMobile);
                startActivity(intent);
                finish();
                break;
        }
    }
}
