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
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.InformResponse;
import cn.czyugang.tcg.client.modules.store.SearchActivity;
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
    @BindView(R.id.myself_follow_num)
    TextView userFollowNum;
    @BindView(R.id.myself_fans_num)
    TextView userFansNum;
    @BindView(R.id.inform_order_isfollow)
    TextView userIsFollow;

    private boolean isFollow;

    List<Inform> informs=new ArrayList<Inform>();
    InformColumnMsgActivity.SelfInformAdapter informAdapter;
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


        informAdapter=new InformColumnMsgActivity.SelfInformAdapter(informs,this);
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
                if (ErrorHandler.judge200(response)){
                    response.parse();
                    informs.clear();
                    informs.addAll(response.data);
                    informAdapter.notifyDataSetChanged();
                    if (firstLoad) {
                        informForMyselfList.setLayoutManager(new LinearLayoutManager(InformOrderSelfActivity.this));
                        informForMyselfList.setAdapter(informAdapter);
                    }
                    userName.setText(response.userName);
                    userHead.id(response.userHead);
                    userFollowNum.setText(response.userFollowNum);
                    userFansNum.setText(response.userFansNum);
                    isFollow=response.userIsFollow;

                    if(response.userIdentity.equals("NORMAL")){
                        userSummary.setText("");
                    }else {
                        userSummary.setText(response.userSummary);
                    }
                }


            }
        });
    }

    @OnClick(R.id.inform_for_myself_follow)
    void toMyFollow(){
        InformSelfFollowActivity.startInformSelfFollowActivity("TA的关注",userId);
    }

    @OnClick(R.id.inform_for_myself_fans)
    void toMyFans(){
        InformSelfFansActivity.startInformSelfFansActivity("TA的粉丝",userId);
    }

    @OnClick(R.id.inform_order_isfollow)
    void onFollow(){
        /*if (!isFollow) {
            InformApi.toFollowColumn(data.id).subscribe(
                    new Observer<Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response response) {
                            switch (response.getCode()) {
                                case 200:
                                    userIsFollow.setText("已关注");
                                    userIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }
            );
        } else {
            InformApi.toUnFollowColumn(data.id).subscribe(
                    new Observer<Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response response) {
                            switch (response.getCode()) {
                                case 200:
                                    userIsFollow.setText("+关注");
                                    userIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }
            );
        }*/
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
    @OnClick(R.id.title_search_bg)
    public void onSearch(){
        SearchActivity.startSearchActivity(SearchActivity.SEARCH_INFORM);
    }
}
