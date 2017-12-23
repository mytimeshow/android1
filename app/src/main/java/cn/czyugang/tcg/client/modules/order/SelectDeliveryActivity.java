package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.OrderPreSettleResponse;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.modules.common.ImgAdapter;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.string.TimeUtils;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class SelectDeliveryActivity extends BaseActivity {
    @BindView(R.id.select_delivery_store_name)
    TextView storeName;
    @BindView(R.id.select_delivery_goods)
    RecyclerView goodsR;
    @BindView(R.id.select_delivery_platform)
    TextView deliveryPlatform;
    @BindView(R.id.select_delivery_self)
    TextView deliverySelf;
    @BindView(R.id.select_delivery_time)
    TextView deliveryTime;
    @BindView(R.id.select_delivery_rest)
    TextView deliveryRest;
    @BindView(R.id.select_delivery_change)
    TextView changeTime;
    @BindView(R.id.select_delivery_time_day)
    RecyclerView timeDay;
    @BindView(R.id.select_delivery_time_hour)
    RecyclerView timeHour;
    @BindView(R.id.select_delivery_timeL)
    LinearLayout timeL;

    private Store store;
    private OrderPreSettleResponse.StoreMoreInfo storeMoreInfo;
    private String selectedWay = "";
    private String selectedTime = "";
    private DayAdapter dayAdapter;
    private HourAdapter hourAdapter;


    public static void startSelectDeliveryActivity(Activity activity, int requestCode, Store store, OrderPreSettleResponse.StoreMoreInfo storeMoreInfo) {
        Intent intent = new Intent(activity, SelectDeliveryActivity.class);
        MyApplication.getInstance().activityTransferData = store;
        MyApplication.getInstance().activityTransferDataTwo = storeMoreInfo;
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = (Store) MyApplication.getInstance().activityTransferData;
        storeMoreInfo = (OrderPreSettleResponse.StoreMoreInfo) MyApplication.getInstance().activityTransferDataTwo;
        MyApplication.getInstance().activityTransferData = null;
        MyApplication.getInstance().activityTransferDataTwo = null;
        selectedWay = storeMoreInfo.selectedDeliveryWay;
        selectedTime = storeMoreInfo.selectedDeliveryTime;

        setContentView(R.layout.activity_select_delivery);
        ButterKnife.bind(this);

        storeName.setText(store.name);
        goodsR.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        goodsR.setAdapter(new ImgAdapter(storeMoreInfo.imgList, this).setSizeRes(R.dimen.dp_60));


        initDeliveryWay();

        if (!storeMoreInfo.arrowDeliveryPlatform() && !storeMoreInfo.arrowDeliveryStore()) {
            disableDeliveryWay(deliveryPlatform);
        }
        if (!storeMoreInfo.arrowDeliveryFETCH()) {
            disableDeliveryWay(deliverySelf);
        }
        if (storeMoreInfo.platformDeliveryRemainTime > 0) {
            deliveryRest.setText(String.format("平台配送还有 %s 休息，欲购从速喔~",
                    TimeUtils.transfer(storeMoreInfo.platformDeliveryRemainTime * 1000, "HH:mm ")));
        } else {
            deliveryRest.setVisibility(View.GONE);
        }

        dayAdapter = new DayAdapter(storeMoreInfo.deliveryTimeList, this);
        hourAdapter = new HourAdapter();
        timeDay.setLayoutManager(new LinearLayoutManager(this));
        timeDay.setAdapter(dayAdapter);
        timeHour.setLayoutManager(new LinearLayoutManager(this));
        timeHour.setAdapter(hourAdapter);
        hourAdapter.notify(storeMoreInfo.deliveryTimeList.get(0).date, storeMoreInfo.deliveryTimeList.get(0).price);
    }

    private void initDeliveryWay() {
        switch (selectedWay) {
            case "平台配送":
                selectDeliveryWay(deliveryPlatform);
                unSelectDeliveryWay(deliverySelf);
                deliveryPlatform.setText("平台配送");
                break;
            case "商家配送":
                selectDeliveryWay(deliveryPlatform);
                unSelectDeliveryWay(deliverySelf);
                deliveryPlatform.setText("商家配送");
                break;
            case "自提":
                selectDeliveryWay(deliverySelf);
                unSelectDeliveryWay(deliveryPlatform);
                deliveryPlatform.setText(storeMoreInfo.arrowDeliveryPlatform() ? "平台配送" : "商家配送");
                break;
        }
    }

    private void disableDeliveryWay(TextView textView) {
        textView.setTextColor(ResUtil.getColor(R.color.grey_350));
        textView.setBackgroundResource(R.drawable.border_rect_grey);
        textView.setClickable(false);
    }

    private void selectDeliveryWay(TextView textView) {
        textView.setTextColor(ResUtil.getColor(R.color.main_red));
        textView.setBackgroundResource(R.drawable.border_rect_red);
    }

    private void unSelectDeliveryWay(TextView textView) {
        textView.setTextColor(ResUtil.getColor(R.color.text_black));
        textView.setBackgroundResource(R.drawable.border_rect_grey);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.select_delivery_confirm)
    public void onConfirm() {
        if (selectedTime.split(":").length==2) selectedTime=selectedTime+":00";
        storeMoreInfo.selectedDeliveryWay = selectedWay;
        storeMoreInfo.selectedDeliveryTime = selectedTime;
        Intent intent = new Intent();
        intent.putExtra("deliveryWay", selectedWay);
        intent.putExtra("deliveryTime", selectedTime);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.select_delivery_change)
    public void onChangeTime() {
        if (timeL.getVisibility() == View.GONE) {
            timeL.setVisibility(View.VISIBLE);
            changeTime.setText("确定");
        } else {
            timeL.setVisibility(View.GONE);
            changeTime.setText("修改");
        }
    }


    @OnClick(R.id.select_delivery_self)
    public void onDeliverySelf() {
        selectedWay = "自提";
        initDeliveryWay();
    }

    @OnClick(R.id.select_delivery_platform)
    public void onDeliveryPlatform() {
        selectedWay = deliveryPlatform.getText().toString();
        initDeliveryWay();
    }

    public void refreshTime() {
        if (hourAdapter.selectTime.contains("尽快送达")){
            selectedTime=storeMoreInfo.deliveryTimeList.get(0).date;
            deliveryTime.setText(hourAdapter.selectTime);
        }else {
            selectedTime = dayAdapter.selectDate + " " + hourAdapter.selectTime;
            deliveryTime.setText(selectedTime);
        }
    }

    private class DayAdapter extends RecyclerView.Adapter<DayAdapter.Holder> {
        private List<String> list=new ArrayList<>();
        private List<OrderPreSettleResponse.DeliveryTime> originList;
        private List<String> dateList = new ArrayList<>();
        private Activity activity;
        private String selectDateStr = "";
        public String selectDate = "";

        public DayAdapter(List<OrderPreSettleResponse.DeliveryTime> originList, Activity activity) {
            this.originList = originList;
            this.activity = activity;
            if (originList.size() == 0) return;

            String[] times = originList.get(0).date.split(" ");
            String[] dates = times[0].split("-");
            int year = Integer.valueOf(dates[0]);
            int mon = Integer.valueOf(dates[1]);
            int day = Integer.valueOf(dates[2]);
            boolean isToday = false;
            Calendar calendar = Calendar.getInstance();
            if (mon == (calendar.get(Calendar.MONTH) + 1) && day == calendar.get(Calendar.DAY_OF_MONTH))
                isToday = true;
            for (OrderPreSettleResponse.DeliveryTime time : originList) {
                String s = time.date;
                if (isToday) {
                    list.add(String.format("今天[%s]", getWeekStr(calendar.get(Calendar.DAY_OF_WEEK))));
                    times = s.split(" ");
                    dateList.add(times[0]);
                } else {
                    times = s.split(" ");
                    dateList.add(times[0]);
                    dates = times[0].split("-");
                    year = Integer.valueOf(dates[0]);
                    mon = Integer.valueOf(dates[1]);
                    day = Integer.valueOf(dates[2]);
                    calendar.set(year, mon, day);
                    list.add(String.format("%02d月%02d日[%s]", mon, day, getWeekStr(calendar.get(Calendar.DAY_OF_WEEK))));
                }
                isToday = false;
            }
            if (!list.isEmpty()) {
                selectDateStr = list.get(0);
                selectDate = dateList.get(0);
            }
        }

        private String getWeekStr(int weekday) {
            switch (weekday) {
                case Calendar.SUNDAY:
                    return "周日";
                case Calendar.MONDAY:
                    return "周一";
                case Calendar.TUESDAY:
                    return "周二";
                case Calendar.WEDNESDAY:
                    return "周三";
                case Calendar.THURSDAY:
                    return "周四";
                case Calendar.FRIDAY:
                    return "周五";
                case Calendar.SATURDAY:
                    return "周六";
            }
            return "周";
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_select_delivery_day, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String data = list.get(position);
            holder.name.setText(data);
            if (data.equals(selectDateStr)) {
                holder.name.setTextColor(ResUtil.getColor(R.color.main_red));
                holder.itemView.setBackgroundResource(R.color.white);
            } else {
                holder.name.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
                holder.itemView.setBackgroundResource(R.color.bg);
            }
            holder.itemView.setOnClickListener(v -> {
                selectDateStr = list.get(position);
                selectDate = dateList.get(position);
                hourAdapter.notify(originList.get(position).date, originList.get(position).price);
                refreshTime();
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView name;

            public Holder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
            }
        }
    }

    private class HourAdapter extends RecyclerView.Adapter<HourAdapter.Holder> {

        private List<String> hourList = new ArrayList<>();
        private String price = "￥0";
        public String selectTime = "";
        public String quicklyTime = "";

        public HourAdapter() {
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_select_delivery_hour, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String data = hourList.get(position);
            holder.name.setText(data);
            holder.price.setText(price);
            if (data.equals(selectTime)) {
                holder.name.setTextColor(ResUtil.getColor(R.color.main_red));
                holder.price.setTextColor(ResUtil.getColor(R.color.main_red));
            } else {
                holder.name.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
                holder.price.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            }

            holder.itemView.setOnClickListener(v -> {
                selectTime = data;
                notifyDataSetChanged();
                refreshTime();
            });
        }

        @Override
        public int getItemCount() {
            return hourList.size();
        }

        public void notify(String deliveryDate, double price) {
            hourList.clear();
            String[] times = deliveryDate.split(" ");
            String[] dates = times[0].split("-");
            String[] hours = times[1].split(":");
            int mon = Integer.valueOf(dates[1]);
            int day = Integer.valueOf(dates[2]);
            int startHour = Integer.valueOf(hours[0]);
            int startMin = Integer.valueOf(hours[1]);
            Calendar calendar = Calendar.getInstance();
            if (mon == (calendar.get(Calendar.MONTH) + 1) && day == calendar.get(Calendar.DAY_OF_MONTH)) {
                /*
                *   当天
                *   现在时间9:15，延迟1小时10:15
                *
                *   delivery    10:00   --> 从 now 开始
                *   now         10:15
                *   delivery    10:30   --> 从 delivery 开始
                *
                * */
                quicklyTime = String.format("%02d:%02d", startHour, startMin);
                int nowHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
                int nowMin = calendar.get(Calendar.MINUTE);
                if (startHour < nowHour || (startHour == nowHour && startMin < nowMin)) {
                    startHour = nowHour;
                    startMin = nowMin;
                }

                nowHour--;
                nowMin += 30;
                if (nowMin >= 60) {
                    nowMin -= 60;
                    nowHour++;
                }
                hourList.add(String.format("尽快送达 | 大约%02d:%02d送达", nowHour, nowMin));
            }


            while (startHour < 24) {
                hourList.add(String.format("%02d:%02d", startHour, startMin));
                startMin += 15;
                if (startMin >= 60) {
                    startMin -= 60;
                    startHour++;
                }
            }


            this.price = String.format("￥%.2f", price);
            if (hourList.size() > 0) selectTime = hourList.get(0);
            notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView name;
            TextView price;

            public Holder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                price = itemView.findViewById(R.id.item_price);
            }
        }
    }

}
