package cn.czyugang.tcg.client.modules.im;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.ImApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.ImNoticeMsg;
import cn.czyugang.tcg.client.entity.ImNoticeType;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.LabelLayout;

/**
 * @author ruiaa
 * @date 2017/12/21
 */

public class NoticeFragment extends BaseFragment {
    @BindView(R.id.im_notice_list)
    RecyclerView noticeR;
    @BindView(R.id.im_notice_types)
    LabelLayout types;
    @BindView(R.id.im_notice_typesL)
    FrameLayout typesL;
    Unbinder unbinder;

    private List<String> noticeList = new ArrayList<>();
    private NoticeAdapter adapter;
    List<String> noticeType=new ArrayList<String>();
    List<ImNoticeType> noticeTypeList = new ArrayList<ImNoticeType>();

    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_im_notice, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        noticeList.addAll(Arrays.asList(".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."));
        adapter = new NoticeAdapter(noticeList, getActivity());
        noticeR.setLayoutManager(new LinearLayoutManager(getActivity()));
        noticeR.setAdapter(adapter);
        ImApi.getNoticeList("100001",1).subscribe(new BaseActivity.NetObserver<Response<List<ImNoticeMsg>>>() {
            @Override
            public void onNext(Response<List<ImNoticeMsg>> response) {
                super.onNext(response);
            }
        });
        ImApi.getNoticeTypeList().subscribe(new BaseActivity.NetObserver<Response<List<ImNoticeType>>>() {
            @Override
            public void onNext(Response<List<ImNoticeType>> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                noticeTypeList.clear();
                noticeTypeList.addAll(response.data);
                if (noticeTypeList.size() != 0 && noticeTypeList != null) {
                    for (int i = 0, size = noticeTypeList.size(); i < size; i++) {
                        noticeType.add(noticeTypeList.get(i).name);
                    }
                    types.setTexts(noticeType);
                    LogRui.e("onNext####"+noticeType);
                }


            }
        });


        return rootView;
    }

    public void showTypes() {
        typesL.setVisibility(View.VISIBLE);
    }

    public void hideTypes() {
        typesL.setVisibility(View.GONE);
    }

    public boolean typesIsShowing() {
        return typesL.getVisibility() == View.VISIBLE;
    }

    @Override
    public String getLabel() {
        return "通知";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Holder> {
        private List<String> list;
        private Activity activity;

        public NoticeAdapter(List<String> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_im_notice, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String data = list.get(position);

            holder.imgView.setVisibility(position % 3 == 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView time;
            TextView title;
            TextView subTitle;
            ImgView imgView;
            TextView category;

            public Holder(View itemView) {
                super(itemView);
                time = itemView.findViewById(R.id.item_time);
                title = itemView.findViewById(R.id.item_title);
                subTitle = itemView.findViewById(R.id.item_title_sub);
                imgView = itemView.findViewById(R.id.item_img);
                category = itemView.findViewById(R.id.item_category);
            }
        }
    }
}
