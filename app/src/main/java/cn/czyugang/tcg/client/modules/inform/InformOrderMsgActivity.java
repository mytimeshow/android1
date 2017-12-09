package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.FollowCotent;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * Created by Administrator on 2017/12/9.
 */

public class InformOrderMsgActivity extends BaseActivity{

    @BindView(R.id.inform_for_order_list)
    RecyclerView informOrderList;
    @BindView(R.id.inform_appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.inform_title_bg)
    FrameLayout frameTitle;

    @BindView(R.id.inform_order_name)
    TextView orderName;

    public static void startInformOrderMsgActivity( ){
        Intent intent=new Intent(getTopActivity(),InformOrderMsgActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_order_msg);
        ButterKnife.bind(this);

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            LogRui.i("onCreate####"+verticalOffset+"            "+Math.abs(verticalOffset)/appBarLayout.getHeight());
            frameTitle.setAlpha((float)(Math.abs(verticalOffset))/((float)(appBarLayout.getHeight())));
            orderName.setAlpha(1-(Math.abs(verticalOffset))/(appBarLayout.getHeight()));


        });

        List<Inform> list=new ArrayList<Inform>();
        Inform informColumn=new Inform();
        Inform informColumn2=new Inform();
        informColumn.setContent("行走的鸡腿");
        informColumn2.setContent("天天吃吃吃");
        list.add(informColumn);
        list.add(informColumn2);

        SmallInformAdapter smallInformAdapter=new SmallInformAdapter(list,this);
        informOrderList.setLayoutManager(new LinearLayoutManager(this));
        informOrderList.setAdapter(smallInformAdapter);
    }

    static class SmallInformAdapter extends RecyclerView.Adapter<InformOrderMsgActivity.SmallInformAdapter.Holder> {
        private List<Inform> list;
        private Activity activity;

        public SmallInformAdapter(List<Inform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public InformOrderMsgActivity.SmallInformAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InformOrderMsgActivity.SmallInformAdapter.Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_news_small ,parent,false));
        }
        @Override
        public void onBindViewHolder(SmallInformAdapter.Holder holder, int position) {
            Inform informSmallColumnData =  list.get(position);
            holder.informSmallContent.setText(informSmallColumnData.getContent());
        }
        @Override
        public int getItemCount() {
            return list.size();
        }


        class Holder extends RecyclerView.ViewHolder {
            ImgView informSmallImg;
            ImgView informSmallHead;
            TextView informSmallContent;
            TextView informSmallName;
            TextView informSmallCommitNum;
            public Holder(View itemView) {
                super(itemView);
                //资讯小图item
                informSmallImg = itemView.findViewById(R.id.inform_news_large_img);
                informSmallHead = itemView.findViewById(R.id.inform_news_small_head);
                informSmallContent = itemView.findViewById(R.id.inform_news_small_content);
                informSmallName = itemView.findViewById(R.id.inform_news_small_name);
                informSmallCommitNum = itemView.findViewById(R.id.inform_news_small_commit_num);
            }
        }
    }

 /*   static class LargeInformAdapter extends RecyclerView.Adapter<InformOrderMsgActivity.LargeInformAdapter.Holder> {
        private List<Inform> list;
        private Activity activity;

        public LargeInformAdapter(List<Inform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public InformOrderMsgActivity.LargeInformAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InformOrderMsgActivity.LargeInformAdapter.Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_news_large ,parent,false));
        }
        @Override
        public void onBindViewHolder(LargeInformAdapter.Holder holder, int position) {
            Inform informSmallColumnData =  list.get(position);
            holder.informLargePersonName.setText(informSmallColumnData.getName());
        }
        @Override
        public int getItemCount() {
            return list.size();
        }


        class Holder extends RecyclerView.ViewHolder {
            ImgView informLargeImg;
            ImgView informLargeHead;
            TextView informLargeContent;
            TextView informLargeContentName;
            TextView informLargePersonName;
            TextView informLargeCommitNum;
            public Holder(View itemView) {
                super(itemView);
                //资讯大图item
                informLargeImg = itemView.findViewById(R.id.inform_news_large_img);
                informLargeHead = itemView.findViewById(R.id.inform_news_large_head);
                informLargeContent = itemView.findViewById(R.id.inform_news_large_content);
                informLargeContentName = itemView.findViewById(R.id.inform_news_large_cotentname);
                informLargePersonName = itemView.findViewById(R.id.inform_news_large_name);
                informLargeCommitNum = itemView.findViewById(R.id.inform_news_large_commitNum);
            }
        }
    }*/


}