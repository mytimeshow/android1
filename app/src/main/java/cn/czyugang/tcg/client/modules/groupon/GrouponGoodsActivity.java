package cn.czyugang.tcg.client.modules.groupon;

import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/7
 * <p>
 * 降价拍 商品详情
 */

public class GrouponGoodsActivity extends BaseActivity {
   /* private static final String TAG = "GrouponGoodsActivity";
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
//    @BindView(R.id.groupon_goods_five_star)
//    FiveStarView fiveStar;
//    @BindView(R.id.groupon_goods_comment_num)
//    TextView commentNum;
  *//*  @BindView(R.id.groupon_goods_comment_label)
    LabelLayout commentLabel;
    @BindView(R.id.groupon_goods_pull_detail)
    TextView pullToDetail;*//*
    @BindView(R.id.groupon_goods_buy)
    TextView buy;
    @BindView(R.id.groupon_goods_open_group)
    TextView openGroup;

    private List<GrouponGroup> groupList = new ArrayList<>();
    private GroupsAdapter adapter;
    private ReduceProduct products;
    private List<String> imgList;
    //private List<ReduceProduct.LabelListBean> labelListBeans;
    //当前商品的组团数
   // private List<ReduceProduct.GroupListBean> groupListBeans;

    public static void startGrouponGoodsActivity() {
        Intent intent = new Intent(getTopActivity(), GrouponGoodsActivity.class);
        getTopActivity().startActivity(intent);
        Log.e(TAG, "had found: "+ getTopActivity());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupon_goods);
        ButterKnife.bind(this);

        //getReduceProduct("940506493684461569");
        getReduceProduct("940511454422032384","940878770548682753");
        groupList.add(new GrouponGroup());
        groupList.add(new GrouponGroup());
        groupList.add(new GrouponGroup());



     *//*   scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
        });*//*

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
        GrouponDetailActivity.startGrouponDetailActivity();
    }
    
    @OnClick(R.id.groupon_goods_open_group)
    public void onOpenGroup(){
    startActivity(new Intent(GrouponGoodsActivity.this,GroupGoodActivity.class));

    }
    //获取降价拍商品
    public void getReduceProduct(String productid,String activityid){
        ReduceProductApi.getReducesProduct(productid,activityid).subscribe(new NetObserver<Response<ReduceProduct>>() {
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
                    groupR.setLayoutManager(new LinearLayoutManager(GrouponGoodsActivity.this));
                    groupR.setAdapter(adapter);
                    groupR.setNestedScrollingEnabled(false);

                }
            }
        });

    }

    private void initData(ReduceProduct products) {
        initImgs();
        initGroupList(products);
       *//* name.setText(products.productTitle);
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
        Log.e(TAG, "initData: "+strList.get(0)+" /n"+strList.get(1) );*//*
       // commentLabel.setTextList(commentLabel,strList);
    }

    private void initGroupList(ReduceProduct products) {
           // groupListBeans=products.groupList;

    }

    private void initImgs() {
        multiImg.setImgIds(imgList);
    }

    private static class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Holder> {
     //   private List<ReduceProduct.GroupListBean> list;
        private Activity activity;

//        public GroupsAdapter(List<ReduceProduct.GroupListBean> list, Activity activity) {
//            this.list = list;
//            this.activity = activity;
//        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_groupon_group, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
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
            return  2;*//*ist==null ?3:list.size();*//*
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
*/
}
