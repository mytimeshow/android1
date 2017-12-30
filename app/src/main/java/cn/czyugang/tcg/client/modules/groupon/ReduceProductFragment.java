package cn.czyugang.tcg.client.modules.groupon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.ReduceProductApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.GrouponGroup;
import cn.czyugang.tcg.client.entity.ReduceProduct;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.common.dialog.StoreTrolleyDialog;
import cn.czyugang.tcg.client.modules.store.StoreActivity;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.FlowLayout;
import cn.czyugang.tcg.client.widget.MultiImgView;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public class ReduceProductFragment extends BaseFragment {
    @BindView(R.id.group_good_tag)
    LinearLayout tagL;
    @BindView(R.id.group_good_guarantee)
    LinearLayout guaranteeL;
    @BindView(R.id.group_good_comment)
    TextView comment;
    @BindView(R.id.group_good_comment_num)
    TextView commentNum;
    @BindView(R.id.group_good_comment_list)
    RecyclerView commentR;
    private static final String TAG = "GrouponGoodsActivity";
    @BindView(R.id.group_goods_scroll)
    NestedScrollView scrollView;
    @BindView(R.id.group_goods_multi_img)
    MultiImgView multiImg;
    @BindView(R.id.group_goods_name)
    TextView name;
    @BindView(R.id.group_goods_name_sub)
    TextView nameSub;
    @BindView(R.id.group_goods_price)
    TextView price;
    @BindView(R.id.group_goods_sale)
    TextView sale;
    @BindView(R.id.group_goods_price_down)
    TextView priceDown;
    @BindView(R.id.group_goods_price_min)
    TextView priceMin;
    @BindView(R.id.group_goods_time_limit)
    TextView timeLimit;
    @BindView(R.id.group_goods_explain)
    TextView explain;
    @BindView(R.id.group_goods_group_list)
    RecyclerView groupR;
    Unbinder unbinder;

    /*  @BindView(R.id.groupon_goods_comment_label)
      LabelLayout commentLabel;
      @BindView(R.id.groupon_goods_pull_detail)
      TextView pullToDetail;*/
    @BindView(R.id.group_goods_buy)
    TextView buy;
    @BindView(R.id.group_goods_open_group)
    TextView openGroup;

    private List<GrouponGroup> groupList = new ArrayList<>();
    private GroupsAdapter adapter;
    private ReduceProduct products;
    private List<String> imgList;

    //private GoodDetailActivity goodDetailActivity;

    //购物车
    public TrolleyStore trolleyStore = null;
    private StoreTrolleyDialog storeTrolleyDialog = null;

    private Good good;

    public static ReduceProductFragment newInstance() {
        ReduceProductFragment fragment = new ReduceProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  goodDetailActivity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_groupon_goods, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        getReduceProduct("940511454422032384","940878770548682753");
        return rootView;
    }

    public void setGoodInfo(Good good) {
        this.good=good;
        initProductTags();
        initServiceTag();
        LogRui.i("initTrolley####1");
    }

    @OnClick(R.id.group_goods_store)
    public void onStore() {
        StoreActivity.startStoreActivity("999");
    }

    @OnClick(R.id.group_goods_buy)
    public void onBuy() {

        GrouponDetailActivity.startGrouponDetailActivity();
    }

    @OnClick(R.id.group_goods_open_group)
    public void onOpenGroup(){


    }
    private void initProductTags(){
        if (good.productTagList==null||good.productTagList.isEmpty()) {
            tagL.setVisibility(View.GONE);
            return;
        }

        for(int i=0,size=good.productTagList.size();i<size&&i<3;i++){
            Good.Tag tag=good.productTagList.get(i);
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_good_detail_tag,tagL,false);
            ((ImgView)view.findViewById(R.id.item_img)).id(tag.picId);
            ((TextView)view.findViewById(R.id.item_name)).setText(tag.name);
            tagL.addView(view,0);
        }

        tagL.setOnClickListener(v -> {
            MyDialog.Builder.newBuilder(getActivity())
                    .custom(R.layout.dialog_good_detail_tags)
                    .gravity(Gravity.BOTTOM)
                    .width(-1)
                    .heightPercent(0.4f)
                    .bindView(myDialog -> {
                        myDialog.onClick(R.id.dialog_close);
                        FlowLayout flowLayout=myDialog.rootView.findViewById(R.id.dialog_tags);
                        for(int i=0,size=good.productTagList.size();i<size;i++){
                            Good.Tag tag=good.productTagList.get(i);
                            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_good_detail_tag_dialog,flowLayout,false);
                            ((ImgView)view.findViewById(R.id.item_img)).id(tag.picId);
                            ((TextView)view.findViewById(R.id.item_name)).setText(tag.name);
                            flowLayout.addView(view,0);
                        }
                    })
                    .build()
                    .show();
        });
    }

    private void initServiceTag(){
        if (good.serviceTagList==null||good.serviceTagList.isEmpty()) {
            guaranteeL.setVisibility(View.GONE);
            return;
        }
        for(Good.Tag tag:good.serviceTagList){
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_good_detail_tag,guaranteeL,false);
            ((ImgView)view.findViewById(R.id.item_img)).id(tag.picId);
            ((TextView)view.findViewById(R.id.item_name)).setText(tag.name);
            guaranteeL.addView(view,0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getLabel() {
        return "商品";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //
    //获取降价拍商品
    public void getReduceProduct(String productid,String activityid){
        ReduceProductApi.getReducesProduct(productid,activityid).subscribe(new BaseActivity.NetObserver<Response<ReduceProduct>>() {
            @Override
            public void onNext(Response<ReduceProduct> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    Log.e(TAG, "onNext: done1" );
                    products=response.getData();
                    Log.e(TAG, "onNext: done2" );
                    // imgList=products.productPicIdList;
                    initData(products);
                    Log.e(TAG, "onNext: done3" );
                    Log.e(TAG, "onNext: done4" );

                    adapter = new GroupsAdapter();
                    groupR.setLayoutManager(new LinearLayoutManager(getActivity()));
                    groupR.setAdapter(adapter);
                    groupR.setNestedScrollingEnabled(false);

                }
            }
        });

    }

    private void initData(ReduceProduct products) {
        initImgs();
        initGroupList(products);
       /* name.setText(products.productTitle);
        nameSub.setText(products.productSubTitle);
        price.setText("￥"+String.valueOf(products.productPrice));
        sale.setText("已售"+String.valueOf(products.sales));
        priceDown.setText("每多一人参团\n拼团价降￥"+products.reducePrice);
        priceMin.setText("拼团冰点价\n最低￥"+products.minPrice);
        timeLimit.setText("拼团有效时间\n"+products.groupTime+"小时");
        commentNum.setText(String.valueOf(products.assessmentCount)+"条评价");
        fiveStar.setScore(products.score);
        buy.setText("￥"+String.valueOf(products.productPrice)+"\n直接购买");
        //评价标签
        labelListBeans=products.labelList;
        List<String> strList=new ArrayList<>();
        for(int i=0,size=labelListBeans.size();i<size;i++){
            String str=labelListBeans.get(i).name;
                    //+"("+ labelListBeans.get(i).count+")";
            strList.add(str);
        }
        Log.e(TAG, "initData: "+strList.get(0)+" /n"+strList.get(1) );*/
        // commentLabel.setTextList(commentLabel,strList);
    }

    private void initGroupList(ReduceProduct products) {
        // groupListBeans=products.groupList;

    }

    private void initImgs() {
        multiImg.setImgIds(imgList);
    }

    public static class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Holder> {
        //   private List<ReduceProduct.GroupListBean> list;
        private Activity activity;

//        public GroupsAdapter(List<ReduceProduct.GroupListBean> list, Activity activity) {
//            this.list = list;
//            this.activity = activity;
//        }

        @Override
        public GroupsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GroupsAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_groupon_group, parent, false));
        }

        @Override
        public void onBindViewHolder(GroupsAdapter.Holder holder, int position) {
            // if(list!=null) {
//               ReduceProduct.GroupListBean data = list.get(position);
//               holder.headName.setText(data.name);
//               holder.currentPrice.setText("当前拼团价 ￥" + data.currentPrice +
//                       "\n" + data.restTime / 60 + "小时" + data.restTime % 60 + "分钟" + "后成团");

//               holder.joinGroup.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View view) {
//                      // Toast.makeText(activity, data.id, Toast.LENGTH_SHORT).show();
//                   }
//               });
//           }
        }

        @Override
        public int getItemCount() {
//            if(list==null){
//                list=new ArrayList<>();
//                ReduceProduct.GroupListBean bean=new ReduceProduct.GroupListBean();
//                bean.setCurrentPrice(66);
//                bean.setId("555555");
//                bean.setName("lisi");
//                bean.setRestTime(12);
//                list.add(bean);
//            }
            return  2;/*ist==null ?3:list.size();*/
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
