package cn.czyugang.tcg.client.modules.aftersale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import cn.czyugang.tcg.client.entity.AftersaleDetailResponse;
import cn.czyugang.tcg.client.modules.common.ImgAdapter;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.string.RichText;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 售后追踪
 */

public class AftersaleTrackActivity extends BaseActivity {
    @BindView(R.id.aftersale_tracks)
    RecyclerView tracksR;
    private List<AftersaleTrack> trackList = new ArrayList<>();
    private TrackAdapter adapter;

    //售后订单详情的所有信息
    private AftersaleDetailResponse detailResponse;

    public static void startAftersaleTrackActivity(AftersaleDetailResponse response) {
        Intent intent = new Intent(getTopActivity(), AftersaleTrackActivity.class);
        MyApplication.getInstance().activityTransferData=response;
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailResponse=(AftersaleDetailResponse)MyApplication.getInstance().activityTransferData;
        MyApplication.getInstance().activityTransferData=null;

        setContentView(R.layout.activity_aftersale_track);
        ButterKnife.bind(this);

        trackList.add(new AftersaleTrack());
        trackList.add(new AftersaleTrack());
        trackList.add(new AftersaleTrack());
        trackList.add(new AftersaleTrack());

        adapter = new TrackAdapter(trackList, this);
        tracksR.setLayoutManager(new LinearLayoutManager(this));
        tracksR.setAdapter(adapter);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    private static class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.Holder> {
        private List<AftersaleTrack> list;
        private Activity activity;

        public TrackAdapter(List<AftersaleTrack> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            AftersaleTrack data = list.get(position);
            data.bindImgAdapter(activity, holder.imgs);
            holder.text.setText(RichText.newRichText().
                    appendSpColorRes("订单退款成功", R.dimen.sp_14, R.color.main_red)
                    .append("\n经平台仲裁，此次退款申请予以通过")
                    .build());

            if (position==0){
                holder.itemView.setOnClickListener(v -> {
                    RefundStatusActivity.startRefundStatusActivity();
                });
            }else {
                holder.itemView.setOnClickListener(null);
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
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 0 ? R.layout.item_aftersale_track_text : R.layout.item_aftersale_track_img;
        }

        class Holder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView time;
            TextView text;
            RecyclerView imgs;

            public Holder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.item_img);
                time = itemView.findViewById(R.id.item_time);
                text = itemView.findViewById(R.id.item_text);
                imgs = itemView.findViewById(R.id.item_imgs);
            }
        }
    }

    private static class AftersaleTrack {

        private List<String> imgList = null;
        private ImgAdapter imgAdapter = null;

        public void bindImgAdapter(Activity activity, RecyclerView recyclerView) {
            if (recyclerView == null) return;
            if (imgAdapter == null) {
                imgList = new ArrayList<>();
                imgList.add("325789");
                imgList.add("325789");
                imgList.add("325789");
                imgList.add("325789");
                imgList.add("325789");
                imgList.add("325789");
                imgList.add("325789");
                imgAdapter = new ImgAdapter(imgList, activity);
                imgAdapter.setSizeRes(R.dimen.dp_60);
            }
            if (recyclerView.getLayoutManager() == null) {
                recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
                recyclerView.addItemDecoration(new RecycleViewDivider(activity, -1,
                        ResUtil.getDimenInPx(R.dimen.dp_5), ResUtil.getColor(R.color.white)));
            }
            recyclerView.setAdapter(imgAdapter);
        }
    }
}
