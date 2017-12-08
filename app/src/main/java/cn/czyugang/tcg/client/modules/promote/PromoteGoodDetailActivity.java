package cn.czyugang.tcg.client.modules.promote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoteGoodDetailActivity extends BaseActivity {
    @BindView(R.id.promote_goods_detail_img)
    ImgView goodsImg;
    @BindView(R.id.promote_goods_detail_name)
    TextView name;
    @BindView(R.id.promote_goods_detail_name_sub)
    TextView nameSub;
    @BindView(R.id.promote_goods_detail_price)
    TextView price;
    @BindView(R.id.promote_goods_detail_profit)
    TextView profit;
    @BindView(R.id.promote_goods_detail_similar_list)
    RecyclerView similarR;

    private List<Good> similarGoodList = new ArrayList<>();
    private PromoteGoodListActivity.PromoteGoodsAdapter adapter;

    public static void startPromoteGoodDetailActivity() {
        Intent intent = new Intent(getTopActivity(), PromoteGoodDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_goods_detail);
        ButterKnife.bind(this);

        similarGoodList.add(new Good());

        adapter = new PromoteGoodListActivity.PromoteGoodsAdapter(similarGoodList, this);
        similarR.setLayoutManager(new LinearLayoutManager(this));
        similarR.setAdapter(adapter);

    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.commit)
    public void onShareThis(){

    }
}
