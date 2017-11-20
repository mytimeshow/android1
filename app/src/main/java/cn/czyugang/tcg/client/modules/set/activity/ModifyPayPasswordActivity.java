package cn.czyugang.tcg.client.modules.set.activity;

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
import cn.czyugang.tcg.client.modules.set.contract.ModifyPayPasswordContract;
import cn.czyugang.tcg.client.modules.set.presenter.ModifyPayPasswordPresenter;

/**
 * Created by wuzihong on 2017/10/7.
 * 修改支付密码
 */

public class ModifyPayPasswordActivity extends BaseActivity implements ModifyPayPasswordContract.View {
    public static final String KEY_MOBILE = "mobile";
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.et_old_pay_password)
    EditText et_old_pay_password;
    @BindView(R.id.et_pay_password)
    EditText et_pay_password;
    @BindView(R.id.et_again_pay_password)
    EditText et_again_pay_password;

    private ModifyPayPasswordContract.Presenter mPresenter;

    private String mMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pay_password);
        ButterKnife.bind(this);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        mPresenter = new ModifyPayPasswordPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        tv_mobile.setText(mMobile);
    }

    @OnClick({R.id.iv_back, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_commit:
                mPresenter.modifyPayPassword(et_old_pay_password.getText().toString(), et_pay_password.getText().toString(), et_again_pay_password.getText().toString());
                break;
        }
    }
}
