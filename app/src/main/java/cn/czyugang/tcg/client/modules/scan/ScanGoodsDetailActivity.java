package cn.czyugang.tcg.client.modules.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.widget.FiveStarView;
import cn.czyugang.tcg.client.widget.MultiImgView;

/**
 * @author ruiaa
 * @date 2017/12/5
 */

public class ScanGoodsDetailActivity extends BaseActivity {
    @BindView(R.id.good_multi_img)
    MultiImgView multiImg;
    @BindView(R.id.good_name)
    TextView name;
    @BindView(R.id.good_name_sub)
    TextView nameSub;
    @BindView(R.id.good_price)
    TextView price;
    @BindView(R.id.good_sale)
    TextView sale;
    @BindView(R.id.good_promotion)
    TextView promotion;
    @BindView(R.id.good_guarantee)
    TextView guarantee;
    @BindView(R.id.good_five_star)
    FiveStarView fiveStar;
    @BindView(R.id.good_comment_num)
    TextView commentNum;
    @BindView(R.id.goods_bottom_add_trolley)
    LinearLayout trolleyAddL;
    @BindView(R.id.goods_money)
    TextView goodsMoney;
    @BindView(R.id.goods_bottom_trolley)
    LinearLayout trolleyL;
    @BindView(R.id.goods_trolley_tipL)
    LinearLayout trolleyTipL;

    public static void startScanGoodsDetailActivity(String scan) {
        Intent intent = new Intent(getTopActivity(), ScanGoodsDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_goods_detail);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.good_back)
    public void onBack() {
        ScanGoodsListActivity.startScanGoodsListActivity();
    }

    @OnClick(R.id.goods_trolley_img)
    public void onOpenTrolley() {
        trolleyAddL.setVisibility(View.GONE);
        trolleyL.setVisibility(View.VISIBLE);
        if (trolleyTipL.getVisibility() != View.INVISIBLE) trolleyTipL.setVisibility(View.VISIBLE);
    }

    private void onCloseTrolley() {
        trolleyAddL.setVisibility(View.VISIBLE);
        trolleyL.setVisibility(View.GONE);
        trolleyTipL.setVisibility(View.GONE);
    }

    @OnClick(R.id.goods_trolley_tip_close)
    public void onCloseTrolleyTip() {
        trolleyTipL.setVisibility(View.INVISIBLE);
    }
}
