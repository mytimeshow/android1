package cn.czyugang.tcg.client.modules.entry.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.modules.im.ImListActivity;
import cn.czyugang.tcg.client.modules.store.HomeCategoryFragment;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

public class CategoryFragment extends BaseFragment {
    @BindView(R.id.category_location)
    TextView location;
    @BindView(R.id.category_tab)
    TabLayout tabLayout;
    @BindView(R.id.category_viewpager)
    ViewPager viewPager;
    Unbinder unbinder;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(HomeCategoryFragment.newInstance(Store.STORE_TYPE_FOOD));
        fragments.add(HomeCategoryFragment.newInstance(Store.STORE_TYPE_GOODS));
        viewPager.setAdapter(new BaseFragmentAdapter(getChildFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        getCategoryInfo();

        return rootView;
    }

    private void getCategoryInfo() {

    }

    @OnClick(R.id.category_msg)
    public void onMsg() {
        ImListActivity.startImListActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
