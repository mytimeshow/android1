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
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
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
    private HashMap<String, String> statusList = new HashMap<>();
    private List<String> orderschedule = new ArrayList<>();
    private String deliverWay;
    public String deliverT;
    public String delivorName;
    public String delivorPhone;


    public static void startOrderTrackActivity() {
        Intent intent = new Intent(getTopActivity(), OrderTrackActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        ButterKnife.bind(this);
        getOrderDetail("948752830892789760");
        for (int i = 0; i < 10; i++) {
            trackList.add(new Track());
        }

        trackR.setLayoutManager(new LinearLayoutManager(this));
        trackR.setAdapter(new TrackAdapter(trackList, this));
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

                    // deliverWay=result.deliverTypeDict.get(order.deliveryWay);
                    deliveryType.setText(deliverTypeDict.get(order.deliveryWay));
                    Log.e(TAG, "onNext: " + order.deliveryWay + deliverWay);
                    deliveryTime.setText(order.isImmediately.equals("YES") ? "尽快送达" : "非立即送达");
                    if (deliverWay != null && deliverWay.equals("自提")) {
                        track_person.setVisibility(View.GONE);
                        deliverySelf.setText("自提券码");
                    } else {
                        deliverySelf.setText("配送时间");
                        Log.e(TAG, "onNext: distributorName");
                        distributorName.setText(result.values.optString("deliveryUserName")
                                + "      " + result.values.optString("deliveryUserPhone"));
                    }
                   // Glide.with(OrderTrackActivity.this).load(R.drawable.icon_delivery_man_white)
                   //         .into(track_person_img);
                    distributorImg.drawableId(R.drawable.icon_delivery_man_white);

                    //initTrackList(result);

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

    private void initTrackList(OrderDetailResponse result) {
        initOrderSchedule(result);
        Order order = result.getData();
        for (int i = 0, size = result.statusMap.size(); i < size; i++) {
            if (true) {
                Track track = new Track();
                track.setStatu(result.statusMap.get(order.status));
            }

        }


    }

    private void initOrderSchedule(OrderDetailResponse result) {
        JSONArray jsonArray = result.values.optJSONArray("orderSchedule");
        for (int i = 0, size = jsonArray.length(); i < size; i++) {
            JSONObject object = jsonArray.optJSONObject(i);
            if (object.optString("create") != null) {
                orderschedule.add(object.optString("create"));
                orderschedule.add(object.optString("create"));
            } else if ( result.values.optString("waitPayTime") != null) {
                orderschedule.add(object.optString("create"));
            } else if (object.optString("pay") != null) {
                orderschedule.add(object.optString("pay"));
            } else if (object.optString("businessOrder") != null) {
                orderschedule.add(object.optString("businessOrder"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            } else if (object.optString("noUseTicketCode") != null) {
                orderschedule.add(object.optString("noUseTicketCode"));
            }

        }

    }


    private static class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.Holder> {
        private List<Track> list;
        private Activity activity;

        public TrackAdapter(List<Track> list, Activity activity) {
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
            Track data = list.get(position);

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
        }

        @Override
        public int getItemCount() {
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

        public String headImg;
        public String statu;
        public String statuTitle;
        public String statuTime;


        public void setHeadImg(String headImg) {
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
