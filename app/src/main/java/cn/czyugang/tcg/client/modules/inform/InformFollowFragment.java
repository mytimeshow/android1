package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.InformFollow;
import cn.czyugang.tcg.client.entity.InformFollowResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static cn.czyugang.tcg.client.utils.app.ResUtil.getColor;

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

    FollowContentAdapter followContentAdapter;
    List<InformFollow> followCotentsList = new ArrayList<InformFollow>();
    private InformFollowResponse informFollowResponse;
    private RefreshLoadHelper refreshLoadHelper;


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

        init();
        List<String> list = new ArrayList<>();
        list.add("全部栏目");
        list.add("电影扒客");
        list.add("吃货专区");
        list.add("八卦资讯协会");
        list.add("小城百事通");
        list.add("感情后花园");
        columnType.setTexts(list);
        followContentAdapter = new FollowContentAdapter(followCotentsList, getActivity());
        columnType.setOnClickItemListener((text, textView) -> {
            textView.setTextColor(getColor(R.color.main_red));
            textView.setBackgroundResource(R.drawable.bg_rect_label_click);

            if (columnType.lastSelectTextView != null) {
                columnType.lastSelectTextView.setTextColor(getResources().getColor(R.color.text_dark_gray));
                columnType.lastSelectTextView.setBackgroundResource(R.drawable.bg_rect);

            }
        });

        lvInformFollow.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvInformFollow.setAdapter(followContentAdapter);
        refreshLoadHelper = new RefreshLoadHelper(getActivity()).build(lvInformFollow);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> refreshInform(false, "ALL"));
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> refreshInform(true, "ALL"));
        refreshInform(true, "ALL");

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            switch (CLICK_TYPE) {
                case 1:
                    refreshInform(false, "ALL");
                    break;
                case 2:
                    refreshInform(false, "SORT");
                    break;
                case 3:
                    refreshInform(false, "USER");
                    break;
                default:
                    break;
            }

        }
    }

    public void init() {
        tvAllFollw.setTextColor(getResources().getColor(R.color.main_red));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
    }

    @Override
    public String getLabel() {
        return "我的关注";
    }

    @OnClick(R.id.tv_all_follow)
    public void onAllFollow() {
        CLICK_TYPE = 1;
        tvAllFollw.setTextColor(getResources().getColor(R.color.main_red));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
        refreshInform(false, "ALL");
    }

    @OnClick(R.id.tv_all_column)
    public void onAllColumn() {
        if (CLICK_TYPE == 1 || CLICK_TYPE == 3) {
            columnType.setVisibility(View.VISIBLE);
        }
        CLICK_TYPE = 2;
        tvAllColumn.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        if (CLICK_TYPE == 2) {
            if (columnType.getVisibility() == View.GONE) {
                columnType.setVisibility(View.VISIBLE);
                tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_up), null);

            } else if (columnType.getVisibility() == View.VISIBLE) {
                columnType.setVisibility(View.GONE);
                tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down_red), null);
            }
        }
        refreshInform(false, "SORT");

    }

    @OnClick(R.id.tv_follow_person)
    public void onFollowPerson() {
        CLICK_TYPE = 3;
        tvFollowPerson.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
        refreshInform(false, "USER");
    }


    private void refreshInform(boolean loadmore, String type) {
        int pagerIndex = 1;
        String accessTime = null;
        if (loadmore && informFollowResponse != null) {
            accessTime = informFollowResponse.accessTime;
            pagerIndex = informFollowResponse.currentPage + 1;
        }
        InformApi.getFollowInform(type, pagerIndex, accessTime).subscribe(new BaseActivity.NetObserver<InformFollowResponse>() {
            @Override
            public void onNext(InformFollowResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (ErrorHandler.isRepeat(informFollowResponse,response)) return;
                response.parse();
                if (!loadmore) {
                    followCotentsList.clear();
                }
                followCotentsList.addAll(response.data);
                followContentAdapter.notifyDataSetChanged();

                informFollowResponse = response;

            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    public static class FollowContentAdapter extends RecyclerView.Adapter<FollowContentAdapter.Holder> {
        private List<InformFollow> list;
        private Activity activity;

        public FollowContentAdapter(List<InformFollow> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_follow_list, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            InformFollow data = list.get(position);
            holder.followCommitNum.setText(data.commentNum);
            holder.followThumbNum.setText(data.thumbNum);
            holder.followName.setText(data.userName);
            holder.followContent.setText(data.title);
            holder.followImg.id(data.imgUrl);
            holder.followHead.id(data.headUrl);
            holder.itemView.setOnClickListener(v -> {
                InformDetailsActivity.startInformDetailsActivity();
            });
            holder.followName.setOnClickListener(v -> {
                InformOrderSelfActivity.startInformOrderSelfActivity(data.userId);
            });
            holder.followHead.setOnClickListener(v -> {
                InformOrderSelfActivity.startInformOrderSelfActivity(data.userId);
            });
            if (data.isThumbs) {
                holder.followThumbPic.setBackgroundResource(R.drawable.icon_dianzan2);
            } else {
                holder.followThumbPic.setBackgroundResource(R.drawable.ic_thumb_up);
            }
            holder.followThumb.setOnClickListener(v -> {
                if (!data.isThumbs) {
                    InformApi.toLikeInform(data.id).subscribe(new BaseActivity.NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            holder.followThumbPic.setBackgroundResource(R.drawable.icon_dianzan2);
                            holder.followThumbNum.setText(String.valueOf(Integer.valueOf(data.thumbNum) + 1));
                        }
                    });
                } else {
                    InformApi.toUnLikeInform(data.id).subscribe(new BaseActivity.NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            holder.followThumbPic.setBackgroundResource(R.drawable.ic_thumb_up);
                            holder.followThumbNum.setText(data.thumbNum);
                        }
                    });
                }
                data.isThumbs = (data.isThumbs ? false : true);

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
            LinearLayout followThumb;

            TextView followThumbPic;

            public Holder(View itemView) {
                super(itemView);
                followHead = itemView.findViewById(R.id.follow_list_head);
                followName = itemView.findViewById(R.id.follow_list_name);
                followCommitNum = itemView.findViewById(R.id.follw_list_commit_num);
                followThumbNum = itemView.findViewById(R.id.follw_list_thumb_num);
                followContent = itemView.findViewById(R.id.follow_list_content);
                followImg = itemView.findViewById(R.id.follow_list_img);
                followThumbPic = itemView.findViewById(R.id.follw_list_thumb_pic);
                followThumb = itemView.findViewById(R.id.follw_list_thumb);
            }
        }
    }
}
