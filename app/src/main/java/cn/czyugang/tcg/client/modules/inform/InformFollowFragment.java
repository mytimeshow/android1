package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.entity.FollowCotent;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.LabelLayout;

import static cn.czyugang.tcg.client.utils.app.ResUtil.getColor;
import static cn.czyugang.tcg.client.utils.app.ResUtil.getDrawable;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class InformFollowFragment extends BaseFragment {

    @BindView(R.id.tv_all_follow)
    TextView tvAllFollw;
    @BindView(R.id.tv_all_column)
    TextView tvAllColumn;
    @BindView(R.id.tv_follow_person)
    TextView tvFollowPerson;

    @BindView(R.id.column_type)
    LabelLayout columnType;

    @BindView(R.id.inform_follow_list)
    RecyclerView lvInformFollow;

    Unbinder unbinder;

    private int CLICK_TYPE;


    public static InformFollowFragment newInstance() {
        InformFollowFragment fragment = new InformFollowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inform_follow, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        onAllFollow();

        List<String> list = new ArrayList<>();
        list.add("全部栏目");
        list.add("电影扒客");
        list.add("吃货专区");
        list.add("八卦资讯协会");
        list.add("小城百事通");
        list.add("感情后花园");
        columnType.setTexts(list);

        columnType.setOnClickItemListener(new LabelLayout.OnClickItemListener() {

            @Override
            public void onClick(String text, TextView textView) {


                textView.setTextColor(getColor(R.color.main_red));
                textView.setBackgroundResource(R.drawable.bg_rect_label_click);
                if (columnType.lastSelectTextView != null) {
                    columnType.lastSelectTextView.setTextColor(getResources().getColor(R.color.text_dark_gray));
                    columnType.lastSelectTextView.setBackgroundResource(R.drawable.bg_rect);

                }


            }
        });
        init();

        return rootView;
    }

    @Override
    public String getLabel() {
        return "我的关注";
    }

    @OnClick(R.id.tv_all_follow)
    public void onAllFollow() {
        CLICK_TYPE=1;
        tvAllFollw.setTextColor(getResources().getColor(R.color.main_red));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_all_column)
    public void onAllColumn() {
        if ( CLICK_TYPE==1|| CLICK_TYPE==3){
            columnType.setVisibility(View.VISIBLE);
        }
        CLICK_TYPE=2;
        tvAllColumn.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        if(CLICK_TYPE==2){
            if (columnType.getVisibility() == View.GONE) {
                columnType.setVisibility(View.VISIBLE);
                tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_up), null);

            }else if (columnType.getVisibility() == View.VISIBLE) {
                columnType.setVisibility(View.GONE);
                tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down_red), null);
            }
        }

    }

    @OnClick(R.id.tv_follow_person)
    public void onFollowPerson() {
        CLICK_TYPE=3;
        tvFollowPerson.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
    }

    private void init() {
        List<FollowCotent> followCotentsList = new ArrayList<FollowCotent>();
        FollowCotent followCotent = new FollowCotent();
        followCotent.setName("博主名称");
        followCotent.setContent("内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
        followCotent.setCommentNum("1234567");
        followCotent.setThumbNum("777777777");
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        FollowContentAdapter followContentAdapter = new FollowContentAdapter(followCotentsList,getActivity());
        lvInformFollow.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvInformFollow.setAdapter(followContentAdapter);

    }


     public static class FollowContentAdapter extends RecyclerView.Adapter<FollowContentAdapter.Holder> {
        private List<FollowCotent> list;
        private Activity activity;

        public FollowContentAdapter(List<FollowCotent> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                   R.layout.item_follow_list ,parent,false));
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            FollowCotent data=list.get(position);
            holder.followCommitNum.setText(data.getCommentNum());
            holder.followThumbNum.setText(data.getThumbNum());
            holder.followName.setText(data.getName());
            holder.followContent.setText(data.getContent());
            holder.itemView.setOnClickListener(v -> {
                InformOrderSelfActivity.startInformOrderSelfActivity();
            });
        }
        @Override
        public int getItemCount() {
            return list.size();
        }


        class Holder extends RecyclerView.ViewHolder {
            ImgView followHead;
            TextView followName;
            TextView followCommitNum;
            TextView followThumbNum;
            TextView followContent;
            ImgView followImg;
            public Holder(View itemView) {
                super(itemView);
                followHead=itemView.findViewById(R.id.follow_list_head);
                followName=itemView.findViewById(R.id.follow_list_name);
                followCommitNum=itemView.findViewById(R.id.follw_list_commit_num);
                followThumbNum=itemView.findViewById(R.id.follw_list_thumb_num);
                followContent=itemView.findViewById(R.id.follow_list_content);
                followImg=itemView.findViewById(R.id.follow_list_img);
            }
        }
    }
}
