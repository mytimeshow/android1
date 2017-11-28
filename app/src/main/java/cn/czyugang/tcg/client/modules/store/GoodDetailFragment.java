package cn.czyugang.tcg.client.modules.store;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class GoodDetailFragment extends BaseFragment {
    @BindView(R.id.good_detail_tab)
    TabLayout tabLayout;
    @BindView(R.id.good_detail_pager)
    ViewPager viewPager;
    Unbinder unbinder;

    private List<BaseFragment> fragments = new ArrayList<>();

    public static GoodDetailFragment newInstance() {
        GoodDetailFragment fragment = new GoodDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        fragments.add(GoodDetailIntroFragment.newInstance());
        fragments.add(GoodDetailSpecFragment.newInstance());

        viewPager.setAdapter(new BaseFragmentAdapter(getChildFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setTabTextColors(ResUtil.getColor(R.color.text_dark_gray), ResUtil.getColor(R.color.main_red));

        return rootView;
    }

    @Override
    public String getLabel() {
        return "详情";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
