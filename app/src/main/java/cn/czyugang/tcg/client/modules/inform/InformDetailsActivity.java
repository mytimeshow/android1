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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.api.RecordApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.InformColumnResponse;
import cn.czyugang.tcg.client.entity.InformComment;
import cn.czyugang.tcg.client.entity.InformCommentRespone;
import cn.czyugang.tcg.client.entity.InformDetail;
import cn.czyugang.tcg.client.entity.InformDetailResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.StringUtil;
import cn.czyugang.tcg.client.utils.string.TimeUtils;
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
    @BindView(R.id.comment_list)
    RecyclerView commitList;

    private boolean isFollow;
    private boolean isLike;
    private int likeNum;
    private String userId;
    private List<InformComment> hotComments = new ArrayList<InformComment>();
    private List<InformComment> newComments = new ArrayList<InformComment>();
    private List<InformComment> comments = new ArrayList<InformComment>();
    private CommentAdapter commentAdapter;

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
        loadComment();

        commentAdapter = new CommentAdapter(comments, this);
        commitList.setLayoutManager(new LinearLayoutManager(this));
        commitList.setAdapter(commentAdapter);


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
                isLike = response.isLike;
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
                likeNum = response.likeCount;
                commentNum.setText(StringUtil.returnMoreNum(response.commentCount));
                thumbNum.setText(StringUtil.returnMoreNum(response.likeCount));

            }
        });
    }

    void loadComment() {
        InformApi.getHotComment(id).subscribe(new NetObserver<InformCommentRespone>() {
            @Override
            public void onNext(InformCommentRespone response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                response.parse();
                hotComments.clear();
                hotComments.add(null);
                hotComments.addAll(response.data);
                if (newComments.isEmpty() && response.data.size() >= 5 ) {
                    hotComments.add(null);
                }
                comments.addAll(hotComments);
                commentAdapter.notifyDataSetChanged();
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
    public void onThumb() {
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

    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
        private List<InformComment> list;
        private Activity activity;

        public CommentAdapter(List<InformComment> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public CommentAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommentAdapter.Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(CommentAdapter.Holder holder, int position) {
            InformComment data = list.get(position);
            switch (getItemViewType(position)) {

                case R.layout.item_inform_details_comment_cotent:
                    holder.head.id(data.userFileId);
                    holder.name.setText(data.userName);
                    holder.time.setText(TimeUtils.getTimeDifferent(data.createTime));
                    holder.thumbNum.setText(StringUtil.returnMoreNum(data.likeCount));
                    holder.thumb.setImageResource(data.isThumbs ? R.drawable.icon_dianzan2 : R.drawable.ic_thumb_up);
                    holder.content.setText(data.content);
                    holder.thumb.setOnClickListener(v -> {
                        if (!data.isThumbs) {
                            InformApi.toLikeComment(data.id).subscribe(new BaseActivity.NetObserver<Response>() {
                                @Override
                                public void onNext(Response response) {
                                    super.onNext(response);
                                    if (!ErrorHandler.judge200(response)) return;
                                    holder.thumb.setImageResource(R.drawable.icon_dianzan2);
                                    holder.thumbNum.setText(StringUtil.returnMoreNum(Integer.valueOf(data.likeCount) + 1));
                                    data.likeCount+=1;
                                }
                            });
                        } else {
                            InformApi.toUnLikeComment(data.id).subscribe(new BaseActivity.NetObserver<Response>() {
                                @Override
                                public void onNext(Response response) {
                                    super.onNext(response);
                                    if (!ErrorHandler.judge200(response)) return;
                                    holder.thumb.setImageResource(R.drawable.ic_thumb_up);
                                    holder.thumbNum.setText(StringUtil.returnMoreNum(Integer.valueOf(data.likeCount)-1));
                                    data.likeCount-=1;
                                }
                            });
                        }
                        data.isThumbs = (data.isThumbs ? false : true);

                    });
                    if (data.replyComment == null || data.replyComment.size() == 0) {
                        holder.reply.setVisibility(View.GONE);
                    } else {
                        holder.reply.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.layout.item_inform_details_comment_title:
                    break;

                case R.layout.item_inform_detail_load_more:
                    holder.itemView.setOnClickListener(v -> {
                        InformApi.getNewComment(id).subscribe(new NetObserver<InformCommentRespone>() {
                            @Override
                            public void onNext(InformCommentRespone response) {
                                super.onNext(response);
                                if (!ErrorHandler.judge200(response)) return;
                                response.parse();
                                newComments.clear();
                                newComments.add(null);
                                newComments.addAll(response.data);
                            }
                        });

                        comments.addAll(newComments);
                    });
                    break;
                default:
                    break;

            }

            holder.itemView.setOnClickListener(v -> {
//                InformDetailsActivity.startInformDetailsActivity(data.id);
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            int type;

            if (position == 0) {
                type = R.layout.item_inform_details_comment_title;
            } else if (position == list.size() - 1 && list.get(list.size() - 1) == null && list.size() > 5) {
                type = R.layout.item_inform_detail_load_more;
            } else if (list.get(position) == null && position < list.size() - 1) {
                type = R.layout.item_inform_details_comment_title_new;
            } else {
                type = R.layout.item_inform_details_comment_cotent;
            }

            return type;
        }

        class Holder extends RecyclerView.ViewHolder {

            ImgView head;
            TextView name;
            TextView time;
            TextView thumbNum;
            ImageView thumb;
            TextView content;

            LinearLayout reply;
            TextView replyFirst;
            TextView replySecond;
            TextView replyNum;



            public Holder(View itemView) {
                super(itemView);
                head = itemView.findViewById(R.id.inform_details_comment_head);
                name = itemView.findViewById(R.id.inform_details_comment_name);
                time = itemView.findViewById(R.id.inform_details_comment_time);
                thumbNum = itemView.findViewById(R.id.inform_details_comment_thumbs_num);
                thumb = itemView.findViewById(R.id.inform_details_comment_thumbs);
                content = itemView.findViewById(R.id.inform_details_comment_content);

                reply = itemView.findViewById(R.id.inform_details_comment_reply);
                replyFirst = itemView.findViewById(R.id.inform_details_comment_reply_first);
                replySecond = itemView.findViewById(R.id.inform_details_comment_reply_second);
                replyNum = itemView.findViewById(R.id.inform_details_comment_reply_num);


            }
        }


    }
}
