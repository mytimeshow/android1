package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class SelectDeliveryActivity extends BaseActivity {
    @BindView(R.id.select_delivery_store_name)
    TextView storeName;
    @BindView(R.id.select_delivery_goods)
    RecyclerView goodsR;
    @BindView(R.id.select_delivery_platform)
    TextView deliveryPlatform;
    @BindView(R.id.select_delivery_self)
    TextView deliverySelf;
    @BindView(R.id.select_delivery_time1)
    TextView deliveryTime1;
    @BindView(R.id.select_delivery_time2)
    TextView deliveryTime2;
    @BindView(R.id.select_delivery_rest)
    TextView deliveryRest;

    public static void startSelectDeliveryActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectDeliveryActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_delivery);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.select_delivery_confirm)
    public void onConfirm(){

    }

    @OnClick(R.id.select_delivery_change)
    public void onChange(){

    }


    @OnClick(R.id.select_delivery_self)
    public void onDeliverySelf(){

    }

    @OnClick(R.id.select_delivery_platform)
    public void onDeliveryPlatform(){

    }

}
