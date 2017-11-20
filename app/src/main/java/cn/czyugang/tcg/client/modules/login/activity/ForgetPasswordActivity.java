package cn.czyugang.tcg.client.modules.login.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.DefaultFragmentPagerAdapter;
import cn.czyugang.tcg.client.modules.common.dialog.MessageDialog;
import cn.czyugang.tcg.client.modules.login.contract.ForgetPasswordContract;
import cn.czyugang.tcg.client.modules.login.fragment.InputMobileFragment;
import cn.czyugang.tcg.client.modules.login.fragment.InputVerificationCodeFragment;
import cn.czyugang.tcg.client.modules.login.fragment.SetPasswordFragment;
import cn.czyugang.tcg.client.modules.login.fragment.SetPasswordSucceedFragment;
import cn.czyugang.tcg.client.modules.login.presenter.ForgetPasswordPresenter;

/**
 * Created by Administrator on 2017/1/17.
 * 忘记密码界面
 */

public class ForgetPasswordActivity extends BaseActivity implements ForgetPasswordContract.View,
        MessageDialog.OnClickListener {
    @BindView(R.id.group_tab)
    RadioGroup group_tab;
    @BindView(R.id.pager_forget_password)
    ViewPager pager_forget_password;

    private InputMobileFragment mInputMobileFragment;
    private InputVerificationCodeFragment mInputVerificationCodeFragment;
    private SetPasswordSucceedFragment mSetPasswordSucceedFragment;

    private ForgetPasswordContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        mPresenter = new ForgetPasswordPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void onBackPressed() {
        //如果是在“输入验证码”页面点击返回，回到“输入手机号”页面
        if (pager_forget_password.getCurrentItem() == 1) {
            gotoInputMobile();
            return;
        }
        super.onBackPressed();
    }

    private void initView() {
        //初始化找回密码各步骤界面
        List<BaseFragment> fragmentList = new ArrayList<>();
        mInputMobileFragment = InputMobileFragment.newInstance()
                .setPresenter(mPresenter);
        fragmentList.add(mInputMobileFragment);
        mInputVerificationCodeFragment = InputVerificationCodeFragment.newInstance()
                .setPresenter(mPresenter)
                .setType(InputVerificationCodeFragment.TYPE_FORGET_PASSWORD);
        fragmentList.add(mInputVerificationCodeFragment);
        fragmentList.add(SetPasswordFragment.newInstance()
                .setPresenter(mPresenter));
        mSetPasswordSucceedFragment = SetPasswordSucceedFragment.newInstance();
        fragmentList.add(mSetPasswordSucceedFragment);
        pager_forget_password.setAdapter(new DefaultFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        //设置ViewPager不可滑动
        pager_forget_password.setOnTouchListener((v, event) -> true);
    }

    @Override
    public void showMobileUnregistered() {
        MessageDialog.newInstance()
                .setMessage("该手机号尚未注册\n可使用快捷登录")
                .setNegativeButton("重新输入")
                .setPositiveButton("前往登录")
                .setOnClickListener(this)
                .show(getSupportFragmentManager(), "MessageDialog");
    }

    @Override
    public void updateVerificationCodeCountdown(int time) {
        mInputMobileFragment.updateVerificationCodeCountdown(time);
        mInputVerificationCodeFragment.updateVerificationCodeCountdown(time);
    }

    @Override
    public void updateMobile(String mobile) {
        mInputVerificationCodeFragment.updateMobile(mobile);
    }

    @Override
    public void updateGotoLoginCountdown(int time) {
        mSetPasswordSucceedFragment.updateGotoLoginCountdown(time);
    }

    @Override
    public void gotoInputMobile() {
        pager_forget_password.setCurrentItem(0);
        group_tab.check(R.id.rbtn_input_mobile);
    }

    @Override
    public void gotoInputVerificationCode() {
        pager_forget_password.setCurrentItem(1);
        group_tab.check(R.id.rbtn_input_verification_code);
    }

    @Override
    public void gotoSetPassword() {
        pager_forget_password.setCurrentItem(2);
        group_tab.check(R.id.rbtn_set_password);
    }

    @Override
    public void gotoSetPasswordSucceed() {
        pager_forget_password.setCurrentItem(3);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        //如果是在“输入验证码”页面点击返回，回到“输入手机号”页面
        if (pager_forget_password.getCurrentItem() == 1) {
            gotoInputMobile();
            return;
        }
        finish();
    }

    @Override
    public void onClick(MessageDialog dialog, int what) {
        if (what == Dialog.BUTTON_POSITIVE) {
            finish();
        }
    }
}
