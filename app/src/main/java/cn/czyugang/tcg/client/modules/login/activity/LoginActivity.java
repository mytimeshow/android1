package cn.czyugang.tcg.client.modules.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.TencentApi;
import cn.czyugang.tcg.client.api.WeiboApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.DefaultFragmentPagerAdapter;
import cn.czyugang.tcg.client.modules.login.contract.LoginContract;
import cn.czyugang.tcg.client.modules.login.fragment.AccountLoginFragment;
import cn.czyugang.tcg.client.modules.login.fragment.ShortcutLoginFragment;
import cn.czyugang.tcg.client.modules.login.presenter.LoginPresenter;

/**
 * Created by wuzihong on 2017/9/15.
 * 登录
 */

public class LoginActivity extends BaseActivity implements LoginContract.View,
        ViewPager.OnPageChangeListener {
    private final int LINE_TAB_MARGIN = 38;//红色标志线左右边距
    @BindView(R.id.line_tab)
    View line_tab;
    @BindView(R.id.pager_login)
    ViewPager pager_login;

    private DisplayMetrics mDm;

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPresenter = new LoginPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TencentApi.getInstance().onActivityResult(requestCode, resultCode, data);
        WeiboApi.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mDm = getResources().getDisplayMetrics();
        //初始化红色标志线宽度（屏幕宽度 / 2 - 左右边距）
        line_tab.getLayoutParams().width = (int) (mDm.widthPixels / 2 - 2 * LINE_TAB_MARGIN * mDm.density);
        //初始化帐号密码登录界面和手机快捷登录界面
        List<BaseFragment> loginFragmentList = new ArrayList<>();
        loginFragmentList.add(AccountLoginFragment.newInstance());
        loginFragmentList.add(ShortcutLoginFragment.newInstance());
        pager_login.setAdapter(new DefaultFragmentPagerAdapter(getSupportFragmentManager(), loginFragmentList));
        pager_login.addOnPageChangeListener(this);
    }

    @Override
    public void startBindMobileActivity(String type, String authInfo, String authToken) {
        Intent intent = new Intent(context, BindMobileActivity.class);
        intent.putExtra(BindMobileActivity.KEY_TYPE, BindMobileActivity.TYPE_BIND_MOBILE);
        intent.putExtra(BindMobileActivity.KEY_AUTH_TYPE, type);
        intent.putExtra(BindMobileActivity.KEY_AUTH_INFO, authInfo);
        intent.putExtra(BindMobileActivity.KEY_AUTH_TOKEN, authToken);
        startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.tv_forgot_password, R.id.tv_account_login_tab,
            R.id.tv_shortcut_login_tab, R.id.iv_tencent_login, R.id.iv_wechat_login,
            R.id.iv_sina_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(context, ForgetPasswordActivity.class));
                break;
            case R.id.tv_account_login_tab:
                pager_login.setCurrentItem(0);
                break;
            case R.id.tv_shortcut_login_tab:
                pager_login.setCurrentItem(1);
                break;
            case R.id.iv_tencent_login:
                mPresenter.tencentLogin(this);
                break;
            case R.id.iv_wechat_login:
                mPresenter.wxLogin();
                break;
            case R.id.iv_sina_login:
                mPresenter.weiboLogin(this);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //设置红色标志线位置（屏幕宽度 / 2 * 偏移量 + 左边距）
        line_tab.setTranslationX(mDm.widthPixels / 2 * (positionOffset + position) + LINE_TAB_MARGIN * mDm.density);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
