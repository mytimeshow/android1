package cn.czyugang.tcg.client.modules.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.login.contract.SetPasswordContract;

/**
 * Created by wuzihong on 2017/9/22.
 * 设置密码界面
 */

public class SetPasswordFragment extends BaseFragment {
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_again_password)
    EditText et_again_password;

    private SetPasswordContract.Presenter mPresenter;

    public static SetPasswordFragment newInstance() {
        return new SetPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_set_password, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    public SetPasswordFragment setPresenter(SetPasswordContract.Presenter presenter) {
        mPresenter = presenter;
        return this;
    }

    @OnClick(R.id.tv_commit)
    public void onClick() {
        mPresenter.setPassword(et_password.getText().toString(), et_again_password.getText().toString());
    }
}
