package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.groupon.GrouponGoodsActivity;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class HomeGoodsFragment extends BaseFragment {
    public static HomeGoodsFragment newInstance() {
        HomeGoodsFragment fragment = new HomeGoodsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_goods, container, false);

        rootView.findViewById(R.id.homepage_store1).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "919122791461220353"));
        rootView.findViewById(R.id.homepage_store2).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "930278266785427456"));
        rootView.findViewById(R.id.homepage_store3).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(), "918003175762620416"));

        rootView.findViewById(R.id.homepage_groupon).setOnClickListener(v -> GrouponGoodsActivity.startGrouponGoodsActivity());
        // rootView.findViewById(R.id.homepage_groupon).setOnClickListener(v -> VideoActivity.startVideoActivity());

        return rootView;
    }

    @Override
    public String getLabel() {
        return "商城";
    }
}
