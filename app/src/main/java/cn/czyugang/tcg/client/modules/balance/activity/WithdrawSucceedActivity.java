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
 * 提现成功
 */

public class WithdrawSucceedActivity extends BaseActivity {
    @BindView(R.id.tv_withdraw_money)
    TextView tv_withdraw_money;
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @BindView(R.id.tv_play_money)
    TextView tv_play_money;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_succeed);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_finish:
                finish();
                break;
        }
    }
}
