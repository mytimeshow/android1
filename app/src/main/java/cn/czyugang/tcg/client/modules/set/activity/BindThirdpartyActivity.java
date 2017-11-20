package cn.czyugang.tcg.client.modules.set.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.TencentApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.common.dialog.MessageDialog;
import cn.czyugang.tcg.client.modules.set.contract.BindThirdpartyContract;
import cn.czyugang.tcg.client.modules.set.presenter.BindThirdpartyPresenter;

/**
 * Created by wuzihong on 2017/10/7.
 * 绑定第三方账号
 */

public class BindThirdpartyActivity extends BaseActivity implements BindThirdpartyContract.View, MessageDialog.OnClickListener {
    private final String TAG_UNBIND_WX_DIALOG = "UnbindWX";
    private final String TAG_UNBIND_TENCENT_DIALOG = "UnbindTencent";
    private final String TAG_UNBIND_WEIBO_DIALOG = "UnbindWeibo";
    @BindView(R.id.tv_wx)
    TextView tv_wx;
    @BindView(R.id.tv_wx_bind)
    TextView tv_wx_bind;
    @BindView(R.id.tv_tencent)
    TextView tv_tencent;
    @BindView(R.id.tv_tencent_bind)
    TextView tv_tencent_bind;
    @BindView(R.id.tv_weibo)
    TextView tv_weibo;
    @BindView(R.id.tv_weibo_bind)
    TextView tv_weibo_bind;
    private Resources mResources;

    private BindThirdpartyContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_thirdparty);
        ButterKnife.bind(this);
        mResources = getResources();
        mPresenter = new BindThirdpartyPresenter(this);
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
    }

    @Override
    public void updateBindInfo(UserInfo userInfo) {
        if (userInfo != null) {
            //微信
            if ("YES".equals(userInfo.getIsBindWECHAT())) {
                tv_wx.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wx_bind, 0, 0, 0);
                tv_wx.setTextColor(mResources.getColor(R.color.text_black));
                tv_wx_bind.setText("已绑定");
                tv_wx_bind.setTextColor(mResources.getColor(R.color.text_dark_gray));
            } else {
                tv_wx.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wx_unbind, 0, 0, 0);
                tv_wx.setTextColor(mResources.getColor(R.color.text_gray));
                tv_wx_bind.setText("未绑定");
                tv_wx_bind.setTextColor(mResources.getColor(R.color.main_light_red));
            }
            //QQ
            if ("YES".equals(userInfo.getIsBindQQ())) {
                tv_tencent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tencent_bind, 0, 0, 0);
                tv_tencent.setTextColor(mResources.getColor(R.color.text_black));
                tv_tencent_bind.setText("已绑定");
                tv_tencent_bind.setTextColor(mResources.getColor(R.color.text_dark_gray));
            } else {
                tv_tencent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tencent_unbind, 0, 0, 0);
                tv_tencent.setTextColor(mResources.getColor(R.color.text_gray));
                tv_tencent_bind.setText("未绑定");
                tv_tencent_bind.setTextColor(mResources.getColor(R.color.main_light_red));
            }
            //微博
            if ("YES".equals(userInfo.getIsBindWEIBO())) {
                tv_weibo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_weibo_bind, 0, 0, 0);
                tv_weibo.setTextColor(mResources.getColor(R.color.text_black));
                tv_weibo_bind.setText("已绑定");
                tv_weibo_bind.setTextColor(mResources.getColor(R.color.text_dark_gray));
            } else {
                tv_weibo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_weibo_unbind, 0, 0, 0);
                tv_weibo.setTextColor(mResources.getColor(R.color.text_gray));
                tv_weibo_bind.setText("未绑定");
                tv_weibo_bind.setTextColor(mResources.getColor(R.color.main_light_red));
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.fl_wx_bind, R.id.fl_tencent_bind, R.id.fl_weibo_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_wx_bind: {
                UserInfo userInfo = mPresenter.getUserInfo();
                if (userInfo != null) {
                    if ("YES".equals(userInfo.getIsBindWECHAT())) {
                        MessageDialog.newInstance()
                                .setMessage("要解除与微信账号的绑定吗？")
                                .setPositiveButton("解除绑定")
                                .setNegativeButton("取消")
                                .setOnClickListener(this)
                                .show(getSupportFragmentManager(), TAG_UNBIND_WX_DIALOG);
                    } else {
                        mPresenter.bindWX();
                    }
                }
                break;
            }
            case R.id.fl_tencent_bind: {
                UserInfo userInfo = mPresenter.getUserInfo();
                if (userInfo != null) {
                    if ("YES".equals(userInfo.getIsBindQQ())) {
                        MessageDialog.newInstance()
                                .setMessage("要解除与QQ账号的绑定吗？")
                                .setPositiveButton("解除绑定")
                                .setNegativeButton("取消")
                                .setOnClickListener(this)
                                .show(getSupportFragmentManager(), TAG_UNBIND_TENCENT_DIALOG);
                    } else {
                        mPresenter.bindTencent(this);
                    }
                }
                break;
            }
            case R.id.fl_weibo_bind: {
                UserInfo userInfo = mPresenter.getUserInfo();
                if (userInfo != null) {
                    if ("YES".equals(mPresenter.getUserInfo().getIsBindWEIBO())) {
                        MessageDialog.newInstance()
                                .setMessage("要解除与新浪微博账号的绑定吗？")
                                .setPositiveButton("解除绑定")
                                .setNegativeButton("取消")
                                .setOnClickListener(this)
                                .show(getSupportFragmentManager(), TAG_UNBIND_WEIBO_DIALOG);
                    } else {
                        mPresenter.bindWeibo(this);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onClick(MessageDialog dialog, int what) {
        if (what == Dialog.BUTTON_POSITIVE) {
            switch (dialog.getTag()) {
                case TAG_UNBIND_WX_DIALOG:
                    mPresenter.unbindWX();
                    break;
                case TAG_UNBIND_TENCENT_DIALOG:
                    mPresenter.unbindTencent();
                    break;
                case TAG_UNBIND_WEIBO_DIALOG:
                    mPresenter.unbindWeibo();
                    break;
            }
        }
    }
}
