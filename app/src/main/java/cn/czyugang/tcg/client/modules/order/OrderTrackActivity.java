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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/29
 */

public class OrderTrackActivity extends BaseActivity {

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

    private List<Track> trackList = new ArrayList<>();

    public static void startOrderTrackActivity() {
        Intent intent = new Intent(MyApplication.getContext(), OrderTrackActivity.class);
        MyApplication.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        ButterKnife.bind(this);

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
        MyDialog.Builder.newBuilder(this)
                .custom(R.layout.dialog_call)
                .build()
                .show()
                .text(R.id.dialog_call, "13138705415")
                .onClick(R.id.dialog_call, v -> AppUtil.call(this, "13138705415"))
                .onClick(R.id.dialog_cancel);
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

    }
}
