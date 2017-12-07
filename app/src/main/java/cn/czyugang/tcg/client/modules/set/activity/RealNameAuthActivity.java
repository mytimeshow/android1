package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.contract.RealNameAuthContract;
import cn.czyugang.tcg.client.modules.set.presenter.RealNameAuthPresenter;

/**
 * Created by wuzihong on 2017/10/5.
 * 实名认证
 */

public class RealNameAuthActivity extends BaseActivity implements RealNameAuthContract.View {
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_id_card)
    EditText et_id_card;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;

    private RealNameAuthContract.Presenter mPresenter;

    public static void startRealNameAuthActivity( ){
        Intent intent=new Intent(getTopActivity(),RealNameAuthActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_auth);
        ButterKnife.bind(this);
        mPresenter = new RealNameAuthPresenter(this);
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            UserDetail userDetail = userInfo.getUserDetail();
            if ("YES".equals(userDetail.getRealnameAuth())) {
                //布局调整
                tv_hint.setText("您已通过实名认证");
                et_name.setGravity(Gravity.RIGHT);
                et_id_card.setGravity(Gravity.RIGHT);
                et_name.setKeyListener(null);
                et_id_card.setKeyListener(null);
                tv_confirm.setVisibility(View.GONE);
                //证件号
                et_id_card.setText(userDetail.getIdentityCardId());
                UserBase userBase = userInfo.getUserBase();
                if (userBase != null) {
                    //真实姓名
                    et_name.setText(userBase.getName());
                }
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                mPresenter.auth(et_name.getText().toString(), et_id_card.getText().toString());
                break;
        }
    }
}
