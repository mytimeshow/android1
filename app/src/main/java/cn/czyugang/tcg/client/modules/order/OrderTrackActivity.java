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
import cn.czyugang.tcg.client.entity.DeliveryOrder;
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

    public Map<String, String> deliverTypeDict = new HashMap<>();
    private List<Track> trackList = new ArrayList<>();
    //时间
    private List<String> orderschedule = new ArrayList<>();
    //状态
    private List<String> statuList = new ArrayList<>();
    //状态标题
    private List<String> statusTitle = new ArrayList<>();
    //图片
    private List<Integer> imgList = new ArrayList<>();
    private OrderSchedule orderScheduleO;
    private DeliveryOrder deliveryOrder;
    private String deliverWay;
    //订单id
    private String id="123123";


    public static void startOrderTrackActivity(String id) {
        Intent intent = new Intent(getTopActivity(), OrderTrackActivity.class);
        intent.putExtra("id",id);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        ButterKnife.bind(this);

        id=getIntent().getStringExtra("id");
        getOrderDetail(id);
        //948752830892789760

    }

    @OnClick(R.id.title_back)
    public void onBack() {
       // GroupGoodActivity.startGroupGoodActivity("","");
       finish();
       // getOrderDetail("950247191308587008");
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
                    deliverWay=deliverTypeDict.get(order.deliveryWay);
                    deliveryType.setText(deliverTypeDict.get(order.deliveryWay));

                    if (deliverTypeDict.get(order.deliveryWay).equals("自提")) {
                        JSONObject fetchCode=result.values.optJSONObject("deliveryOrder");
                        deliveryOrder=JsonParse.fromJson(fetchCode.toString(),DeliveryOrder.class);
                        track_person.setVisibility(View.GONE);
                        deliverySelf.setText("自提券码");
                        deliveryTime.setText(deliveryOrder.fetchCode+"（已使用）");
                        distributorName.setText(deliveryOrder.fetchCode);
                        selfTake(result);
                    } else if(deliverTypeDict.get(order.deliveryWay).equals("商家配送")){
                        deliverySelf.setText("配送时间");
                        deliveryTime.setText(order.isImmediately.equals("YES") ? "尽快送达" : "非立即送达");
                        distributorName.setText(result.values.optString("deliveryUserName")
                                + "      " + result.values.optString("deliveryUserPhone"));
                        merchantShipping(result);
                    }else if(deliverTypeDict.get(order.deliveryWay).equals("平台配送")){
                        deliverySelf.setText("配送时间");
                        deliveryTime.setText(order.isImmediately.equals("YES") ? "尽快送达" : "非立即送达");
                        distributorName.setText(result.values.optString("deliveryUserName")
                                + "      " + result.values.optString("deliveryUserPhone"));
                        Platform0Distribution(result);
                    }
                    distributorImg.drawableId(R.drawable.icon_delivery_man_white);
                    trackR.setLayoutManager(new LinearLayoutManager(OrderTrackActivity.this));
                    trackR.setAdapter(new TrackAdapter(trackList, OrderTrackActivity.this,deliverWay));
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
        statuList.clear();
        statusTitle.clear();
        imgList.clear();
        orderschedule.clear();
        initschedule1(result);
        initStatus1();
        initStatusTitle1(result); 
        initImaList1();
        for(int i=0;i<4;i++){
            Track track=new Track();
            track.setStatu(statuList.get(i));
            track.setStatuTitle(statusTitle.get(i));
            track.setStatuTime(orderschedule.get(i));
            track.setHeadImg(imgList.get(i));
            trackList.add(track);
        }
      

    }

    private void initImaList1() {
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_merchant);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_paid_white);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_money_white);
        imgList.add(R.drawable.icon_paid_white);
        imgList.add(R.drawable.icon_paid_white);
    }

    private void initStatusTitle1(OrderDetailResponse result) {
        statusTitle.add("订单编号："+id);
        statusTitle.add("请在提交订单后"+result.values.optString("payTimeout")+"min内完成支付");
        statusTitle.add("请前往门店出示此券码进行使用");
        statusTitle.add("任何意见和吐槽，欢迎随时联系我们");
        statusTitle.add("退款申请:商品【AAAAAAAAAAAAAA】X3，等待商家确认");
        statusTitle.add("商家拒绝了退款申请，您可联系客服介入");
        statusTitle.add("请耐心等待客服处理结果");
        statusTitle.add("经平台仲裁，此次退款申请给予通过");
        statusTitle.add("经平台仲裁，此次退款申请不予通过");
        statusTitle.add("任何意见和吐槽，欢迎随时联系我们");
        statusTitle.add("任何意见和吐槽，欢迎随时联系我们");

    }

    private void initStatus1() {
        statuList.add("订单提交成功");
        statuList.add("订单待支付");
        statuList.add("订单已支付，券码待使用");
        statuList.add("订单已完成");
        statuList.add("订单退款中，用户申请退款");
        statuList.add("订单退款中，商家拒绝退款申请");
        statuList.add("订单退款中，用户申请平台介入");
        statuList.add("订单退款已关闭，申请被驳回");
        statuList.add("订单退款成功，申请通过");
        statuList.add("订单已退款");
        statuList.add("订单已完成");
    }

    private void initschedule1(OrderDetailResponse result) {
        String  object = result.values.optString("orderSchedule");
        if (!CommonUtil.responseIsNull(object)) {
            orderScheduleO = JsonParse.fromJson(object, OrderSchedule.class);
        }
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.create );
        orderschedule.add(orderScheduleO.pay );
        orderschedule.add(orderScheduleO.useTicketCode);


    }

    //平台配送
    private void Platform0Distribution(OrderDetailResponse result){
        statuList.clear();
        statusTitle.clear();
        imgList.clear();
        orderschedule.clear();
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
            trackList.add(track);
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
        imgList.add(R.drawable.icon_paid_white);


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
        statuList.clear();
        statusTitle.clear();
        imgList.clear();
        orderschedule.clear();
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
        imgList.add(R.drawable.icon_paid_white);


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

    private  class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.Holder> {
        private List<Track> list;
        private Activity activity;
        private String  type;

        public TrackAdapter(List<Track> list, Activity activity,String  type) {
            for(int i=0;i<list.size();i++){
                if(list.get(i).statuTime==null){
                    list.remove(i);
                    i--;
                }
            }
            this.list = list;
            this.activity = activity;
            this.type=type;


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
            if(type.equals("自提")){
                if(getItemCount()==4  && index==3 ){
                    holder.icon.setBackgroundResource(R.drawable.bg_dot_red);
                }else if(getItemCount()==9  && index==8){
                    holder.icon.setBackgroundResource(R.drawable.bg_dot_red);
                }
            }else if(type.equals("商家配送")){
                    if(getItemCount()==7 && index==6){
                        holder.icon.setBackgroundResource(R.drawable.bg_dot_red);
                    }
            }else {
                if(getItemCount()==10 && index==9){
                    holder.icon.setBackgroundResource(R.drawable.bg_dot_red);
                }

            }

            holder.icon.setImageResource(data.headImg);
            holder.status.setText(data.statuTitle);
            holder.statusTitle.setText(data.statu);
            holder.time.setText(data.statuTime);
        }

        @Override
        public int getItemCount() {
            Log.e(TAG, "getItemCount: "+ list.size());
            return list.size();
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
