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
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;

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

    public static void startPayOrderWayActivity() {
        Intent intent = new Intent(getTopActivity(), PayOrderWayActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order_way);
        ButterKnife.bind(this);

        addPayWay("支付宝",R.drawable.alipay);
        addPayWay("微信支付",R.drawable.wechat);
        addPayWay("工行银行 储蓄卡（尾号7807）",R.drawable.icbc);
    }

    private void addPayWay(String name,int imgId){
        View view= LayoutInflater.from(this).inflate(R.layout.view_pay_way,payWays,false);
        TextView t=view.findViewById(R.id.view_pay_way_name);
        ImageView i=view.findViewById(R.id.view_pay_way_icon);
        t.setText(name);
        i.setImageResource(imgId);
        payWays.addView(view);
    }
}
