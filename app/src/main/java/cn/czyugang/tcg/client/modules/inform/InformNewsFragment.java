package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.NewsInformResponse;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.StringUtil;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class InformNewsFragment extends BaseFragment {

    @BindView(R.id.inform_news_list)
    RecyclerView informNewsList;

    InformNewsAdapter informNewsAdapter;
    List<Inform> informs = new ArrayList<Inform>();
    private NewsInformResponse newsInformResponse;
    private RefreshLoadHelper refreshLoadHelper;

    public static InformNewsFragment newInstance() {
        InformNewsFragment fragment = new InformNewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inform_news, container, false);
        ButterKnife.bind(this, rootView);
        informNewsAdapter = new InformNewsAdapter(informs, getActivity());
        informNewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        informNewsList.setAdapter(informNewsAdapter);

        refreshLoadHelper = new RefreshLoadHelper(getActivity()).build(informNewsList);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> refreshInform(false));
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> refreshInform(true));
        refreshInform(true);


        return rootView;
    }

    public void refreshInform(boolean loadmore) {
        int pagerIndex = 1;
        String accessTime = null;
        if (loadmore && newsInformResponse != null) {
            accessTime = newsInformResponse.accessTime;
            pagerIndex = newsInformResponse.currentPage + 1;
        }
        InformApi.getNewsInform(accessTime, pagerIndex).subscribe(new BaseActivity.NetObserver<NewsInformResponse>() {
            @Override
            public void onNext(NewsInformResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (ErrorHandler.isRepeat(newsInformResponse, response)) return;
                response.parse();
                if (!loadmore) {
                    informs.clear();
                }
                informs.addAll(response.data);
                informNewsAdapter.notifyDataSetChanged();
                newsInformResponse = response;

            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    @Override
    public String getLabel() {
        return "最新资讯";
    }


    static class InformNewsAdapter extends RecyclerView.Adapter<InformNewsAdapter.Holder> {
        private List<Inform> list;
        private Activity activity;

        public InformNewsAdapter(List<Inform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public InformNewsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InformNewsAdapter.Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(InformNewsAdapter.Holder holder, int position) {
            Inform data = list.get(position);
            switch (getItemViewType(position)) {

                case R.layout.item_inform_news_banner:
                    break;

                case R.layout.item_inform_news_large:
                    holder.newsLargeHead.id(data.headUrl);
                    holder.newsLargePersonName.setText(data.userName);
                    holder.newsLargeContent.setText(data.title);
                    holder.newsLargeContentName.setText("—— " + data.sortName + " ——");
                    holder.newsLargeCommitNum.setText(StringUtil.returnMoreNum(data.commentNum));
                    holder.newsLargeImg.id(data.coverThumbImageFileId == null || data.coverThumbImageFileId.equals("") ? data.coverImageFileId : data.coverThumbImageFileId);
                    holder.newsLargeHead.setOnClickListener(v -> {
                        InformOrderSelfActivity.startInformOrderSelfActivity(data.userId);
                    });
                    holder.newsLargePersonName.setOnClickListener(v -> {
                        InformOrderSelfActivity.startInformOrderSelfActivity(data.userId);
                    });
                    if (data.userIdentity != null&&!data.userIdentity.equals("") ) {
                        holder.newsLargeCommitIdentity.setVisibility(View.VISIBLE);
                        holder.newsLargeCommitIdentity.setText(data.userIdentity);
                    } else {
                        holder.newsLargeCommitIdentity.setVisibility(View.GONE);
                    }
                    break;

                case R.layout.item_inform_news_small:
                    holder.newsSmallHead.id(data.headUrl);
                    holder.newsSmallName.setText(data.userName);
                    holder.newsSmallContent.setText(data.title);
                    holder.newsSmallCommitNum.setText(StringUtil.returnMoreNum(data.commentNum));
                    holder.newsSmallImg.id(data.coverThumbImageFileId == null || data.coverThumbImageFileId.equals("") ? data.coverImageFileId : data.coverThumbImageFileId);
                    holder.newsSmallName.setOnClickListener(v -> {
                        InformOrderSelfActivity.startInformOrderSelfActivity(data.userId);
                    });
                    holder.newsSmallHead.setOnClickListener(v -> {
                        InformOrderSelfActivity.startInformOrderSelfActivity(data.userId);
                    });
                    if (data.userIdentity != null && !data.userIdentity.equals("")) {
                        holder.newsSmallCommitIdentity.setVisibility(View.VISIBLE);
                        holder.newsSmallCommitIdentity.setText(data.userIdentity);
                    } else {
                        holder.newsSmallCommitIdentity.setVisibility(View.GONE);
                    }
                    break;
            }

            holder.itemView.setOnClickListener(v -> {
                InformDetailsActivity.startInformDetailsActivity(data.id);
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            int type = R.layout.item_inform_news_small;
            if (position < 44) {
                position = position % 22;
                switch (position) {
                    case 0:
                        type = R.layout.item_inform_news_banner;
                        break;
                    case 7:
                        type = R.layout.item_inform_news_ad;
                        break;
                    case 1:
                    case 8:
                    case 13:
                    case 17:
                        type = R.layout.item_inform_news_large;
                        break;
                }

            } else {
                position = (position - 44) % 9;
                switch (position) {
                    case 0:
                    case 4:
                        type = R.layout.item_inform_news_large;
                        break;

                }
            }

            return type;

        }

        class Holder extends RecyclerView.ViewHolder {
            //轮播item
            Banner banner;
            //资讯大图item
            ImgView newsLargeImg;
            ImgView newsLargeHead;
            TextView newsLargeContent;
            TextView newsLargeContentName;
            TextView newsLargePersonName;
            TextView newsLargeCommitNum;
            TextView newsLargeCommitIdentity;
            //资讯小图item
            ImgView newsSmallImg;
            ImgView newsSmallHead;
            TextView newsSmallContent;
            TextView newsSmallName;
            TextView newsSmallCommitNum;
            TextView newsSmallCommitIdentity;

            public Holder(View itemView) {
                super(itemView);
                //轮播item
                banner = itemView.findViewById(R.id.inform_news_banner);
                //资讯大图item
                newsLargeImg = itemView.findViewById(R.id.inform_news_large_img);
                newsLargeHead = itemView.findViewById(R.id.inform_news_large_head);
                newsLargeContent = itemView.findViewById(R.id.inform_news_large_content);
                newsLargeContentName = itemView.findViewById(R.id.inform_news_large_cotentname);
                newsLargePersonName = itemView.findViewById(R.id.inform_news_large_name);
                newsLargeCommitNum = itemView.findViewById(R.id.inform_news_large_commitNum);
                newsLargeCommitIdentity = itemView.findViewById(R.id.inform_news_large_identity);
                //资讯小图item
                newsSmallImg = itemView.findViewById(R.id.inform_news_small_img);
                newsSmallHead = itemView.findViewById(R.id.inform_news_small_head);
                newsSmallContent = itemView.findViewById(R.id.inform_news_small_content);
                newsSmallName = itemView.findViewById(R.id.inform_news_small_name);
                newsSmallCommitNum = itemView.findViewById(R.id.inform_news_small_commit_num);
                newsSmallCommitIdentity = itemView.findViewById(R.id.inform_news_small_identity);

            }
        }
    }
}
