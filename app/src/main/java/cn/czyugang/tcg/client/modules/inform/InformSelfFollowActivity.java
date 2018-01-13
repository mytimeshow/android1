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
import android.widget.FrameLayout;
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
import cn.czyugang.tcg.client.entity.Inform;;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserFansFollow;
import cn.czyugang.tcg.client.utils.img.ImgView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformSelfFollowActivity extends BaseActivity {

    @BindView(R.id.inform_self_follow_fans_list)
    RecyclerView selfFollwList;
    @BindView(R.id.inform_self_follow_fans_title_name)
    TextView tvTitleName;


    List<UserFansFollow> informColumns = new ArrayList<UserFansFollow>();


    public static void startInformSelfFollowActivity(String title, String id) {
        Intent intent = new Intent(getTopActivity(), InformSelfFollowActivity.class);
        intent.putExtra("titleName", title);
        intent.putExtra("userId", id);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_self_follow_fans);
        ButterKnife.bind(this);

        tvTitleName.setText(getIntent().getStringExtra("titleName"));


        SelfFollowFansListAdapter SelfFollowFansListAdapter = new SelfFollowFansListAdapter(informColumns, this);
        selfFollwList.setLayoutManager(new LinearLayoutManager(this));
        selfFollwList.setAdapter(SelfFollowFansListAdapter);

        InformApi.userFollowList(getIntent().getStringExtra("userId"), 1).subscribe(new NetObserver<Response<List<UserFansFollow>>>() {
            @Override
            public void onNext(Response<List<UserFansFollow>> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    informColumns.clear();
                    informColumns.addAll(response.data);

                    SelfFollowFansListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    public static class SelfFollowFansListAdapter extends RecyclerView.Adapter<SelfFollowFansListAdapter.Holder> {
        private List<UserFansFollow> list;
        private Activity activity;
        private boolean isFollow;

        public SelfFollowFansListAdapter(List<UserFansFollow> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_self_follow_fans, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            UserFansFollow data = list.get(position);
            holder.follwFansName.setText(data.userName);
            holder.follwFansFollwNum.setText(String.valueOf(data.fansCount));
            holder.follwFansHead.id(data.userFileId);
           isFollow=data.isFollow.equals("YES");
            if (isFollow) {
                holder.isFollow.setText("已关注");
                holder.isFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
            } else {
                holder.isFollow.setText("+关注");
                holder.isFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);
            }
            holder.followFrame.setOnClickListener(v -> {
                if (!isFollow) {
                    InformApi.toFollowUser(data.userId).subscribe(new NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            holder.isFollow.setText("已关注");
                            holder.isFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
                        }
                    });
                } else {
                    InformApi.toUnFollowUser(data.userId).subscribe(new NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            holder.isFollow.setText("+关注");
                            holder.isFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);
                        }
                    });
                }
                isFollow = (isFollow ? false : true);

            });


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
            ImgView follwFansHead;
            TextView follwFansName;
            TextView follwFansFollwNum;
            TextView isFollow;
            FrameLayout followFrame;


            public Holder(View itemView) {
                super(itemView);
                ;
                follwFansHead = itemView.findViewById(R.id.inform_self_follow_fans_img);
                follwFansName = itemView.findViewById(R.id.inform_self_follow_fans_name);
                follwFansFollwNum = itemView.findViewById(R.id.inform_self_follow_fans_follownum);
                isFollow = itemView.findViewById(R.id.inform_self_follow_fans_isfollow);
                followFrame =itemView.findViewById(R.id.inform_self_follow_fans_isfollow_frame);


            }
        }
    }

}
