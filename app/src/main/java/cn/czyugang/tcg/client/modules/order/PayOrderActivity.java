package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.activity.MobileVerifyActivity;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class PayOrderActivity extends BaseActivity {

    @BindView(R.id.pay_order_need_num)
    TextView totalPayT;
    @BindView(R.id.pay_order_delivery)
    TextView deliveryPayT;
    @BindView(R.id.pay_order_select_wallet)
    SelectButton selectWallet;
    @BindView(R.id.pay_order_wallet)
    TextView walletMinus;
    @BindView(R.id.pay_order_money)
    TextView finalPay;

    private String fromActivity = "";
    private String orderId = "";
    private double totalPay = 0;


    public static void startPayOrderActivity(String orderId, double pay) {
        Activity from = getTopActivity();
        Intent intent = new Intent(from, PayOrderActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("pay", pay);
        intent.putExtra("from", from.getClass().getName());
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId=getIntent().getStringExtra("orderId");
        totalPay=getIntent().getDoubleExtra("pay",0);

        setContentView(R.layout.activity_pay_order);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.pay_order_select_wallet)
    public void onSelectWallet(){
        if (selectWallet.isChecked()){
            walletMinus.setVisibility(View.VISIBLE);
        }else {
            walletMinus.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.pay_order_confirm)
    public void onConfirm() {
        if (UserOAuth.getInstance().getUserInfo().getUserDetail().hadSetPayPassword()) {
            PayOrderWayActivity.startPayOrderWayActivity(orderId,totalPay);
        }else {
            AppUtil.toast("您未设置支付密码，请先设置支付密码");
            Intent intent = new Intent(context, MobileVerifyActivity.class);
            intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_SET_PAY_PASSWORD);
            UserInfo userInfo =UserOAuth.getInstance().getUserInfo();
            if (userInfo != null) {
                UserBase userBase = userInfo.getUserBase();
                if (userBase != null) {
                    intent.putExtra(MobileVerifyActivity.KEY_MOBILE, userBase.getPhone());
                }
            }
            startActivity(intent);
        }
    }
}
