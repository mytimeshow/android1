package cn.czyugang.tcg.client.modules.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.login.contract.SetPasswordSucceedContract;

/**
 * Created by wuzihong on 2017/9/22.
 * 设置密码成功界面
 */

public class SetPasswordSucceedFragment extends BaseFragment implements
        SetPasswordSucceedContract.View {
    @BindView(R.id.tv_succeed_description)
    TextView tv_succeed_description;

    public static SetPasswordSucceedFragment newInstance() {
        return new SetPasswordSucceedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_set_password_succeed, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void updateGotoLoginCountdown(int time) {
        tv_succeed_description.setText(String.format("设置成功，前往登录(%ds)", time));
    }

    @OnClick(R.id.tv_login)
    public void onClick() {
        activity.finish();
    }
}
