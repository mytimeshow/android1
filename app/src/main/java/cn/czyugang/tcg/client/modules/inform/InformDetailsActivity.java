package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.api.RecordApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.InformDetail;
import cn.czyugang.tcg.client.entity.InformDetailResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.StringUtil;
import cn.czyugang.tcg.client.widget.LabelLayout;

/**
 * Created by Administrator on 2017/12/12.
 */

public class InformDetailsActivity extends BaseActivity {

    private String id;
    @BindView(R.id.inform_details_img)
    ImgView head;
    @BindView(R.id.inform_details_name)
    TextView name;
    @BindView(R.id.inform_details_isfollow)
    TextView tvIsFollow;
    @BindView(R.id.inform_detail_label)
    LabelLayout labelLayout;
    @BindView(R.id.comment_num)
    TextView commentNum;
    @BindView(R.id.thumb_num)
    TextView thumbNum;
    @BindView(R.id.thumb_up)
    ImageView thumbUp;

    private boolean isFollow;
    private boolean isLike;
    private int likeNum;
    private String userId;

    public static void startInformDetailsActivity(String id) {
        Intent intent = new Intent(getTopActivity(), InformDetailsActivity.class);
        intent.putExtra("id", id);
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");


        setContentView(R.layout.activity_inform_details);
        ButterKnife.bind(this);

        getInformDetail();
    }

    private void getInformDetail() {
        InformApi.getInformDetail(id).subscribe(new NetObserver<InformDetailResponse>() {
            @Override
            public void onNext(InformDetailResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                response.parse();
                head.id(response.userFileId);
                name.setText(response.userName);
                isFollow = response.isFollow;
                userId = response.data.userId;
                tvIsFollow.setText(response.isFollow ? "已关注" : "+关注");
                tvIsFollow.setBackgroundResource(response.isFollow ? R.drawable.bg_rect_cir_grey_ccc : R.drawable.bg_rect_cir_red);
                thumbUp.setImageResource(response.isLike ? R.drawable.icon_dianzan2 : R.drawable.ic_thumb_up);
                if (response.data.labelNames.equals("") || response.data.labelNames == null) {
                    labelLayout.setVisibility(View.GONE);
                } else {
                    labelLayout.setVisibility(View.VISIBLE);
                    labelLayout.setTexts(response.data.labelNames.split(","));
                }
                likeNum=response.likeCount;
                commentNum.setText(StringUtil.returnMoreNum(response.commentCount));
                thumbNum.setText(StringUtil.returnMoreNum(response.likeCount));

            }
        });
    }

    @OnClick(R.id.inform_detail_shoucang)
    public void onCollect() {
        RecordApi.collect("INFO", id).subscribe(new NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    AppUtil.toast("成功收藏");
                }
            }
        });
    }

    @OnClick(R.id.inform_details_isfollow)
    public void onFollow() {
        if (!isFollow) {
            InformApi.toFollowUser(userId).subscribe(new NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (!ErrorHandler.judge200(response)) return;
                    tvIsFollow.setText("已关注");
                    tvIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
                    isFollow = (isFollow ? false : true);
                }
            });
        } else {
            InformApi.toUnFollowUser(userId).subscribe(new NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (!ErrorHandler.judge200(response)) return;
                    tvIsFollow.setText("+关注");
                    tvIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);
                    isFollow = (isFollow ? false : true);
                }
            });
        }
    }
    @OnClick(R.id.inform_detail_thumbs)
    public void onThumb(){
        if (!isLike) {
            InformApi.toLikeInform(id).subscribe(new BaseActivity.NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (!ErrorHandler.judge200(response)) return;
                    thumbUp.setImageResource(R.drawable.icon_dianzan2);
                    thumbNum.setText(StringUtil.returnMoreNum(Integer.valueOf(likeNum) + 1));
                }
            });
        } else {
            InformApi.toUnLikeInform(id).subscribe(new BaseActivity.NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (!ErrorHandler.judge200(response)) return;
                    thumbUp.setImageResource(R.drawable.ic_thumb_up);
                    thumbNum.setText(StringUtil.returnMoreNum(likeNum));
                }
            });
        }
        isLike = (isLike ? false : true);
    }
}
