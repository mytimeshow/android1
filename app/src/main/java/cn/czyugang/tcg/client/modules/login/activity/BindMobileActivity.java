package cn.czyugang.tcg.client.modules.login.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import cn.czyugang.tcg.client.modules.login.contract.BindMobileContract;
import cn.czyugang.tcg.client.modules.login.fragment.InputMobileFragment;
import cn.czyugang.tcg.client.modules.login.fragment.InputVerificationCodeFragment;
import cn.czyugang.tcg.client.modules.login.presenter.BindMobilePresenter;

/**
 * Created by wuzihong on 2017/9/23.
 * 绑定手机号码
 */

public class BindMobileActivity extends BaseActivity implements BindMobileContract.View,
        MessageDialog.OnClickListener {
    public static final String KEY_TYPE = "type";
    public static final String KEY_AUTH_TYPE = "authType";
    public static final String KEY_AUTH_INFO = "authInfo";
    public static final String KEY_AUTH_TOKEN = "authToken";
    public static final int TYPE_BIND_MOBILE = 0;//绑定手机号
    public static final int TYPE_MODIFY_MOBILE = 1;//修改手机号
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.group_tab)
    RadioGroup group_tab;
    @BindView(R.id.pager_bind_mobile)
    ViewPager pager_bind_mobile;

    private InputMobileFragment mInputMobileFragment;
    private InputVerificationCodeFragment mInputVerificationCodeFragment;

    private BindMobileContract.Presenter mPresenter;

    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mType = intent.getIntExtra(KEY_TYPE, 0);
        mPresenter = new BindMobilePresenter(this, mType, intent.getStringExtra(KEY_AUTH_TYPE), intent.getStringExtra(KEY_AUTH_INFO), intent.getStringExtra(KEY_AUTH_TOKEN));
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
        if (pager_bind_mobile.getCurrentItem() == 1) {
            gotoInputMobile();
            return;
        }
        super.onBackPressed();
    }

    private void initView() {
        //初始化绑定手机号码各步骤界面
        List<BaseFragment> fragmentList = new ArrayList<>();
        mInputMobileFragment = InputMobileFragment.newInstance()
                .setPresenter(mPresenter);
        fragmentList.add(mInputMobileFragment);
        mInputVerificationCodeFragment = InputVerificationCodeFragment.newInstance()
                .setPresenter(mPresenter);
        switch (mType) {
            case TYPE_BIND_MOBILE:
                tv_title.setText("绑定手机");
                mInputVerificationCodeFragment.setType(InputVerificationCodeFragment.TYPE_BIND_MOBILE);
                break;
            case TYPE_MODIFY_MOBILE:
                tv_title.setText("更换手机号");
                mInputVerificationCodeFragment.setType(InputVerificationCodeFragment.TYPE_MODIFY_MOBILE);
                break;
        }
        fragmentList.add(mInputVerificationCodeFragment);
        pager_bind_mobile.setAdapter(new DefaultFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        //设置ViewPager不可滑动
        pager_bind_mobile.setOnTouchListener((v, event) -> true);
    }

    @Override
    public void showMobileBinding() {
        MessageDialog.newInstance()
                .setMessage("该手机已被绑定")
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
    public void gotoInputMobile() {
        pager_bind_mobile.setCurrentItem(0);
        group_tab.check(R.id.rbtn_input_mobile);
    }

    @Override
    public void gotoInputVerificationCode() {
        pager_bind_mobile.setCurrentItem(1);
        group_tab.check(R.id.rbtn_input_verification_code);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        //如果是在“输入验证码”页面点击返回，回到“输入手机号”页面
        if (pager_bind_mobile.getCurrentItem() == 1) {
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
