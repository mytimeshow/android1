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
import cn.czyugang.tcg.client.api.PayApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.set.activity.MobileVerifyActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;
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
    @BindView(R.id.pay_order_final)
    TextView finalPayTop;
    @BindView(R.id.pay_order_money)
    TextView finalPay;

    private String fromActivity = "";
    private String orderId = "";
    private double totalPay = 0;
    private double deliveryPay = 0;
    private double cashBalance = 0;
    private double unCashBalance = 0;
    private String psw = "";


    public static void startPayOrderActivity(String orderId) {
        Activity from = getTopActivity();
        Intent intent = new Intent(from, PayOrderActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("from", from.getClass().getName());
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra("orderId");

        setContentView(R.layout.activity_pay_order);
        ButterKnife.bind(this);

        getPrePayInfo();
    }

    private void getPrePayInfo() {
        PayApi.prePayOrder(orderId).subscribe(new NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (response.values == null) return;
                cashBalance = response.values.optDouble("cashBalance", 0);
                unCashBalance = response.values.optDouble("uncashBalance", 0);
                totalPay = response.values.optDouble("totalPayPrice", 0);
                deliveryPay = response.values.optDouble("totalDeliveryPrice", 0);
                onSelectWallet();
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.pay_order_select_wallet)
    public void onSelectWallet() {
        if (selectWallet.isChecked()) {
            walletMinus.setVisibility(View.VISIBLE);
        } else {
            walletMinus.setVisibility(View.GONE);
        }
        totalPayT.setText(CommonUtil.formatPrice(totalPay));
        walletMinus.setText("-" + CommonUtil.formatPrice(getWalletMinus()));
        finalPayTop.setText(CommonUtil.formatPrice(getFinalPay()));
        finalPay.setText(CommonUtil.formatPrice(getFinalPay()));
    }

    @OnClick(R.id.pay_order_wallet)
    public void onShowWalletCash() {
        MyDialog.bubbleToast(this, walletMinus, String.format(
                "可提现余额：￥%.2f \n不可提现余额：￥%.2f \n优先使用不可提现余额进行抵扣",
                cashBalance, unCashBalance));
    }

    @OnClick(R.id.pay_order_confirm)
    public void onConfirm() {
        if (getFinalPay() <= 0) {
            checkPswAndPayAllByWallet();
        } else {
            PayOrderWayActivity.startPayOrderWayActivity(orderId, getFinalPay(), deliveryPay, selectWallet.isChecked());
        }
    }

    private MyDialog myDialog = null;

    private void checkPswAndPayAllByWallet() {
        if (UserOAuth.getInstance().getUserInfo().getUserDetail().hadSetPayPassword()) {
            if (psw.equals("")) {
                myDialog = MyDialog.pswDialog(this, null, password -> {
                    psw = password;
                    payAllByWallet();
                });
                return;
            }
            payAllByWallet();
        } else {
            AppUtil.toast("您未设置支付密码，请先设置支付密码");
            Intent intent = new Intent(context, MobileVerifyActivity.class);
            intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_SET_PAY_PASSWORD);
            UserInfo userInfo = UserOAuth.getInstance().getUserInfo();
            if (userInfo != null) {
                UserBase userBase = userInfo.getUserBase();
                if (userBase != null) {
                    intent.putExtra(MobileVerifyActivity.KEY_MOBILE, userBase.getPhone());
                }
            }
            startActivity(intent);
        }
    }

    private void payAllByWallet() {
        if (myDialog != null) myDialog.dismiss();
        PayApi.payOrder(orderId, true, psw, null)
                .subscribe(new NetObserver<Response<Object>>() {
                    @Override
                    public void onNext(Response<Object> response) {
                        super.onNext(response);
                        if (!ErrorHandler.judge200(response)) {
                            psw = "";
                            return;
                        } else {
                            AppUtil.toast("支付成功");
                            PaySuccessActivity.startPaySuccessActivity(totalPay);
                        }
                    }
                });
    }


    private double getFinalPay() {
        if (selectWallet.isChecked()) {
            double d = totalPay - cashBalance - unCashBalance;
            if (d <= 0) return 0;
            return d;
        }
        return totalPay;
    }

    private double getWalletMinus() {
        double d = totalPay - cashBalance - unCashBalance;
        if (d <= 0) return totalPay;
        return cashBalance + unCashBalance;
    }
}
