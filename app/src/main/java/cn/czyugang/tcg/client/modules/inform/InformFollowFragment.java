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

import org.json.JSONArray;
import org.json.JSONObject;

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
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.InformFollow;
import cn.czyugang.tcg.client.entity.InformFollowResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.StringUtil;
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

    private int clickType = 1;

    FollowContentAdapter followContentAdapter;
    List<Inform> followCotentsListAll = new ArrayList<Inform>();
    List<Inform> followCotentsListSort = new ArrayList<Inform>();
    List<Inform> followCotentsListUser = new ArrayList<Inform>();
    List<String> labelList = new ArrayList<>();
    private InformFollowResponse informFollowResponseAll;
    private InformFollowResponse informFollowResponseSort;
    private InformFollowResponse informFollowResponseUser;
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

        followContentAdapter = new FollowContentAdapter(followCotentsListAll, getActivity());
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


        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> {
            String nowType;
            switch (clickType) {
                case 1:
                    nowType = "ALL";
                    break;
                case 2:
                    nowType = "SORT";
                    break;
                default:
                    nowType = "USER";
                    break;

            }
            refreshInform(false, nowType);
        });
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> {
            String nowType;
            switch (clickType) {
                case 1:
                    nowType = "ALL";
                    break;
                case 2:
                    nowType = "SORT";
                    break;
                default:
                    nowType = "USER";
                    break;
            }
            refreshInform(true, nowType);
        });
        lvInformFollow.postDelayed(() -> {
            refreshInform(true, "ALL");
            refreshInform(true, "SORT");
            refreshInform(true, "USER");
        }, 2000);

        return rootView;
    }

  /*  @Override
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
    }*/

    public void init() {
        tvAllFollw.setTextColor(getResources().getColor(R.color.main_red));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
        InformApi.getInformLabel().subscribe(new BaseActivity.NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;

                JSONArray labelArray = response.values.optJSONArray("sortNameDict");
                if (labelArray != null && labelArray.length() != 0) {
                    for (int i = 0, size = labelArray.length(); i < size; i++) {
                        JSONObject jsonObject = labelArray.optJSONObject(i);
                        labelList.add(jsonObject.optString("name"));

                    }

                }
                columnType.setTexts(labelList);
            }
        });
    }

    @Override
    public String getLabel() {
        return "我的关注";
    }

    @OnClick(R.id.tv_all_follow)
    public void onAllFollow() {
        if (clickType != 1) {
            followContentAdapter.list = followCotentsListAll;
            followContentAdapter.notifyDataSetChanged();
        }
        clickType = 1;
        tvAllFollw.setTextColor(getResources().getColor(R.color.main_red));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_all_column)
    public void onAllColumn() {


        if (clickType != 2) {
            followContentAdapter.list = followCotentsListSort;
            followContentAdapter.notifyDataSetChanged();
            lvInformFollow.scrollToPosition(0);
        }
        if (clickType == 1 || clickType == 3) {
            columnType.setVisibility(View.VISIBLE);
        }
        clickType = 2;
        tvAllColumn.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        if (clickType == 2) {
            if (columnType.getVisibility() == View.GONE) {
                columnType.setVisibility(View.VISIBLE);
                tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_up), null);

            } else if (columnType.getVisibility() == View.VISIBLE) {
                columnType.setVisibility(View.GONE);
                tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down_red), null);
            }
        }

    }

    @OnClick(R.id.tv_follow_person)
    public void onFollowPerson() {
        if (clickType != 3) {
            followContentAdapter.list = followCotentsListUser;
            followContentAdapter.notifyDataSetChanged();
        }
        clickType = 3;
        tvFollowPerson.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pull_down), null);
        columnType.setVisibility(View.GONE);
    }


    private void refreshInform(boolean loadmore, String type) {
        int pagerIndex = 1;
        String accessTime = null;
        switch (type) {
            case "ALL":
                if (loadmore && informFollowResponseAll != null) {
                    accessTime = informFollowResponseAll.accessTime;
                    pagerIndex = informFollowResponseAll.currentPage + 1;
                }
                break;
            case "SORT":
                if (loadmore && informFollowResponseSort != null) {
                    accessTime = informFollowResponseSort.accessTime;
                    pagerIndex = informFollowResponseSort.currentPage + 1;
                }
                break;
            case "USER":
                if (loadmore && informFollowResponseUser != null) {
                    accessTime = informFollowResponseUser.accessTime;
                    pagerIndex = informFollowResponseUser.currentPage + 1;
                }
                break;
            default:
                break;
        }

        InformApi.getFollowInform(type, pagerIndex, accessTime).subscribe(new BaseActivity.NetObserver<InformFollowResponse>() {
            @Override
            public void onNext(InformFollowResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                switch (type) {
                    case "ALL":
                        if (ErrorHandler.isRepeat(informFollowResponseAll, response)) return;
                        response.parse();
                        if (!loadmore) {
                            followCotentsListAll.clear();
                        }
                        followCotentsListAll.addAll(response.data);
                        informFollowResponseAll = response;
                        break;
                    case "SORT":
                        if (ErrorHandler.isRepeat(informFollowResponseSort, response)) return;
                        response.parse();
                        if (!loadmore) {
                            followCotentsListSort.clear();
                        }
                        followCotentsListSort.addAll(response.data);
                        informFollowResponseSort = response;
                        break;
                    case "USER":
                        if (ErrorHandler.isRepeat(informFollowResponseUser, response)) return;
                        response.parse();
                        if (!loadmore) {
                            followCotentsListUser.clear();
                        }
                        followCotentsListUser.addAll(response.data);
                        informFollowResponseUser = response;
                        break;
                }
                followContentAdapter.notifyDataSetChanged();
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    public static class FollowContentAdapter extends RecyclerView.Adapter<FollowContentAdapter.Holder> {
        private List<Inform> list;
        private Activity activity;

        public FollowContentAdapter(List<Inform> list, Activity activity) {
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
            Inform data = list.get(position);
            holder.followCommitNum.setText(StringUtil.returnMoreNum(data.commentNum));
            holder.followThumbNum.setText(StringUtil.returnMoreNum(data.thumbNum));
            holder.followName.setText(data.userName);
            holder.followContent.setText(data.title);
            holder.followImg.id(data.coverThumbImageFileId == null || data.coverThumbImageFileId.equals("") ? data.coverImageFileId : data.coverThumbImageFileId);
            holder.followHead.id(data.headUrl);
            holder.itemView.setOnClickListener(v -> {
                InformDetailsActivity.startInformDetailsActivity(data.id);
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
            //  LogRui.e("onBindViewHolder####" + data.isThumbs);
            holder.followThumb.setOnClickListener(v -> {
                if (!data.isThumbs) {
                    InformApi.toLikeInform(data.id).subscribe(new BaseActivity.NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            holder.followThumbPic.setBackgroundResource(R.drawable.icon_dianzan2);
                            holder.followThumbNum.setText(StringUtil.returnMoreNum(Integer.valueOf(data.thumbNum) + 1));
                        }
                    });
                } else {
                    InformApi.toUnLikeInform(data.id).subscribe(new BaseActivity.NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            holder.followThumbPic.setBackgroundResource(R.drawable.ic_thumb_up);
                            holder.followThumbNum.setText(StringUtil.returnMoreNum(data.thumbNum));
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
