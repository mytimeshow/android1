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
import android.widget.Toast;

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
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * Created by Administrator on 2017/12/9.
 */

public class InformColumnMsgActivity extends BaseActivity {

    @BindView(R.id.inform_for_order_list)
    RecyclerView informOrderList;
    @BindView(R.id.inform_appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.inform_title_bg)
    FrameLayout frameTitle;

    @BindView(R.id.inform_order_name)
    TextView userName;
    @BindView(R.id.inform_order_head)
    ImgView userHead;
    @BindView(R.id.inform_order_follownum)
    TextView userFollowNum;
    @BindView(R.id.inform_order_introduction)
    TextView userSummary;
    @BindView(R.id.inform_order_isfollow)
    TextView columnIsFollow;

    SmallInformAdapter smallInformAdapter;
    List<Inform> informs = new ArrayList<Inform>();

    boolean isFollow ;

    public static void startInformOrderMsgActivity(String id) {
        Intent intent = new Intent(getTopActivity(), InformColumnMsgActivity.class);
        intent.putExtra("id",id);
        getTopActivity().startActivity(intent);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_order_column_msg);
        ButterKnife.bind(this);

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            LogRui.i("onCreate####" + verticalOffset + "            " + Math.abs(verticalOffset) / appBarLayout.getHeight());
            frameTitle.setAlpha((float) (Math.abs(verticalOffset)) / ((float) (appBarLayout.getHeight())));
            userName.setAlpha(1 - (Math.abs(verticalOffset)) / (appBarLayout.getHeight()));


        });


        refreshInform(true,1,getIntent().getStringExtra("id"),"","","","");
        columnIsFollow.setText(isFollow?"已关注":"+关注");
        columnIsFollow.setBackgroundResource(isFollow?R.drawable.bg_rect_cir_grey_ccc:R.drawable.bg_rect_cir_red);

        smallInformAdapter = new SmallInformAdapter(informs, this);
        informOrderList.setLayoutManager(new LinearLayoutManager(this));
        informOrderList.setAdapter(smallInformAdapter);
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
                    smallInformAdapter.notifyDataSetChanged();
                    if (firstLoad) {
                        informOrderList.setLayoutManager(new LinearLayoutManager(InformColumnMsgActivity.this));
                        informOrderList.setAdapter(smallInformAdapter);
                    }
                    userName.setText(response.name);
                    userHead.id(response.fileId);
                    userFollowNum.setText(String.valueOf(response.columnFollowNum));
                    isFollow=response.columnIsFollow;

                    if(response.userIdentity.equals("NORMAL")){
                        userSummary.setText("");
                    }else {
                        userSummary.setText(response.userSummary);
                    }
                }


            }
        });
    }

    @OnClick(R.id.inform_order_isfollow)
    void onIsFollow() {
        MyDialog.Builder.newBuilder(this)
                .contentStr("您真的忍心抛弃电影扒客吗？")
                .onPositiveButton(myDialog -> {
                    myDialog.dismiss();
                })
                .bindView(myDialog -> {
                    TextView tv_p=myDialog.rootView.findViewById(R.id.tv_positive);
                    TextView tv_n=myDialog.rootView.findViewById(R.id.tv_negative);
                    tv_n.setBackgroundResource(R.drawable.bg_dialog_button_positive);
                    tv_p.setBackgroundResource(R.drawable.bg_dialog_button_negative);
                    tv_p.setTextColor(getResources().getColor(R.color.text_dialog_button_negative));
                    tv_n.setTextColor(getResources().getColor(R.color.text_dialog_button_positive));
                    tv_n.setText("朕再想想");
                    tv_p.setText("狠心抛弃");
                })
                .build()
                .show();


    }

    @OnClick(R.id.inform_order_edit_article)
    void toEditArticle(){
        InformEditArticleActivity.startInformEditArticleActivity();
    }
    static class SmallInformAdapter extends RecyclerView.Adapter<InformColumnMsgActivity.SmallInformAdapter.Holder> {
        private List<Inform> list;
        private Activity activity;

        public SmallInformAdapter(List<Inform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public InformColumnMsgActivity.SmallInformAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InformColumnMsgActivity.SmallInformAdapter.Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_news_small, parent, false));
        }

        @Override
        public void onBindViewHolder(SmallInformAdapter.Holder holder, int position) {
            Inform data = list.get(position);
            holder.informSmallContent.setText(data.title);
            holder.informSmallCommitNum.setText(String.valueOf(data.commentNum));
            holder.informSmallHead.id(data.headUrl);
            holder.informSmallImg.id(data.imgUrl);
//            holder.informSmallImg.id(data.imgUrl);
            holder.informSmallName.setText(data.userName);
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
                informSmallImg = itemView.findViewById(R.id.inform_news_small_img);
                informSmallHead = itemView.findViewById(R.id.inform_news_small_head);
                informSmallContent = itemView.findViewById(R.id.inform_news_small_content);
                informSmallName = itemView.findViewById(R.id.inform_news_small_name);
                informSmallCommitNum = itemView.findViewById(R.id.inform_news_small_commit_num);
            }
        }
    }


}
