package cn.czyugang.tcg.client.modules.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class PayOrderActivity extends BaseActivity {

    private String fromActivity="";
    private String orderId="";


    public static void startPayOrderActivity( ){
        Intent intent=new Intent(getTopActivity(),PayOrderActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.pay_order_confirm)
    public void onConfirm(){
        PayOrderWayActivity.startPayOrderWayActivity();
    }
}
