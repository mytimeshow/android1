package cn.czyugang.tcg.client.modules.inform;

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
import cn.czyugang.tcg.client.entity.InformComment;
import cn.czyugang.tcg.client.entity.InformCommentDetailRespone;
import cn.czyugang.tcg.client.entity.InformCommentRespone;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.address.activity.AddAddressActivity;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.StringUtil;
import cn.czyugang.tcg.client.utils.string.TimeUtils;

/**
 * Created by Administrator on 2018/1/11.
 */

public class InformCommentDetailAcitivity extends BaseActivity {

    CommentDetailAdapter commentDetailAdapter;
    @BindView(R.id.commentList)
    RecyclerView commentList;
    @BindView(R.id.inform_details_comment_head)
    ImgView commentHead;
    @BindView(R.id.inform_details_comment_name)
    TextView commentName;
    @BindView(R.id.inform_details_comment_time)
    TextView commentTime;
    @BindView(R.id.inform_details_comment_thumbs_num)
    TextView thumbNum;
    @BindView(R.id.inform_details_comment_thumbs)
    ImageView thumb;
    @BindView(R.id.inform_details_comment_content)
    TextView commentContent;
    private List<InformComment> replyCommentList = new ArrayList<InformComment>();

    String commentId;
    boolean isThumbs;
    int likeCount;

    public static void startInformCommentDetailAcitivity(String commentId) {
        Intent intent = new Intent(getTopActivity(), InformCommentDetailAcitivity.class);
        intent.putExtra("commentId", commentId);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_comment_detail);
        ButterKnife.bind(this);

        commentDetailAdapter = new CommentDetailAdapter(replyCommentList, this);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setAdapter(commentDetailAdapter);
        commentId = getIntent().getStringExtra("commentId");
        InformApi.getCommentDetail(commentId).subscribe(new NetObserver<InformCommentDetailRespone>() {
            @Override
            public void onNext(InformCommentDetailRespone response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                response.parse();
                commentHead.id(response.data.userFileId);
                commentName.setText(response.data.userName);
                commentTime.setText(TimeUtils.getTimeDifferent(response.data.createTime));
                thumbNum.setText(StringUtil.returnMoreNum(response.data.likeCount));
                commentContent.setText(response.data.content);
                isThumbs = response.data.isThumbs;
                likeCount = response.data.likeCount;
                thumb.setImageResource(response.data.isThumbs ? R.drawable.icon_dianzan2 : R.drawable.ic_thumb_up);
                replyCommentList.addAll(response.data.replyComment);
                commentDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.inform_details_comment_thumbs)
    void onThumb(){
        if (!isThumbs) {
            InformApi.toLikeComment(commentId).subscribe(new BaseActivity.NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (!ErrorHandler.judge200(response)) return;
                    thumb.setImageResource(R.drawable.icon_dianzan2);
                    thumbNum.setText(StringUtil.returnMoreNum(Integer.valueOf(likeCount) + 1));
                    likeCount += 1;
                }
            });
        } else {
            InformApi.toUnLikeComment(commentId).subscribe(new BaseActivity.NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (!ErrorHandler.judge200(response)) return;
                    thumb.setImageResource(R.drawable.ic_thumb_up);
                    thumbNum.setText(StringUtil.returnMoreNum(Integer.valueOf(likeCount) - 1));
                    likeCount -= 1;
                }
            });
        }
        isThumbs = (isThumbs ? false : true);
    }
    ;

    private static class CommentDetailAdapter extends RecyclerView.Adapter<CommentDetailAdapter.Holder> {
        private List<InformComment> list;
        private Activity activity;

        public CommentDetailAdapter(List<InformComment> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_details_comment_cotent_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            InformComment data = list.get(position);
            holder.name.setText(data.userName);
            holder.time.setText(TimeUtils.getTimeDifferent(data.createTime));
            if (data.userName.equals(data.targetUserName)) {
                holder.content.setText(data.content);
            } else {
                holder.content.setText("回复 " + data.targetUserName + ": " + data.content);
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class Holder extends RecyclerView.ViewHolder {
            TextView name;
            TextView time;
            TextView content;

            public Holder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.comment_name);
                time = itemView.findViewById(R.id.comment_time);
                content = itemView.findViewById(R.id.comment_content);
            }
        }
    }

}
