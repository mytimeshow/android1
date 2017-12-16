package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.OrderPreSettleResponse;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.modules.common.ImgAdapter;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.string.TimeUtils;

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

    private Store store;
    private OrderPreSettleResponse.StoreMoreInfo storeMoreInfo;

    public static void startSelectDeliveryActivity(Activity activity, int requestCode, Store store, OrderPreSettleResponse.StoreMoreInfo storeMoreInfo) {
        Intent intent = new Intent(activity, SelectDeliveryActivity.class);
        MyApplication.getInstance().activityTransferData = store;
        MyApplication.getInstance().activityTransferDataTwo = storeMoreInfo;
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = (Store) MyApplication.getInstance().activityTransferData;
        storeMoreInfo = (OrderPreSettleResponse.StoreMoreInfo) MyApplication.getInstance().activityTransferDataTwo;
        MyApplication.getInstance().activityTransferData = null;
        MyApplication.getInstance().activityTransferDataTwo = null;
        setContentView(R.layout.activity_select_delivery);
        ButterKnife.bind(this);

        storeName.setText(store.name);
        goodsR.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        goodsR.setAdapter(new ImgAdapter(storeMoreInfo.imgList, this).setSizeRes(R.dimen.dp_60));


        initDeliveryWay();

        if (!storeMoreInfo.arrowDeliveryPlatform() && !storeMoreInfo.arrowDeliveryStore()) {
            disableDeliveryWay(deliveryPlatform);
        }
        if (!storeMoreInfo.arrowDeliveryFETCH()) {
            disableDeliveryWay(deliverySelf);
        }
        if (storeMoreInfo.platformDeliveryRemainTime > 0) {
            deliveryRest.setText(String.format("平台配送还有 %s 休息，欲购从速喔~",
                    TimeUtils.transfer(storeMoreInfo.platformDeliveryRemainTime * 1000, "HH:mm ")));
        } else {
            deliveryRest.setVisibility(View.GONE);
        }
    }

    private void initDeliveryWay() {
        LogRui.i("initDeliveryWay####" + storeMoreInfo.selectedDeliveryWay + "#");
        switch (storeMoreInfo.selectedDeliveryWay) {
            case "平台配送":
                selectDeliveryWay(deliveryPlatform);
                unSelectDeliveryWay(deliverySelf);
                deliveryPlatform.setText("平台配送");
                break;
            case "商家配送":
                selectDeliveryWay(deliveryPlatform);
                unSelectDeliveryWay(deliverySelf);
                deliveryPlatform.setText("商家配送");
                break;
            case "自提":
                selectDeliveryWay(deliverySelf);
                unSelectDeliveryWay(deliveryPlatform);
                deliveryPlatform.setText(storeMoreInfo.arrowDeliveryPlatform() ? "平台配送" : "商家配送");
                break;
        }
    }

    private void disableDeliveryWay(TextView textView) {
        textView.setTextColor(ResUtil.getColor(R.color.grey_350));
        textView.setBackgroundResource(R.drawable.border_rect_grey);
        textView.setClickable(false);
    }

    private void selectDeliveryWay(TextView textView) {
        textView.setTextColor(ResUtil.getColor(R.color.main_red));
        textView.setBackgroundResource(R.drawable.border_rect_red);
    }

    private void unSelectDeliveryWay(TextView textView) {
        textView.setTextColor(ResUtil.getColor(R.color.text_black));
        textView.setBackgroundResource(R.drawable.border_rect_grey);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.select_delivery_confirm)
    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("deliveryWay", storeMoreInfo.selectedDeliveryWay);
        intent.putExtra("deliveryTime", storeMoreInfo.selectedDeliveryTime);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.select_delivery_change)
    public void onChangeTime() {

    }


    @OnClick(R.id.select_delivery_self)
    public void onDeliverySelf() {
        storeMoreInfo.selectedDeliveryWay = "自提";
        initDeliveryWay();
    }

    @OnClick(R.id.select_delivery_platform)
    public void onDeliveryPlatform() {
        storeMoreInfo.selectedDeliveryWay = deliveryPlatform.getText().toString();
        initDeliveryWay();
    }

}
