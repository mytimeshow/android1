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
import cn.czyugang.tcg.client.modules.errand.HomeErrandFragment;
import cn.czyugang.tcg.client.modules.scan.ScanActivity;
import cn.czyugang.tcg.client.modules.store.HomeFoodFragment;
import cn.czyugang.tcg.client.modules.store.HomeGoodsFragment;
import cn.czyugang.tcg.client.modules.store.SearchActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

public class HomepageFragment extends BaseFragment {

    @BindView(R.id.homepage_location)
    TextView location;
    @BindView(R.id.homepage_tab)
    TabLayout tabLayout;
    @BindView(R.id.homepage_viewpager)
    ViewPager viewPager;
    Unbinder unbinder;


    public static HomepageFragment newInstance() {
        HomepageFragment fragment = new HomepageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        unbinder = ButterKnife.bind(this, rootView);


//        rootView.findViewById(R.id.homepage_store1).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "919122791461220353"));
//        rootView.findViewById(R.id.homepage_store2).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "930278266785427456"));
//        rootView.findViewById(R.id.homepage_store3).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "918003175762620416"));
//       rootView.findViewById(R.id.homepage_groupon).setOnClickListener(v -> GrouponGoodsActivity.startGrouponGoodsActivity());
        //  rootView.findViewById(R.id.homepage_groupon).setOnClickListener(v -> VideoActivity.startVideoActivity());


        List<BaseFragment> fragments=new ArrayList<>();
        fragments.add(HomeGoodsFragment.newInstance());
        fragments.add(HomeFoodFragment.newInstance());
        fragments.add(HomeErrandFragment.newInstance());
        viewPager.setAdapter(new BaseFragmentAdapter(getChildFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        CommonUtil.setTabLayoutIndicator(tabLayout, ResUtil.getDimenInPx(R.dimen.dp_30),ResUtil.getDimenInPx(R.dimen.dp_30));

        return rootView;
    }

    @OnClick(R.id.homepage_search)
    public void onSearch() {
        SearchActivity.startSearchActivity(SearchActivity.SEARCH_STORE);
    }

    @OnClick(R.id.homepage_scan)
    public void onScan(){
        ScanActivity.startScanActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
