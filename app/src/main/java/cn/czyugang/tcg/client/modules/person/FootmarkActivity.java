package cn.czyugang.tcg.client.modules.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class FootmarkActivity extends BaseActivity {

    public static final int ROOTMARK_TYPE_SHOP = 0;
    public static final int ROOTMARK_TYPE_GOODS = 1;
    public static final int ROOTMARK_TYPE_INFORM = 2;

    @BindView(R.id.footmark_tab)
    TabLayout tabLayout;
    @BindView(R.id.footmark_viewpager)
    ViewPager viewPager;
    @BindView(R.id.title_right)
    TextView title_right;

    private BaseFragmentAdapter adapter;
    private List<BaseFragment> fragments;

    public static void startFootmarkActivity() {
        Intent intent = new Intent(MyApplication.getContext(), FootmarkActivity.class);
        MyApplication.getContext().startActivity(intent);
    }

    public static void startFootmarkActivity(int showType) {
        Intent intent = new Intent(MyApplication.getContext(), FootmarkActivity.class);
        intent.putExtra("showType", showType);
        MyApplication.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footmark);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragments.add(FootmarkFragment.newInstance(ROOTMARK_TYPE_SHOP));
        fragments.add(FootmarkFragment.newInstance(ROOTMARK_TYPE_GOODS));
        fragments.add(FootmarkFragment.newInstance(ROOTMARK_TYPE_INFORM));
        adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(getIntent().getIntExtra("showType", 0));
    }

    @OnClick(R.id.title_right)
    public void onTitleRight() {
        if (title_right.getText().equals("编辑")) {
            title_right.setText("完成");
            for (BaseFragment f : fragments) {
                ((FootmarkFragment) f).showBottom(true);
            }
        } else {
            title_right.setText("编辑");
            for (BaseFragment f : fragments) {
                ((FootmarkFragment) f).showBottom(false);
            }
        }
    }


    public static class FootmarkFragment extends BaseFragment {

        @BindView(R.id.fragment_collection_bottom)
        View bottomV;
        @BindView(R.id.view_select_all)
        SelectButton selectB;
        @BindView(R.id.view_delete)
        TextView deleteT;
        @BindView(R.id.fragment_collection_list)
        RecyclerView ListV;
        Unbinder unbinder;

        public static FootmarkFragment newInstance(int type) {
            FootmarkFragment fragment = new FootmarkFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            fragment.setArguments(bundle);
            return fragment;
        }

        int type = -1;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (type < 0 && getArguments() != null) type = getArguments().getInt("type");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_collection, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            return rootView;
        }

        @Override
        public String getLabel() {
            if (type < 0 && getArguments() != null) type = getArguments().getInt("type");
            switch (type) {
                case ROOTMARK_TYPE_SHOP:
                    return "店铺";
                case ROOTMARK_TYPE_GOODS:
                    return "商品";
                default:
                    return "资讯";
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        public void showBottom(boolean show) {
            bottomV.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private static class FootmarkAdapter extends RecyclerView.Adapter<FootmarkAdapter.Holder> {
        private List<Object> list;
        private Activity activity;

        public FootmarkAdapter(List<Object> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public FootmarkAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FootmarkAdapter.Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_collection, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Object data = list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}