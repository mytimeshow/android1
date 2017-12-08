package cn.czyugang.tcg.client.modules.groupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.GrouponGroup;
import cn.czyugang.tcg.client.modules.store.StoreActivity;
import cn.czyugang.tcg.client.widget.FiveStarView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.MultiImgView;

/**
 * @author ruiaa
 * @date 2017/12/7
 * <p>
 * 降价拍 商品详情
 */

public class GrouponGoodsActivity extends BaseActivity {
    @BindView(R.id.groupon_goods_scroll)
    NestedScrollView scrollView;
    @BindView(R.id.groupon_goods_multi_img)
    MultiImgView multiImg;
    @BindView(R.id.groupon_goods_name)
    TextView name;
    @BindView(R.id.groupon_goods_name_sub)
    TextView nameSub;
    @BindView(R.id.groupon_goods_price)
    TextView price;
    @BindView(R.id.groupon_goods_sale)
    TextView sale;
    @BindView(R.id.groupon_goods_price_down)
    TextView priceDown;
    @BindView(R.id.groupon_goods_price_min)
    TextView priceMin;
    @BindView(R.id.groupon_goods_time_limit)
    TextView timeLimit;
    @BindView(R.id.groupon_goods_explain)
    TextView explain;
    @BindView(R.id.groupon_goods_group_list)
    RecyclerView groupR;
    @BindView(R.id.groupon_goods_five_star)
    FiveStarView fiveStar;
    @BindView(R.id.groupon_goods_comment_num)
    TextView commentNum;
    @BindView(R.id.groupon_goods_comment_label)
    LabelLayout commentLabel;
    @BindView(R.id.groupon_goods_pull_detail)
    TextView pullToDetail;
    @BindView(R.id.groupon_goods_buy)
    TextView buy;
    @BindView(R.id.groupon_goods_open_group)
    TextView openGroup;

    private List<GrouponGroup> groupList = new ArrayList<>();
    private GroupsAdapter adapter;

    public static void startGrouponGoodsActivity() {
        Intent intent = new Intent(getTopActivity(), GrouponGoodsActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupon_goods);
        ButterKnife.bind(this);

        groupList.add(new GrouponGroup());
        groupList.add(new GrouponGroup());
        groupList.add(new GrouponGroup());

        adapter = new GroupsAdapter(groupList, this);
        groupR.setLayoutManager(new LinearLayoutManager(this));
        groupR.setAdapter(adapter);
        groupR.setNestedScrollingEnabled(false);


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int maxScroll=-1;
            boolean isScrollToBottom=false;
            long lastBottomTime=0;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (maxScroll<0) maxScroll=pullToDetail.getBottom()-scrollView.getHeight();
                if (scrollY == maxScroll) {
                    if (isScrollToBottom) {
                        if (System.currentTimeMillis()-lastBottomTime>500){
                            GrouponDetailActivity.startGrouponDetailActivity();
                        }else {
                            scrollView.scrollTo(0, maxScroll - 1);
                            return;
                        }
                    }
                    scrollView.scrollTo(0, maxScroll - 1);
                    isScrollToBottom=true;
                    lastBottomTime=System.currentTimeMillis();
                }else {
                    isScrollToBottom=false;
                }
            }
        });

    }

    @OnClick(R.id.groupon_goods_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.groupon_goods_store)
    public void onStore() {
        StoreActivity.startStoreActivity("999");
    }

    @OnClick(R.id.groupon_goods_buy)
    public void onBuy() {

    }
    
    @OnClick(R.id.groupon_goods_open_group)
    public void onOpenGroup(){

    }

    private static class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Holder> {
        private List<GrouponGroup> list;
        private Activity activity;

        public GroupsAdapter(List<GrouponGroup> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_groupon_group, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            GrouponGroup data = list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
