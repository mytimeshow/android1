package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.MyInform;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformOrderSelfActivity extends BaseActivity {

    @BindView(R.id.inform_order_isfollow_frame)
    FrameLayout followButton;
    @BindView(R.id.inform_for_myself_article)
    LinearLayout articleNum;
    @BindView(R.id.inform_for_myself_list)
    RecyclerView informForMyselfList;
    @BindView(R.id.inform_appbar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.myself_title_bg)
    FrameLayout frameTitle;


    public static InformOrderSelfActivity instance;
    public static void startInformOrderSelfActivity( ){
        Intent intent=new Intent(getTopActivity(),InformOrderSelfActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_myself);
        ButterKnife.bind(this);
        followButton.setVisibility(View.VISIBLE);
        articleNum.setVisibility(View.GONE);
        instance=this;


        List<MyInform> myInforms=new ArrayList<MyInform>();
        MyInform myInform=new MyInform();
        myInform.setCommitNum("46121");
        myInform.setType("发表了文章");
        myInform.setContent("哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或");
        MyInform myInform2=new MyInform();
        myInform2.setCommitNum("7685446");
        myInform2.setType("赞同了评论");
        myInform2.setContent("内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");

        myInforms.add(myInform);
        myInforms.add(myInform2);
        myInforms.add(myInform);
        myInforms.add(myInform);
        myInforms.add(myInform);
        myInforms.add(myInform2);
        myInforms.add(myInform2);
        myInforms.add(myInform);
        myInforms.add(myInform);


        InformMySelfActivity.MyInformAdapter myInformAdapter=new InformMySelfActivity.MyInformAdapter(myInforms,this);
        InformMySelfActivity.MyInformAdapter.Holder holder=null;
        informForMyselfList.setLayoutManager(new LinearLayoutManager(this));
        informForMyselfList.setAdapter(myInformAdapter);

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            LogRui.i("onCreate####"+verticalOffset+"            "+Math.abs(verticalOffset)/appBarLayout.getHeight());
            frameTitle.setAlpha((float)(Math.abs(verticalOffset))/((float)(appBarLayout.getHeight())));
        });
    }

    @OnClick(R.id.inform_for_myself_follow)
    void toMyFollow(){
        InformSelfFollowActivity.startInformSelfFollowActivity("TA的关注");
    }

    @OnClick(R.id.inform_for_myself_fans)
    void toMyFans(){
        InformSelfFansActivity.startInformSelfFansActivity("TA的粉丝");
    }
}
