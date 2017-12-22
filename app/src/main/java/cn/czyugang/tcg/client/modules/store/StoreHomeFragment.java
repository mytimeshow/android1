package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class StoreHomeFragment extends BaseFragment {


    @BindView(R.id.store_home_banner)
    Banner banner;
    @BindView(R.id.store_home_video)
    ImgView video;
    @BindView(R.id.store_home_marquee)
    TextView marquee;
    @BindView(R.id.store_home_list1)
    RecyclerView listR1;
    @BindView(R.id.store_home_list2)
    RecyclerView listR2;
    @BindView(R.id.store_home_list3)
    RecyclerView listR3;
    @BindView(R.id.store_home)
    View homepage;
    @BindView(R.id.store_home_no)
    View homepageNo;

    Unbinder unbinder;

    private int type=0;

    private List<Item> list1 = new ArrayList<>();
    private List<Item> list2 = new ArrayList<>();
    private List<Item> list3 = new ArrayList<>();
    private AdAdapter adapter1;
    private AdAdapter adapter2;
    private AdAdapter adapter3;

    public static StoreHomeFragment newInstance() {
        StoreHomeFragment fragment = new StoreHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        marquee.requestFocus();

        for (int i = 0; i < 2; i++) list1.add(new Item().setLayout(R.layout.item_store_home_ad1));
        for (int i = 0; i < 4; i++) list1.add(new Item().setLayout(R.layout.item_store_home_ad2));

        for (int i = 0; i < 6; i++) list2.add(new Item().setLayout(R.layout.item_store_home_ad3));

        for (int i = 0; i < 8; i++) list3.add(new Item().setLayout(R.layout.item_store_home_ad1));

        init();

        return rootView;
    }

    private void init(){
        homepageNo.setVisibility(View.GONE);
        homepage.setVisibility(View.VISIBLE);

        adapter1 = new AdAdapter(list1, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position < 2 ? 2 : 1;
            }
        });
        listR1.setLayoutManager(gridLayoutManager);
        listR1.setAdapter(adapter1);
        listR1.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, ResUtil.getDimenInPx(R.dimen.dp_6), 0);
            }
        });


        adapter2 = new AdAdapter(list2, getActivity());
        listR2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        listR2.setAdapter(adapter2);
        listR2.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, ResUtil.getDimenInPx(R.dimen.dp_6), 0);
            }
        });

        adapter3 = new AdAdapter(list3, getActivity());
        listR3.setLayoutManager(new GridLayoutManager(getActivity(),2));
        listR3.setAdapter(adapter3);
        listR3.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, ResUtil.getDimenInPx(R.dimen.dp_6), 0);
            }
        });

        listR1.setNestedScrollingEnabled(false);
        listR3.setNestedScrollingEnabled(false);
    }

    @Override
    public String getLabel() {
        return "首页";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class AdAdapter extends RecyclerView.Adapter<AdAdapter.Holder> {
        private List<Item> list;
        private Activity activity;

        public AdAdapter(List<Item> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Item data = list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).layout;
        }

        class Holder extends RecyclerView.ViewHolder {
            ImgView imgView;
            TextView name;
            TextView price;

            public Holder(View itemView) {
                super(itemView);
                imgView = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                price = itemView.findViewById(R.id.item_price);
            }
        }
    }

    private static class Item {
        int layout = R.layout.item_store_home_ad1;

        public Item setLayout(int layout) {
            this.layout = layout;
            return this;
        }
    }
}
