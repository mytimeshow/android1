package cn.czyugang.tcg.client.modules.login.fragment;

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
import cn.czyugang.tcg.client.modules.login.contract.AccountLoginContract;
import cn.czyugang.tcg.client.modules.login.dialog.InputCodeDialog;
import cn.czyugang.tcg.client.modules.login.presenter.AccountLoginPresenter;

/**
 * Created by wuzihong on 2017/9/16.
 * 账号密码登录
 */

public class AccountLoginFragment extends BaseFragment implements AccountLoginContract.View,
        InputCodeDialog.OnClickListener {
    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_login)
    TextView tv_login;

    private InputCodeDialog mInputCodeDialog;

    private AccountLoginContract.Presenter mPresenter;

    public static AccountLoginFragment newInstance() {
        return new AccountLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_account_login, container, false);
            ButterKnife.bind(this, rootView);
            mPresenter = new AccountLoginPresenter(this);
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
    public void showCodeDialog(String url) {
        mInputCodeDialog = InputCodeDialog.newInstance()
                .setCodeUrl(url)
                .setOnClickListener(this);
        mInputCodeDialog.show(getChildFragmentManager(), "InputCodeDialog");
    }

    @Override
    public void dismissCodeDialog() {
        mInputCodeDialog.dismiss();
    }

    @OnClick(R.id.tv_login)
    public void onClick() {
        mPresenter.login(et_account.getText().toString(), et_password.getText().toString());
    }

    @Override
    public void onClick(InputCodeDialog dialog, String code) {
        mPresenter.login(et_account.getText().toString(), et_password.getText().toString(), code);
    }
}
