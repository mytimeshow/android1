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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.ReduceProductApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.GrouponGroup;
import cn.czyugang.tcg.client.entity.Response;
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
    private ReduceProduct products;
    private List<String> imgList;
    //当前商品的组团数
    private List<GroupListBean> groupListBeans;

    public static void startGrouponGoodsActivity() {
        Intent intent = new Intent(getTopActivity(), GrouponGoodsActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupon_goods);
        ButterKnife.bind(this);
        getReduceProduct("940506493684461569");
        initImgs();
        groupList.add(new GrouponGroup());
        groupList.add(new GrouponGroup());
        groupList.add(new GrouponGroup());

        adapter = new GroupsAdapter(groupListBeans, this);
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
    //获取降价拍商品
    public void getReduceProduct(String id){
        ReduceProductApi.getReducesProduct(id).subscribe(new NetObserver<Response<ReduceProduct>>() {
            @Override
            public void onNext(Response<ReduceProduct> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    products=response.getData();
                    imgList=products.productPicIdList;
                    initData(products);
                }
            }
        });

    }

    private void initData(ReduceProduct products) {
        initImgs();
        initGroupList(products);
        name.setText(products.productTitle);
        nameSub.setText(products.productSubTitle);
        price.setText("￥"+String.valueOf(products.productPrice));
        sale.setText("已售"+String.valueOf(products.sales));
        priceDown.setText("每多一人参团\n拼团价降￥"+products.reducePrice);
        priceMin.setText("拼团冰点价\n最低￥"+products.minPrice);
        timeLimit.setText("拼团有效时间\n"+products.groupTime+"小时");
        commentNum.setText(String.valueOf(products.assessmentCount)+"条评价");
        fiveStar.setText(products.score+"分");
        fiveStar.setStarResId(products.score);
      
        //commentLabel.setTexts();
    }

    private void initGroupList(ReduceProduct products) {
            groupListBeans=products.groupList;

    }

    private void initImgs() {
        multiImg.setImgIds(imgList);
    }

    private static class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Holder> {
        private List<GroupListBean> list;
        private Activity activity;

        public GroupsAdapter(List<GroupListBean> list, Activity activity) {
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



           if(list!=null){
               GroupListBean data = list.get(position);
               holder.headName.setText(data.getName());
               holder.currentPrice.setText("当前拼团价 ￥"+data.getCurrentPrice()+
                       "\n"+data.getRestTime() +"后成团");
           }
           holder.joinGroup.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Toast.makeText(activity, "jionGroup", Toast.LENGTH_SHORT).show();
               }
           });
        }

        @Override
        public int getItemCount() {
            if(list==null){
                list=new ArrayList<>();
                GroupListBean bean=new GroupListBean();
                bean.setCurrentPrice(66);
                bean.setId("555555");
                bean.setName("lisi");
                bean.setRestTime(12);
                list.add(bean);
            }
            return  list==null ?3:list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            cn.czyugang.tcg.client.utils.img.ImgView imgView;
            TextView headName;
            TextView currentPrice;
            TextView joinGroup;
            public Holder(View itemView) {
                super(itemView);
                imgView=itemView.findViewById(R.id.item_img);
                headName=itemView.findViewById(R.id.item_name);
                currentPrice=itemView.findViewById(R.id.item_price);
                joinGroup=itemView.findViewById(R.id.item_group);
            }
        }
    }

}
