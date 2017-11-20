package cn.czyugang.tcg.client.modules.entry.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.balance.activity.BalanceActivity;
import cn.czyugang.tcg.client.modules.entry.contract.MyContract;
import cn.czyugang.tcg.client.modules.entry.presenter.MyPresenter;
import cn.czyugang.tcg.client.modules.login.activity.LoginActivity;
import cn.czyugang.tcg.client.modules.set.activity.SetActivity;
import cn.czyugang.tcg.client.utils.ImageLoader;

/**
 * Created by wuzihong on 2017/9/13.
 * 个人中心
 */

public class MyFragment extends BaseFragment implements MyContract.View {
    @BindView(R.id.fresco_avatar)
    SimpleDraweeView fresco_avatar;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_login_description)
    TextView tv_login_description;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_points)
    TextView tv_points;
    @BindView(R.id.tv_coupon)
    TextView tv_coupon;
    @BindView(R.id.grid_tools)
    RecyclerView grid_tools;

    private MyContract.Presenter mPresenter;

    private Resources mResources;

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my, container, false);
            ButterKnife.bind(this, rootView);
            mResources = getResources();
            mPresenter = new MyPresenter(this);
            mPresenter.subscribe();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        //隐藏登录描述
        tv_login_description.setVisibility(View.GONE);
        //修改用户名布局
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_name
                .getLayoutParams();
        lp.bottomMargin = 0;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        if (userInfo != null) {
            //设置账户余额
            String balanceStr = userInfo.getTotalBalance();
            if (!TextUtils.isEmpty(balanceStr)) {
                tv_balance.setText(new DecimalFormat("0.##").format(new BigDecimal(userInfo.getTotalBalance())));
            }
            //设置我的积分
            tv_points.setText(String.valueOf(userInfo.getTotalScore()));
            //设置抵用券
            tv_coupon.setText(String.valueOf(userInfo.getTotalFetchCode()));
            UserBase userBase = userInfo.getUserBase();
            if (userBase != null) {
                //设置昵称
                tv_name.setText(userBase.getNickname());
            }
            UserDetail userDetail = userInfo.getUserDetail();
            if (userDetail != null) {
                //设置头像
                ImageLoader.loadImageToView(userDetail.getFileId(), fresco_avatar);
            }
        }
    }

    @Override
    public void logout() {
        //隐藏登录描述
        tv_login_description.setVisibility(View.VISIBLE);
        //设置头像
        fresco_avatar.setImageResource(R.drawable.ic_logout_avatar);
        //设置用户名
        tv_name.setText("未登陆");
        //修改用户名布局
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_name
                .getLayoutParams();
        lp.bottomMargin = (int) (3.5 * mResources.getDisplayMetrics().density);
        lp.bottomToBottom = R.id.guideline;
        lp.topToTop = ConstraintLayout.LayoutParams.UNSET;
        tv_balance.setText("0");
        tv_points.setText("0");
        tv_coupon.setText("0");
    }

    @OnClick({R.id.iv_set, R.id.iv_message, R.id.fresco_avatar, R.id.tv_name, R.id.ll_balance,
            R.id.ll_points, R.id.ll_coupon, R.id.fl_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_set:
                startActivity(new Intent(context, SetActivity.class));
                break;
            case R.id.fresco_avatar:
            case R.id.tv_name: {
                Intent intent = new Intent();
                if (UserOAuth.getInstance().isLogin()) {
                    intent.setClass(context, SetActivity.class);
                } else {
                    intent.setClass(context, LoginActivity.class);
                }
                startActivity(intent);
                break;
            }
            case R.id.iv_message:
                //TODO 启动消息页面
                break;
            case R.id.ll_balance:
                Intent intent = new Intent();
                if (UserOAuth.getInstance().isLogin()) {
                    intent.setClass(context, BalanceActivity.class);
                } else {
                    intent.setClass(context, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.ll_points:
                //TODO 启动我的分数页面
                break;
            case R.id.ll_coupon:
                //TODO 启动抵用券页面
                break;
            case R.id.fl_order:
                //TODO 启动订单列表
                break;
        }
    }
}
