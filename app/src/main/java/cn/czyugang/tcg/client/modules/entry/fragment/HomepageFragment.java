package cn.czyugang.tcg.client.modules.entry.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.aftersale.AftersaleListActivity;
import cn.czyugang.tcg.client.modules.groupon.GrouponGoodsActivity;
import cn.czyugang.tcg.client.modules.order.ConfirmOrderActivity;
import cn.czyugang.tcg.client.modules.scan.ScanActivity;
import cn.czyugang.tcg.client.modules.store.SearchActivity;
import cn.czyugang.tcg.client.modules.store.StoreActivity;

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

        rootView.findViewById(R.id.homepage_store1).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "919122791461220353"));
        rootView.findViewById(R.id.homepage_store2).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "930278266785427456"));
        rootView.findViewById(R.id.homepage_store3).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "918003175762620416"));
        rootView.findViewById(R.id.homepage_order).setOnClickListener(v -> ConfirmOrderActivity.startConfirmOrderActivity());
        rootView.findViewById(R.id.homepage_aftersale_list).setOnClickListener(v -> AftersaleListActivity.startAftersaleListActivity());
        rootView.findViewById(R.id.homepage_groupon).setOnClickListener(v -> GrouponGoodsActivity.startGrouponGoodsActivity());

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
