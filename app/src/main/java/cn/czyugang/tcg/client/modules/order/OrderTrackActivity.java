package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.OrderApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Order;
import cn.czyugang.tcg.client.entity.OrderDetailResponse;
import cn.czyugang.tcg.client.entity.OrderSchedule;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/29
 */

public class OrderTrackActivity extends BaseActivity {
    private static final String TAG = "OrderTrackActivity";
    @BindView(R.id.order_track_list)
    RecyclerView trackR;
    @BindView(R.id.order_track_delivery_type)
    TextView deliveryType;
    @BindView(R.id.order_track_delivery_self)
    TextView deliverySelf;
    @BindView(R.id.order_track_delivery_time)
    TextView deliveryTime;
    @BindView(R.id.order_track_distributor_img)
    ImgView distributorImg;
    @BindView(R.id.order_track_distributor_name)
    TextView distributorName;
    @BindView(R.id.order_track_distributorL)
    LinearLayout track_person;

    public Map<String, String> deliverTypeDict = new HashMap<>();//
    private List<Track> trackList = new ArrayList<>();
   // private HashMap<String, String> statusList = new HashMap<>();
    //时间
    private List<String> orderschedule = new ArrayList<>();
    //状态
    private List<String> statuList = new ArrayList<>();
    //状态标题
    private List<String> statusTitle = new ArrayList<>();
    //图片
    private List<Integer> imgList = new ArrayList<>();
    private OrderSchedule orderScheduleO;
    private String deliverWay;
    public String deliverT;
    public String delivorName;
    public String delivorPhone;
    private String id="123123";


    public static void startOrderTrackActivity() {
        Intent intent = new Intent(getTopActivity(), OrderTrackActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        ButterKnife.bind(this);

        id=getIntent().getStringExtra("id");
        getOrderDetail("948752830892789760");



    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.order_track_distributorL)
    public void onOpenMap() {
        OrderTrackMapActivity.startOrderTrackMapActivity();
    }

    @OnClick(R.id.order_track_distributor_name)
    public void onCallDistributor() {
        MyDialog.phoneDialog(this, "13138705415");
    }

    public void getOrderDetail(String orderGoodsId) {
        OrderApi.getOrderDetail(orderGoodsId).subscribe(new NetObserver<OrderDetailResponse>() {
            @Override
            public void onNext(OrderDetailResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    OrderDetailResponse result = response;
                    Order order = result.getData();
                    initDeliverType(result);

                    deliveryType.setText(deliverTypeDict.get(order.deliveryWay));
                    deliveryTime.setText(order.isImmediately.equals("YES") ? "尽快送达" : "非立即送达");
                    if (deliverTypeDict.get(order.deliveryWay).equals("自提")) {
                        track_person.setVisibility(View.GONE);
                        deliverySelf.setText("自提券码");
                        selfTake(result);
                    } else if(deliverTypeDict.get(order.deliveryWay).equals("商家配送")){
                        deliverySelf.setText("配送时间");
                        distributorName.setText(result.values.optString("deliveryUserName")
                                + "      " + result.values.optString("deliveryUserPhone"));
                        merchantShipping(result);
                    }else if(deliverTypeDict.get(order.deliveryWay).equals("平台配送")){
                        deliverySelf.setText("配送时间");
                        distributorName.setText(result.values.optString("deliveryUserName")
                                + "      " + result.values.optString("deliveryUserPhone"));
                        Platform0Distribution(result);
                    }
                    distributorImg.drawableId(R.drawable.icon_delivery_man_white);
                   // initTrackList(result);
                    trackR.setLayoutManager(new LinearLayoutManager(OrderTrackActivity.this));
                    trackR.setAdapter(new TrackAdapter(trackList, OrderTrackActivity.this));
                }
            }
        });

    }

    private void initDeliverType(OrderDetailResponse result) {
        //配送方式
        JSONArray array = result.values.optJSONArray("deliveryWayDict");
        for (int i = 0, size = array.length(); i < size; i++) {
            JSONObject json = array.optJSONObject(i);
            deliverTypeDict.put(json.optString("id"), json.optString("name"));
            Log.e("deliverTypeDict", "parse: " + json.optString("id") + " " +
                    " " + json.optString("name"));
        }
    }
    //自提
    private void selfTake(OrderDetailResponse result) {
            initschedule1(result);
                Track track1 = new Track();
                track1.setStatu("订单提交成功");
                track1.setStatuTitle("订单编号："+id);
                track1.setHeadImg(R.drawable.icon_merchant);
                track1.setStatuTime(orderschedule.get(0));
        Track track2 = new Track();
        track2.setStatu("订单待支付");
        track2.setStatuTitle("请在提交订单后"+result.values.optString("payTimeout")+"min内完成支付");
        track2.setHeadImg(R.drawable.icon_merchant);
        track2.setStatuTime(orderschedule.get(1));
        Track track3 = new Track();
        track3.setStatu("订单已支付，券码待使用");
        track3.setStatuTitle("请前往门店出示此券码进行使用");
        track3.setHeadImg(R.drawable.icon_money_white);
        track3.setStatuTime(orderschedule.get(2));
        Track track4 = new Track();
        track4.setStatu("订单已完成");
        track4.setStatuTitle("任何意见和吐槽，欢迎随时联系我们");
        track4.setHeadImg(R.drawable.icon_merchant);
        track4.setStatuTime(orderschedule.get(3));
        trackList.add(track1);
        trackList.add(track2);
        trackList.add(track3);
        trackList.add(track4);

    }
    private void initschedule1(OrderDetailResponse result) {
        String  object = result.values.optString("orderSchedule");
        if (!CommonUtil.responseIsNull(object)) {
            orderScheduleO = JsonParse.fromJson(object, OrderSchedule.class);
        }
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.noUseTicketCode );
        orderschedule.add(orderScheduleO.evaluate);

    }

    //平台配送  /* */
    private void Platform0Distribution(OrderDetailResponse result){
        initschedule2(result);
        initStatus2(result);
        initStatusTitle2(result);
        initImaList2();
        for(int i=0;i<10;i++){
            Track track=new Track();
            track.setStatu(statuList.get(i));
            track.setStatuTitle(statusTitle.get(i));
            track.setStatuTime(orderschedule.get(i));
            track.setHeadImg(imgList.get(i));
        }



    }

    private void initImaList2() {
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_store_white);
        imgList.add(R.drawable.icon_store_white);
        imgList.add(R.drawable.icon_delivery_man_white);
        imgList.add(R.drawable.icon_delivery_man_white);
        imgList.add(R.drawable.icon_delivery_man_white);
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_merchant);


    }

    private void initStatusTitle2(OrderDetailResponse result) {
        statusTitle.add("订单编号："+id);
        statusTitle.add("请在提交订单后"+result.values.optString("payTimeout")+"min内完成支付");
        statusTitle.add("商家在"+result.values.optString("orderTimeout")+"min内未接单，将自动取消订单");
        statusTitle.add("商家正在为您准备商品，备货时间为"+result.values.optString("prepareTime")+"min");
        statusTitle.add("商家已备货完毕，待配送员接单配送");
        statusTitle.add("配送员已接单，正赶往商家店铺取货");
        statusTitle.add("配送员就到达商家店铺，等待取货");
        statusTitle.add("配送员已取货，正在进行配送");
        statusTitle.add("配送员已送达，等待用户确认收货");
        statusTitle.add("任何意见和吐槽，欢迎随时联系我们");


    }

    private void initStatus2(OrderDetailResponse result) {
        statuList.add("订单提交成功");
        statuList.add("订单待支付");
        statuList.add("订单已支付，商家待接单");
        statuList.add("订单已支付，商家已接单");
        statuList.add("订单已支付，商家已发货");
        statuList.add("订单已支付，配送员已接单");
        statuList.add("订单已支付，配送员已到店");
        statuList.add("订单已支付，配送员配送中");
        statuList.add("订单已支付，用户待确认收货");
        statuList.add("订单已完成");


    }

    private void initschedule2(OrderDetailResponse result) {
        String  object = result.values.optString("orderSchedule");
        if (!CommonUtil.responseIsNull(object)) {
            orderScheduleO = JsonParse.fromJson(object, OrderSchedule.class);
        }
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.pay );
        orderschedule.add(orderScheduleO.businessOrder );
        orderschedule.add(orderScheduleO.stockUp);
        orderschedule.add(orderScheduleO.receive);
        orderschedule.add(orderScheduleO.arriveServerPoint);
        orderschedule.add(orderScheduleO.pickup);
        orderschedule.add(orderScheduleO.confirmReceipt);
        orderschedule.add(orderScheduleO.evaluate);
    }
    /*  */

    //商家配送
    private void merchantShipping(OrderDetailResponse result){
        initschedule3(result);
        initStatus3();
        initStatusTitle3(result);
        initImaList3();
        for(int i=0;i<7;i++){
            Track track=new Track();
            track.setStatu(statuList.get(i));
            track.setStatuTitle(statusTitle.get(i));
            track.setStatuTime(orderschedule.get(i));
            track.setHeadImg(imgList.get(i));
            trackList.add(track);
        }

    }

    private void initImaList3() {
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_store_white);
        imgList.add(R.drawable.icon_store_white);
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_merchant);


    }

    private void initStatusTitle3(OrderDetailResponse result) {
        statusTitle.add("订单编号：22222277788898465");
        statusTitle.add("请在提交订单后"+result.values.optString("payTimeout")+"min内完成支付");
        statusTitle.add("商家在"+result.values.optString("orderTimeout")+"min内未接单，将自动取消订单");
        statusTitle.add("商家正在为您准备商品，备货时间为"+result.values.optString("prepareTime")+"min");
        statusTitle.add("商家已备货完毕，将由商家配送员为您配送");
        statusTitle.add("配送员已送达，等待用户确认收货");
        statusTitle.add("任何意见和吐槽，欢迎随时联系我们");


    }

    private void initStatus3() {
        statuList.add("订单提交成功");
        statuList.add("订单待支付");
        statuList.add("订单已支付，商家待接单");
        statuList.add("订单已支付，商家已接单");
        statuList.add("订单已支付，商家已发货");
        statuList.add("订单已支付，用户待确认收货");
        statuList.add("订单已完成");

    }

    private void initschedule3(OrderDetailResponse result) {
        String  object = result.values.optString("orderSchedule");
        if (!CommonUtil.responseIsNull(object)) {
            orderScheduleO = JsonParse.fromJson(object, OrderSchedule.class);
        }
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.pay );
        orderschedule.add(orderScheduleO.businessOrder );
        orderschedule.add(orderScheduleO.stockUp);
        orderschedule.add(orderScheduleO.confirmReceipt);
        orderschedule.add(orderScheduleO.evaluate);
    }


    private void initOrderScheduleSelf(OrderDetailResponse result) {
        String  object = result.values.optString("orderSchedule");
        if (!CommonUtil.responseIsNull(object)) {
            orderScheduleO = JsonParse.fromJson(object, OrderSchedule.class);
        }
        // result.values.optJSONObject("rderSchedule").optString("create");
        if (orderScheduleO.create!=null) {
            orderschedule.add(orderScheduleO.create );
            orderschedule.add(orderScheduleO.create );
            Log.e(TAG, "initOrderSchedule:create " );
        }
        if ( orderScheduleO.noUseTicketCode!=null) {
            orderschedule.add(orderScheduleO.noUseTicketCode );
            Log.e(TAG, "initOrderSchedule:pay " );
        }
        if (orderScheduleO.useTicketCode !=null) {
            orderschedule.add(orderScheduleO.useTicketCode );
            Log.e(TAG, "initOrderSchedule:businessOrder " );
        }

    }

    private void initOrderSchedule(OrderDetailResponse result) {
        String  object = result.values.optString("orderSchedule");
        if (!CommonUtil.responseIsNull(object)) {
            orderScheduleO = JsonParse.fromJson(object, OrderSchedule.class);
        }
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.pay );
        orderschedule.add(orderScheduleO.businessOrder );
        orderschedule.add(orderScheduleO.stockUp);
        orderschedule.add(orderScheduleO.confirmReceipt );
        orderschedule.add(orderScheduleO.evaluate);

    }


    private static class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.Holder> {
        private List<Track> list;
        private Activity activity;
        private int removeNum;

        public TrackAdapter(List<Track> list, Activity activity) {
            for(int i=0,size=list.size();i<size;i++){
                if(list.get(i).statuTime==null)
                    removeNum++;
            }
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_order_track, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            int index=getItemCount()-position-1;
            Track data = list.get(index);
            if(data.statuTime==null){
                return;
            }
            if (position == 0) {
                CommonUtil.setMarginTop(holder.itemView, R.dimen.dp_20);
                CommonUtil.setMarginBottom(holder.itemView, R.dimen.dp_1);

            } else if (position == getItemCount() - 1) {
                CommonUtil.setMarginTop(holder.itemView, R.dimen.dp_1);
                CommonUtil.setMarginBottom(holder.itemView, R.dimen.dp_40);
            } else {
                CommonUtil.setMarginTop(holder.itemView, R.dimen.dp_1);
                CommonUtil.setMarginBottom(holder.itemView, R.dimen.dp_1);
            }
            holder.icon.setImageResource(data.headImg);
            holder.status.setText(data.statu);
            holder.statusTitle.setText(data.statuTitle);
            holder.time.setText(data.statuTime);
        }

        @Override
        public int getItemCount() {
            return list.size()-removeNum;
        }

        class Holder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView time;
            TextView statusTitle;
            TextView status;

            public Holder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.item_img);
                time = itemView.findViewById(R.id.item_time);
                statusTitle = itemView.findViewById(R.id.item_status_title);
                status = itemView.findViewById(R.id.item_status);
            }
        }
    }

    private static class Track {

        public int headImg;
        public String statu;
        public String statuTitle;
        public String statuTime;


        public void setHeadImg(int headImg) {
            this.headImg = headImg;
        }

        public void setStatu(String statu) {
            this.statu = statu;
        }

        public void setStatuTitle(String statuTitle) {
            this.statuTitle = statuTitle;
        }

        public void setStatuTime(String statuTime) {
            this.statuTime = statuTime;
        }


    }
}
