package cn.czyugang.tcg.client.modules.aftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.Order;
import cn.czyugang.tcg.client.modules.order.OrderDetailActivity;

/**
 * @author ruiaa
 * @date 2017/11/30
 *
 * 退款申请成功
 */

public class RefundApplyFinishActivity extends BaseActivity {

    public static void startRefundApplyFinishActivity( ){
        Intent intent=new Intent(getTopActivity(),RefundApplyFinishActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersale_refund_apply_finish);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.refund_finish_shop)
    public void onOpenShop(){
        clearAllActivityExceptMain().selectFragment(0);
    }

    @OnClick(R.id.refund_finish_order)
    public void onOpenOrder(){
        OrderDetailActivity.startOrderDetailActivity(new Order());
    }

}
