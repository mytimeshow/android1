package cn.czyugang.tcg.client.modules.balance.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by wuzihong on 2017/10/16.
 * 提现说明
 */

public class ExplainActivity extends BaseActivity {
    public static final String KEY_TYPE = "type";
    public static final int TYPE_WITHDRAW = 0;//提现
    public static final int TYPE_BANK_CAR_VERIFY = 1;//银行卡验证
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_explain_title)
    TextView tv_explain_title;
    @BindView(R.id.tv_explain)
    TextView tv_explain;

    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra(KEY_TYPE, 0);
        initView();
    }

    private void initView() {
        switch (mType) {
            case TYPE_WITHDRAW:
                initWithdraw();
                break;
            case TYPE_BANK_CAR_VERIFY:
                initBankCardVerify();
                break;
        }
    }

    private void initWithdraw() {
        tv_title.setText("提现说明");
        tv_explain_title.setText("余额提现规则");
        tv_explain.setText("1、最低可提现余额必须满100元\n2、单笔提现金额不能超过100元\n3、每天提现金额不能超过100元");
    }

    private void initBankCardVerify() {
        tv_title.setText("验证银行卡信息");
        tv_explain_title.setText("手机号说明");
        tv_explain.setText("银行预留的手机号码是办理该银行卡时所填的手机号码。没有预留、手机号码忘记或者停用，请联系银行客服处理或亲自到银行柜台验证手机。\n\n\n请注意区别绑定同城鸽的手机号码。");
    }

    @OnClick({R.id.iv_back, R.id.tv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_close:
                finish();
                break;
        }
    }
}
