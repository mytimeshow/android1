package cn.czyugang.tcg.client.modules.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;

/**
 * @author ruiaa
 * @date 2017/12/25
 */

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.pay_success_pay)
    TextView payT;
    double pay=0;

    public static void startPaySuccessActivity(double pay) {
        Intent intent = new Intent(getTopActivity(), PaySuccessActivity.class);
        intent.putExtra("pay", pay);
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pay=getIntent().getDoubleExtra("pay",0);

        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);

        payT.setText(CommonUtil.formatPrice(pay));
    }

    @OnClick(R.id.pay_success_open_order)
    public void onOpenOrder(){
        MyOrderActivity.startMyOrderActivity();
    }
}
