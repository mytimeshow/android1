package cn.czyugang.tcg.client.modules.aftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.OrderGoods;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.LabelLayout;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 售后服务选择
 */

public class AftersaleServiceActivity extends BaseActivity {
    @BindView(R.id.item_img)
    ImgView goodsImg;
    @BindView(R.id.item_name)
    TextView goodsName;
    @BindView(R.id.item_spec)
    TextView goodsSpec;
    @BindView(R.id.item_labels)
    LabelLayout labels;
    @BindView(R.id.item_price)
    TextView goodsPrice;
    @BindView(R.id.item_price_origin)
    TextView goodsPriceOrigin;
    @BindView(R.id.item_num)
    TextView goodsNum;
    @BindView(R.id.item_tag)
    TextView goodsTag;

    private OrderGoods orderGoods;

    public static void startAftersaleServiceActivity(OrderGoods orderGoods) {
        Intent intent = new Intent(getTopActivity(), AftersaleServiceActivity.class);
        MyApplication.getInstance().activityTransferData = orderGoods;
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderGoods = (OrderGoods) MyApplication.getInstance().activityTransferData;
        MyApplication.getInstance().activityTransferData = null;

        setContentView(R.layout.activity_aftersale_service);
        ButterKnife.bind(this);

        goodsImg.id(orderGoods.picId);

        goodsName.setText(orderGoods.title);
        goodsSpec.setText(orderGoods.getSpec());
        labels.setTexts(orderGoods.getLabels());

        goodsPrice.setText(CommonUtil.formatPrice(orderGoods.realPrice));
        if (orderGoods.unitPrice > orderGoods.realPrice)
            goodsPriceOrigin.setText(CommonUtil.formatOriginPrice(orderGoods.unitPrice));
        goodsNum.setText("x"+orderGoods.number);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.aftersale_refundL)
    public void onRefund() {
        RefundActivity.startRefundActivity(true, orderGoods);
    }

    @OnClick(R.id.aftersale_return_goods)
    public void onReturnGoods() {
        RefundActivity.startRefundActivity(false, orderGoods);
    }
}
