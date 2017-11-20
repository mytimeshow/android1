package cn.czyugang.tcg.client.modules.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.set.contract.ModifyAccountContract;
import cn.czyugang.tcg.client.modules.set.presenter.ModifyAccountPresenter;

/**
 * Created by wuzihong on 2017/10/4.
 * 修改账号
 */

public class ModifyAccountActivity extends BaseActivity implements ModifyAccountContract.View {
    public static final String KEY_ACCOUNT = "account";
    @BindView(R.id.et_account)
    EditText et_account;

    private String mAccount;

    private ModifyAccountContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);
        ButterKnife.bind(this);
        mAccount = getIntent().getStringExtra(KEY_ACCOUNT);
        mPresenter = new ModifyAccountPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    public void initView() {
        et_account.setText(mAccount);
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                mPresenter.modifyAccount(et_account.getText().toString());
                break;
        }
    }
}
