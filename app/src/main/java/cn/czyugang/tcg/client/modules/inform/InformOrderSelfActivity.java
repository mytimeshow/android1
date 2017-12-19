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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.InformResponse;
import cn.czyugang.tcg.client.entity.MyInform;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;

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

    @BindView(R.id.myself_name)
    TextView userName;
    @BindView(R.id.myself_head)
    ImgView userHead;
    @BindView(R.id.myself_description)
    TextView userSummary;
    @BindView(R.id.)


    List<Inform> informs=new ArrayList<Inform>();
    InformColumnMsgActivity.SmallInformAdapter informAdapter;

    public static InformOrderSelfActivity instance;
    public static String userId;
    public static void startInformOrderSelfActivity(String id){
        Intent intent=new Intent(getTopActivity(),InformOrderSelfActivity.class);
        getTopActivity().startActivity(intent);
        userId=id;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_myself);
        ButterKnife.bind(this);
        followButton.setVisibility(View.VISIBLE);
        articleNum.setVisibility(View.GONE);
        instance=this;


        informAdapter=new InformColumnMsgActivity.SmallInformAdapter(informs,this);
        refreshInform(true,1,"","",userId,"","");
       // InformMySelfActivity.MyInformAdapter.Holder holder=null;


        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            LogRui.i("onCreate####"+verticalOffset+"            "+Math.abs(verticalOffset)/appBarLayout.getHeight());
            frameTitle.setAlpha((float)(Math.abs(verticalOffset))/((float)(appBarLayout.getHeight())));
        });
    }

    public void refreshInform(boolean firstLoad,int page,String sortId,String labelId,String publisherId,String keywordType,String keyword){
        InformApi.getInformByCondition(page,sortId,labelId,publisherId,keywordType,keyword).subscribe(new BaseActivity.NetObserver<InformResponse>() {
            @Override
            public void onNext(InformResponse response) {
                super.onNext(response);
                response.parse();
                informs.clear();
                informs.addAll(response.data);
                userName.setText(response.userName);
                userHead.id(response.userHead);
                informAdapter.notifyDataSetChanged();
                if (firstLoad) {
                    informForMyselfList.setLayoutManager(new LinearLayoutManager(InformOrderSelfActivity.this));
                    informForMyselfList.setAdapter(informAdapter);
                }

            }
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
