package cn.czyugang.tcg.client.modules.aftersale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private static final String TAG = "AftersaleTrackActivity";
    @BindView(R.id.aftersale_tracks)
    RecyclerView tracksR;
    private List<AftersaleTrack> trackList = new ArrayList<>();
    private TrackAdapter adapter;

    //售后订单详情的所有信息
    private static AftersaleDetailResponse detailResponse;

    public static void startAftersaleTrackActivity(AftersaleDetailResponse response) {
        Intent intent = new Intent(getTopActivity(), AftersaleTrackActivity.class);
        MyApplication.getInstance().activityTransferData=response;
        detailResponse=response;
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailResponse=(AftersaleDetailResponse)MyApplication.getInstance().activityTransferData;
        MyApplication.getInstance().activityTransferData=null;

        setContentView(R.layout.activity_aftersale_track);
        ButterKnife.bind(this);

        initData();

     /*   trackList.add(new AftersaleTrack());
        trackList.add(new AftersaleTrack());
        trackList.add(new AftersaleTrack());
        trackList.add(new AftersaleTrack());*/

        adapter = new TrackAdapter(trackList, this);
        tracksR.setLayoutManager(new LinearLayoutManager(this));
        tracksR.setAdapter(adapter);
    }

    private void initData() {
        if(detailResponse==null){
            return;
        }
        detailResponse.parse();
        Log.e(TAG, "initData: had ran one" );
        int size=detailResponse.timeList.size();
        Log.e(TAG, "initData: had ran two" +size);
        for(int i=0;i<size;i++){
            Log.e(TAG, "initData: had ran three" );
            AftersaleTrack aftersaleTrack=new AftersaleTrack();
            aftersaleTrack.setHeadImg(detailResponse.imgList.get(i));
            Log.e(TAG, "initData: "+ detailResponse.imgList.get(i));
            aftersaleTrack.setStatu(detailResponse.statusList.get(i));
            aftersaleTrack.setStatuTitle(detailResponse.titleList.get(i));
            aftersaleTrack.setStatuTime(detailResponse.timeList.get(i));
            if(i==0 && detailResponse.buyerImgList.size()>0) aftersaleTrack.setImgList(detailResponse.buyerImgList);
            if(i==1 && detailResponse.sellerImgList1.size()>0) aftersaleTrack.setImgList(detailResponse.sellerImgList1);
            if(size>4 && i==5 && detailResponse.sellerImgList2.size()>0) aftersaleTrack.setImgList(detailResponse.sellerImgList2);
            trackList.add(aftersaleTrack);
        }
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    private static class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.Holder> {
        private List<AftersaleTrack> list;
        private Activity activity;

        public TrackAdapter(List<AftersaleTrack> list, Activity activity) {
            for(int i=0,size=list.size();i<size;i++){
                if(list.get(i).statuTime==null){
                    list.remove(i);
                    i--;
                }
            }
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
            int index=getItemCount()-position-1;
            String type=detailResponse.returnType;

            AftersaleTrack data = list.get(index);
            data.bindImgAdapter(activity, holder.imgs);

            if(index==7) holder.icon.setBackgroundResource(R.drawable.bg_dot_red);
            if(type.equals("退货退款") &&(index==5 || index==1 || index==0)){
                String reason="",explian="";
                if(index==0) {reason=detailResponse.buyerReason;explian=detailResponse.buyerExplian;}
                if (index==1){reason=detailResponse.sellerReason1;explian=detailResponse.sellerExplian1;}
                if (index==5){reason=detailResponse.sellerReason2;explian=detailResponse.sellerExplian2;}
                holder.text.setText(RichText.newRichText().
                        appendSpColorRes(data.statu, R.dimen.sp_14, R.color.main_red)
                        .append("\n"+data.statuTitle)
                        .append("\n"+"拒绝理由:"+reason)
                        .append("\n"+"补充说明:"+explian)
                        .build());
            }else if(type.equals("退款") && (index==1 || index==0)){
                String reason="",explian="";
                if(index==3 ) holder.icon.setBackgroundResource(R.drawable.bg_dot_red);
                if(index==0) {reason=detailResponse.buyerReason;explian=detailResponse.buyerExplian;}
                if (index==1){reason=detailResponse.sellerReason1;explian=detailResponse.sellerExplian1;}
                holder.text.setText(RichText.newRichText().
                        appendSpColorRes(data.statu, R.dimen.sp_14, R.color.main_red)
                        .append("\n"+data.statuTitle)
                        .append("\n"+"拒绝理由:"+reason)
                        .append("\n"+"补充说明:"+explian)
                        .build());
            }else {
                holder.text.setText(RichText.newRichText().
                        appendSpColorRes(data.statu, R.dimen.sp_14, R.color.main_red)
                        .append("\n"+data.statuTitle)
                        .build());
            }
            holder.icon.setImageResource(data.headImg);
            holder.time.setText(data.statuTime);


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
            return (position == 2 || position == 3 || position == 6 || position == 7) ?  R.layout.item_aftersale_track_img:R.layout.item_aftersale_track_text ;
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
        public void setImgList(List<String> imgList){
            this.imgList=imgList;
        }

        private List<String> imgList = null;
        private ImgAdapter imgAdapter = null;

        public void bindImgAdapter(Activity activity, RecyclerView recyclerView) {
            if (recyclerView == null || imgList==null) return;
            if (imgAdapter == null) {
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
