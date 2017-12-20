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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.MyInform;
import cn.czyugang.tcg.client.entity.MyInformResponse;
import cn.czyugang.tcg.client.modules.store.SearchActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * Created by Administrator on 2017/12/8.
 */

public class InformMySelfActivity extends BaseActivity {

    @BindView(R.id.inform_for_myself_list)
    RecyclerView informForMyselfList;
    @BindView(R.id.inform_appbar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.myself_title_bg)
    FrameLayout frameTitle;
    @BindView(R.id.myself_name)
    TextView mySelfName;
    @BindView(R.id.myself_description)
    TextView mySelfDescription;
    @BindView(R.id.myself_follow_num)
    TextView mySelfFollowNum;
    @BindView(R.id.myself_fans_num)
    TextView mySelfFansNum;
    @BindView(R.id.myself_article_num)
    TextView mySelfArticleNum;

    @BindView(R.id.myself_head)
    ImgView mySelfHead;


    List<MyInform> myInforms=new ArrayList<MyInform>();
    MyInformAdapter myInformAdapter;
   
    public static void startMySelfActivity( ){
        Intent intent=new Intent(getTopActivity(),InformMySelfActivity.class);
        getTopActivity().startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_myself);
        ButterKnife.bind(this);

        mySelfHead.id(UserOAuth.getUserPhotoId());

        refreshInform(true);




        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            LogRui.i("onCreate####"+verticalOffset+"            "+Math.abs(verticalOffset)/appBarLayout.getHeight());
            frameTitle.setAlpha((float)(Math.abs(verticalOffset))/((float)(appBarLayout.getHeight())));
        });
    }

    private void refreshInform(boolean firstLoad) {
        InformApi.getInformByMyself().subscribe(new NetObserver<MyInformResponse>() {
            @Override
            public void onNext(MyInformResponse response) {
                super.onNext(response);
                //Toast.makeText(InformMySelfActivity.this,response.data.toString(),Toast.LENGTH_SHORT).show();
                response.parse();
                myInforms.addAll(response.data);
                myInformAdapter=new MyInformAdapter(myInforms,InformMySelfActivity.this);
                informForMyselfList.setLayoutManager(new LinearLayoutManager(InformMySelfActivity.this));
                informForMyselfList.setAdapter(myInformAdapter);
                mySelfName.setText(response.myName);
                mySelfFollowNum.setText(String.valueOf(response.followCount));
                mySelfFansNum.setText(String.valueOf(response.fansCount));
                mySelfArticleNum.setText(String.valueOf(response.articleCount));
                mySelfDescription.setText(response.myIdentity?response.mySummary:"");
               /* if(response..equals("NORMAL")){
                    userSummary.setText("");
                }else {
                    userSummary.setText(response.userSummary);
                }*/
            }
        });
    }


    @OnClick(R.id.title_search_bg)
    public void onSearch(){
        SearchActivity.startSearchActivity(SearchActivity.SEARCH_INFORM);
    }

    public static class MyInformAdapter extends RecyclerView.Adapter<MyInformAdapter.Holder> {
        private List<MyInform> list;
        private Activity activity;

        public MyInformAdapter(List<MyInform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_for_myself ,parent,false));
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            MyInform data=list.get(position);
            CommonUtil.setTextViewLinesWithEllipsis(holder.informContent,2);
            CommonUtil.setTextViewLinesWithEllipsis(holder.commitContent,1);
            holder.informContent.setText(data.getContent());
            holder.informCommitNum.setText(data.getCommitNum());

            if(data.getType().equals("INFO")){
                holder.informType.setText("发表了文章");
                holder.commit.setVisibility(View.GONE);
            }
            else if(data.getType().equals("COMMENT")){
                holder.informType.setText("评论了文章");
                holder.commit.setVisibility(View.VISIBLE);
                holder.commitContent.setText(data.commitName+"："+data.getCommitContent());
            }
            else if(data.getType().equals("REPLY")){
                holder.informType.setText("回复了评论");
                holder.commitHead.setVisibility(View.GONE);
                holder.commit.setVisibility(View.VISIBLE);
                holder.commitContent.setText(data.commitName+" 回复了 "+data.replyName+"："+data.commitContent);
            }
            else if(data.getType().equals("LIKE_COMMENT")){
                holder.informType.setText("点赞并且评论了资讯");
                holder.commit.setVisibility(View.VISIBLE);
                holder.commitContent.setText(data.commitName+"："+data.commitContent);
            }
            else if(data.getType().equals("LIKE_REPLY")){
                holder.informType.setText("点赞并且回复了评论");
                holder.commitHead.setVisibility(View.GONE);
                holder.commit.setVisibility(View.VISIBLE);
                holder.commitContent.setText(data.commitName+" 回复了 "+data.replyName+"："+data.getCommitContent());
            }
            else if(data.getType().equals("LIKE_INFO")){
                holder.informType.setText("点赞了资讯");
                holder.commit.setVisibility(View.GONE);
            }
            else if(data.getType().equals("KEEP_INFO")){
                holder.informType.setText("收藏了资讯");
                holder.commit.setVisibility(View.GONE);
            }else{
                holder.commit.setVisibility(View.GONE);
            }
            /*if (activity==InformOrderSelfActivity.instance){
                holder.typeLinearLayout.setVisibility(View.GONE);
                holder.commit.setVisibility(View.GONE);
            }else{
                holder.typeLinearLayout.setVisibility(View.VISIBLE);
            }*/

        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class Holder extends RecyclerView.ViewHolder {
            ImgView informImg;
            TextView informType;
            TextView informTime;
            TextView informContent;
            TextView informCommitNum;
            //LinearLayout typeLinearLayout;

            //评论详情部分
            RelativeLayout commit;
            ImgView commitHead;
            TextView commitContent;

            public Holder(View itemView) {
                super(itemView);
                informImg=itemView.findViewById(R.id.inform_for_myself_img);
                informType=itemView.findViewById(R.id.inform_for_myself_type);
                informTime=itemView.findViewById(R.id.inform_for_myself_time);
                informContent=itemView.findViewById(R.id.inform_for_myself_content);
                informCommitNum=itemView.findViewById(R.id.inform_for_myself_commit_content_num);
                //评论详情部分
                commit=itemView.findViewById(R.id.inform_for_myself_commit_content);
                commitHead=itemView.findViewById(R.id.inform_for_myself_commit_head);
                commitContent=itemView.findViewById(R.id.inform_for_myself_commit_contentmsg);
                //顶部文章状态和时间显示部分
                //typeLinearLayout=itemView.findViewById(R.id.inform_for_myself_type_linear);



            }
        }
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.myself_cover)
    public void onCover(){
        InformChangeCoverActivity.startChangeCoverActivity();
    }

    @OnClick(R.id.inform_for_myself_follow)
    void toMyFollow(){
        InformSelfFollowActivity.startInformSelfFollowActivity("我的关注");
    }

    @OnClick(R.id.inform_for_myself_fans)
    void toMyFans(){
        InformSelfFansActivity.startInformSelfFansActivity("我的粉丝");
    }

}
