package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.address.activity.AddressManageActivity;
import cn.czyugang.tcg.client.modules.set.contract.SetContract;
import cn.czyugang.tcg.client.modules.set.presenter.SetPresenter;
import cn.czyugang.tcg.client.utils.ImageLoader;

/**
 * Created by wuzihong on 2017/9/29.
 * 设置
 */

public class SetActivity extends BaseActivity implements SetContract.View {
    @BindView(R.id.fresco_avatar)
    SimpleDraweeView fresco_avatar;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.cl_account)
    ConstraintLayout cl_account;
    @BindView(R.id.tv_address_manage)
    TextView tv_address_manage;
    @BindView(R.id.div_address_manage)
    View div_address_manage;
    @BindView(R.id.tv_account_safe)
    TextView tv_account_safe;
    @BindView(R.id.div_account_safe)
    View div_account_safe;
    @BindView(R.id.tv_notification)
    TextView tv_notification;
    @BindView(R.id.div_notification)
    View div_notification;
    @BindView(R.id.tv_logout)
    TextView tv_logout;

    private SetContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        mPresenter = new SetPresenter(this);
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        //显示个人信息、地址管理、账户与安全、消息提醒、退出登录
        cl_account.setVisibility(View.VISIBLE);
        tv_address_manage.setVisibility(View.VISIBLE);
        div_address_manage.setVisibility(View.VISIBLE);
        tv_account_safe.setVisibility(View.VISIBLE);
        div_account_safe.setVisibility(View.VISIBLE);
        tv_notification.setVisibility(View.VISIBLE);
        div_notification.setVisibility(View.VISIBLE);
        tv_logout.setVisibility(View.VISIBLE);
        if (userInfo != null) {
            UserBase userBase = userInfo.getUserBase();
            if (userBase != null) {
                //昵称
                tv_name.setText(userBase.getNickname());
                //账号
                tv_account.setText(userBase.getAccount());
            }
            UserDetail userDetail = userInfo.getUserDetail();
            if (userDetail != null) {
                //头像
                ImageLoader.loadImageToView(userDetail.getFileId(), fresco_avatar);
            }
        }
    }

    @Override
    public void hideUserInfo() {
        //隐藏个人信息、地址管理、账户与安全、消息提醒、退出登录
        cl_account.setVisibility(View.GONE);
        tv_address_manage.setVisibility(View.GONE);
        div_address_manage.setVisibility(View.GONE);
        tv_account_safe.setVisibility(View.GONE);
        div_account_safe.setVisibility(View.GONE);
        tv_notification.setVisibility(View.GONE);
        div_notification.setVisibility(View.GONE);
        tv_logout.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.cl_account, R.id.tv_address_manage, R.id.tv_account_safe,
            R.id.tv_notification, R.id.tv_general, R.id.tv_about, R.id.tv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cl_account:
                startActivity(new Intent(context, AccountInfoActivity.class));
                break;
            case R.id.tv_address_manage:
                startActivity(new Intent(context, AddressManageActivity.class));
                break;
            case R.id.tv_account_safe:
                startActivity(new Intent(context, AccountSafeActivity.class));
                break;
            case R.id.tv_notification:
                startActivity(new Intent(context, NotificationSetActivity.class));
                break;
            case R.id.tv_general:
                startActivity(new Intent(context, GeneralActivity.class));
                break;
            case R.id.tv_about:
                startActivity(new Intent(context, AboutActivity.class));
                break;
            case R.id.tv_logout:
                mPresenter.logout();
                break;
        }
    }
}
