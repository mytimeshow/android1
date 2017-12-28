package cn.czyugang.tcg.client.modules.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.PayApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.activity.AddBankCardActivity;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.AppUtil;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class PayOrderWayActivity extends BaseActivity {
    @BindView(R.id.pay_order_need_num)
    TextView needNum;
    @BindView(R.id.pay_order_delivery_num)
    TextView deliveryNum;
    @BindView(R.id.pay_order_ways)
    LinearLayout payWays;

    private String orderId = "";
    private double totalPay = 0;
    private double deliveryPay = 0;
    private boolean useWallet = false;
    private String psw = "";

    public static void startPayOrderWayActivity(String orderId, double pay, double delivery, boolean useWallet) {
        Intent intent = new Intent(getTopActivity(), PayOrderWayActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("pay", pay);
        intent.putExtra("delivery", delivery);
        intent.putExtra("useWallet", useWallet);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra("orderId");
        totalPay = getIntent().getDoubleExtra("pay", 0);
        deliveryPay = getIntent().getDoubleExtra("delivery", 0);
        useWallet = getIntent().getBooleanExtra("useWallet", false);

        setContentView(R.layout.activity_pay_order_way);
        ButterKnife.bind(this);

        needNum.setText(CommonUtil.formatPrice(totalPay));
        deliveryNum.setText(CommonUtil.formatPrice(deliveryPay));

        addPayWay("支付宝", R.drawable.alipay, v -> onAlipay());
        addPayWay("微信支付", R.drawable.wechat, v -> onWeChat());
        addPayWay("工行银行 储蓄卡（尾号7807）", R.drawable.icbc, v -> onICBC("7807"));
    }

    private void addPayWay(String name, int imgId, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_pay_way, payWays, false);
        TextView t = view.findViewById(R.id.view_pay_way_name);
        ImageView i = view.findViewById(R.id.view_pay_way_icon);
        t.setText(name);
        i.setImageResource(imgId);
        view.setOnClickListener(onClickListener);
        payWays.addView(view);
    }

    private MyDialog myDialog = null;

    private void onAlipay() {
        if (psw.equals("")) {
            myDialog = MyDialog.pswDialog(this, null, password -> {
                psw = password;
                onAlipay();
                if (myDialog!=null) myDialog.dismiss();
            });
            return;
        }
        PayApi.payOrder(orderId, false, psw, "ALIPAY").subscribe(new NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) {
                    psw = "";
                    return;
                }
                if (response.values == null) return;
                String payInfo = response.values.optString("param");
                if (payInfo.equals("")) return;
                PayApi.openAlipay(PayOrderWayActivity.this, payInfo);
            }
        });
    }

    private void onWeChat() {

    }

    private void onICBC(String cardId) {

    }

    public void onPaySuccess(){
        AppUtil.toast("支付成功");
        PaySuccessActivity.startPaySuccessActivity(totalPay);
    }

    public void onPayFail(){
        AppUtil.toast("支付失败，请重试");
    }

    @OnClick(R.id.pay_order_add_card)
    public void onAddCard() {
        AddBankCardActivity.startAddBankCardActivity();
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }
}
