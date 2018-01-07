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

import org.json.JSONArray;
import org.json.JSONObject;

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
import cn.czyugang.tcg.client.entity.GroupListBean;
import cn.czyugang.tcg.client.entity.GrouponGroup;
import cn.czyugang.tcg.client.entity.ReduceProduct;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.common.ImgActivity;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.common.dialog.StoreTrolleyDialog;
import cn.czyugang.tcg.client.modules.store.StoreActivity;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.FiveStarView;
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
    private CommenAdapter commenAdapter;
    private ReduceProduct reduceProduct;
    private List<String> imgList=new ArrayList<>();
    private List<GroupListBean> groupListBeans=new ArrayList<>();
    private List<ServiceTagList> serviceTagLists;
    private List<ProductTagList> productTagLists;
    private double productPrice;
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
        if (productTagLists==null) {
            tagL.setVisibility(View.GONE);
            return;
        }

        for(int i=0,size=productTagLists.size();i<size&&i<3;i++){
            ProductTagList tag=productTagLists.get(i);
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
                        for(int i=0,size=productTagLists.size();i<size;i++){
                            ProductTagList tag=productTagLists.get(i);
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
        if (serviceTagLists==null) {
            guaranteeL.setVisibility(View.GONE);
            return;
        }
        for(ServiceTagList tagList:serviceTagLists){
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_good_detail_tag,guaranteeL,false);
            ((ImgView)view.findViewById(R.id.item_img)).id(tagList.picId);
            ((TextView)view.findViewById(R.id.item_name)).setText(tagList.name);
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

                    reduceProduct=response.getData();
                    JSONObject object=response.getValues();
                    int sales=reduceProduct.sales;
                    sale.setText("已售"+String.valueOf(sales));

                    initData(object);
                    Log.e(TAG, "onNext: done3" );
                    //团列表
                    adapter = new GroupsAdapter(groupListBeans,getActivity());
                    groupR.setLayoutManager(new LinearLayoutManager(getActivity()));
                    groupR.setAdapter(adapter);
                    groupR.setNestedScrollingEnabled(false);
                    //评论列表
                    commenAdapter=new CommenAdapter();
                    commentR.setLayoutManager(new LinearLayoutManager(getActivity()));
                    commentR.setAdapter(commenAdapter);

                }
            }
        });

    }

    private void initData(JSONObject products) {
        initPicId(products);
        initGroupList(products);
        initServiceTagList(products);
        initServiceTag();
        initPrice(products);
        initProductTagList(products);
        initProductTags();
        initComments(products);

        JSONObject body=products.optJSONObject("reducePriceActivityProduct");
        String title=products.optJSONObject("productInfo").optString("title");
        String subTitle=products.optJSONObject("productInfo").optString("subTitle");
        double reducePrice=products.optDouble("reducePrice");
        double minPrice=body.optDouble("minPrice");
        int time=body.optInt("time");
        int assessmentCount=products.optInt("countAssessment");
        int goodAssessment=(int)products.optDouble("praiseRate")*100;

        name.setText(title);
        nameSub.setText(subTitle);
       price.setText("￥"+String.valueOf(productPrice));
        priceDown.setText("每多一人参团\n拼团价降￥"+reducePrice);
        priceMin.setText("拼团冰点价\n最低￥"+minPrice);
        timeLimit.setText("拼团有效时间\n"+time+"小时");
        comment.setText("商品评价（好评度"+goodAssessment+"%）");
       commentNum.setText(String.valueOf(assessmentCount)+"条评价");
        buy.setText("￥"+String.valueOf(productPrice)+"\n直接购买");

    }

    private void initComments(JSONObject products) {


    }

    private void initProductTagList(JSONObject products) {
        JSONArray jsonArray=products.optJSONArray("productTagList");
        if(jsonArray!=null && jsonArray.length()>0){
            productTagLists=new ArrayList<>();
            for(int i=0,size=jsonArray.length();i<size;i++){
                ProductTagList tagList=new ProductTagList();
                tagList.setPicId(jsonArray.optJSONObject(i).optString("picId"));
                tagList.setName(jsonArray.optJSONObject(i).optString("name"));
                productTagLists.add(tagList);
            }
        }
    }

    private void initPrice(JSONObject products) {
        JSONArray jsonArray=products.optJSONArray("storeInventoryList");
        if(jsonArray!=null && jsonArray.length()>0){
            for(int i=0;i<1;i++){
                productPrice=jsonArray.optJSONObject(i).optDouble("price");
            }
        }
    }

    private void initServiceTagList(JSONObject products) {
        JSONArray jsonArray=products.optJSONArray("serviceTagList");
        if(jsonArray!=null && jsonArray.length()>0){
            serviceTagLists=new ArrayList<>();
            for(int i=0,size=jsonArray.length();i<size;i++){
                ServiceTagList tagList=new ServiceTagList();
                tagList.setPicId(jsonArray.optJSONObject(i).optString("picId"));
                tagList.setName(jsonArray.optJSONObject(i).optString("name"));
                serviceTagLists.add(tagList);
            }
        }

    }

    private void initGroupList(JSONObject products) {
       JSONArray jsonArray=products.optJSONArray("groupList");
       for(int i=0,size=jsonArray.length();i<size;i++){
           GroupListBean group=new GroupListBean();
           for(int j=0;j<5;j++){
               group.setCurrentPrice(jsonArray.optJSONObject(i).optDouble("currentPrice"));
               group.setId(jsonArray.optJSONObject(i).optString("id"));
               group.setName(jsonArray.optJSONObject(i).optString("name"));
               group.setRestTime(jsonArray.optJSONObject(i).optInt("restTime"));
               group.setHeadId(jsonArray.optJSONObject(i).optString("headId"));
           }
           groupListBeans.add(group);

       }
    }

    private void initPicId(JSONObject products) {
        JSONArray jsonArray=products.optJSONArray("productPicList");
        Log.e(TAG, "initPicId: "+jsonArray );
        if(jsonArray!=null && jsonArray.length()>0){
            for(int i=0,size=jsonArray.length();i<size;i++){
                imgList.add(jsonArray.optJSONObject(i).optString("picId"));
            }
            multiImg.setImgIds(imgList);
            multiImg.setShowBigImg(true);
            multiImg.setShowBigImg(false).setOnClickImg(v -> ImgActivity.startImgActivity((ArrayList<String>) imgList,multiImg.getPosition()))
                    .setImgIds(imgList);
        }

    }


    public static class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Holder> {
          private List<GroupListBean> list;
        private Activity activity;

        public GroupsAdapter(List<GroupListBean> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public GroupsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GroupsAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_groupon_group, parent, false));
        }

        @Override
        public void onBindViewHolder(GroupsAdapter.Holder holder, int position) {
             if(list!=null) {
              GroupListBean data = list.get(position);
             // holder.imgView.id(data.headId);
               holder.headName.setText(data.name);
               holder.currentPrice.setText("当前拼团价 ￥" + data.currentPrice +
                       "\n" + data.restTime / 60 + "小时" + data.restTime % 60 + "分钟" + "后成团");

               holder.joinGroup.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                      // Toast.makeText(activity, data.id, Toast.LENGTH_SHORT).show();
                   }
               });
           }
        }

        @Override
        public int getItemCount() {
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
    public static class ServiceTagList{

        /**
         * createTime : 2017-12-31T02:07:49.983Z
         * deleteFlag : string
         * description : string
         * id : string
         * isAuth : string
         * isGroup : string
         * name : string
         * picId : string
         * status : string
         * type : string
         * updateTime : 2017-12-31T02:07:49.983Z
         */

        private String createTime;
        private String deleteFlag;
        private String description;
        private String id;
        private String isAuth;
        private String isGroup;
        public String name;
        public String picId;
        private String status;
        private String type;
        private String updateTime;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(String deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsAuth() {
            return isAuth;
        }

        public void setIsAuth(String isAuth) {
            this.isAuth = isAuth;
        }

        public String getIsGroup() {
            return isGroup;
        }

        public void setIsGroup(String isGroup) {
            this.isGroup = isGroup;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicId() {
            return picId;
        }

        public void setPicId(String picId) {
            this.picId = picId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
    public static class ProductTagList{

        /**
         * createTime : 2017-12-31T02:07:49.982Z
         * deleteFlag : string
         * description : string
         * id : string
         * isAuth : string
         * isGroup : string
         * name : string
         * picId : string
         * status : string
         * type : string
         * updateTime : 2017-12-31T02:07:49.982Z
         */

        private String createTime;
        private String deleteFlag;
        private String description;
        private String id;
        private String isAuth;
        private String isGroup;
        public String name;
        public String picId;
        private String status;
        private String type;
        private String updateTime;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(String deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsAuth() {
            return isAuth;
        }

        public void setIsAuth(String isAuth) {
            this.isAuth = isAuth;
        }

        public String getIsGroup() {
            return isGroup;
        }

        public void setIsGroup(String isGroup) {
            this.isGroup = isGroup;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicId() {
            return picId;
        }

        public void setPicId(String picId) {
            this.picId = picId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
    public static class CommenAdapter extends RecyclerView.Adapter<CommenAdapter.Holder> {
        private List<GroupListBean> list;
        private Activity activity;

      /*  public CommentList(List<GroupListBean> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }*/

        @Override
        public CommenAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommenAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_comment, parent, false));
        }

        @Override
        public void onBindViewHolder(CommenAdapter.Holder holder, int position) {

        }

        @Override
        public int getItemCount() {

            return  list==null ?2:list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            cn.czyugang.tcg.client.utils.img.ImgView headImg;
            cn.czyugang.tcg.client.utils.img.ImgView flagImg1;
            cn.czyugang.tcg.client.utils.img.ImgView flagImg2;
            cn.czyugang.tcg.client.utils.img.ImgView flagImg3;
            TextView headName;
            FiveStarView starView;
            TextView arriveTime;
            TextView date;
            TextView content;
            LinearLayout imgFlagL;
            LinearLayout replyL;
            TextView reply;

            public Holder(View itemView) {
                super(itemView);
               headImg=itemView.findViewById(R.id.comment_user_img);
                flagImg1=itemView.findViewById(R.id.comment_img1);
                flagImg2=itemView.findViewById(R.id.comment_img2);
                flagImg3=itemView.findViewById(R.id.comment_img3);
                headName=itemView.findViewById(R.id.comment_user_name);
                starView=itemView.findViewById(R.id.comment_five_stars);
                date=itemView.findViewById(R.id.comment_date);
                arriveTime=itemView.findViewById(R.id.comment_arrive_time);
                content=itemView.findViewById(R.id.comment_content);
                imgFlagL=itemView.findViewById(R.id.comment_img_flag);
                replyL=itemView.findViewById(R.id.comment_replay);
                reply=itemView.findViewById(R.id.conment_reply_content);



            }
        }
    }
}
