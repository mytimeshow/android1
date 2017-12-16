package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class InformNewsFragment extends BaseFragment {

    @BindView(R.id.inform_news_list)
    RecyclerView informNewsList;

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

        List<Inform> list=new ArrayList<Inform>();
        Inform informColumn=new Inform();
        Inform informColumn2=new Inform();
        informColumn.name=("行走的鸡腿");
        informColumn2.name=("天天吃吃吃");
        //1234
        list.add(informColumn);
        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn2);
        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        //1234
        list.add(informColumn);
        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn2);
        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        //34
        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        //34
        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        list.add(informColumn2);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);
        list.add(informColumn);

        InformNewsAdapter informNewsAdapter=new InformNewsAdapter(list,getActivity());
        informNewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        informNewsList.setAdapter(informNewsAdapter);



        return rootView;
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
            Inform newsSmallColumnData =  list.get(position);
            switch (getItemViewType(position)){

                case R.layout.item_inform_news_banner:
                    break;

                case R.layout.item_inform_news_large:

                    holder.newsLargePersonName.setText(newsSmallColumnData.name);
                    break;

                case R.layout.item_inform_news_small:

                    holder.newsSmallName.setText(newsSmallColumnData.name);
                    break;
            }


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
                switch (position){
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
            //资讯小图item
            ImgView newsSmallImg;
            ImgView newsSmallHead;
            TextView newsSmallContent;
            TextView newsSmallName;
            TextView newsSmallCommitNum;

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
                //资讯小图item
                newsSmallImg = itemView.findViewById(R.id.inform_news_large_img);
                newsSmallHead = itemView.findViewById(R.id.inform_news_small_head);
                newsSmallContent = itemView.findViewById(R.id.inform_news_small_content);
                newsSmallName = itemView.findViewById(R.id.inform_news_small_name);
                newsSmallCommitNum = itemView.findViewById(R.id.inform_news_small_commit_num);

            }
        }
    }
}
