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
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 售后服务选择
 */

public class AftersaleServiceActivity extends BaseActivity {
    @BindView(R.id.item_img)
    ImgView godsImg;
    @BindView(R.id.item_name)
    TextView goodsName;
    @BindView(R.id.item_price)
    TextView goodsPrice;
    @BindView(R.id.item_spec)
    TextView goodsSpec;
    @BindView(R.id.item_tag)
    TextView goodsTag;
    @BindView(R.id.item_price_origin)
    TextView goodsPriceOrigin;
    @BindView(R.id.item_num)
    TextView goodsNum;

    public static void startAftersaleServiceActivity() {
        Intent intent = new Intent(getTopActivity(), AftersaleServiceActivity.class);
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersale_service);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.aftersale_refundL)
    public void onRefund(){
        RefundActivity.startRefundActivity(true);
    }

    @OnClick(R.id.aftersale_return_goods)
    public void onReturnGoods(){
        RefundActivity.startRefundActivity(false);
    }
}
