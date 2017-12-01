package cn.czyugang.tcg.client.modules.entry.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.aftersale.AftersaleListActivity;
import cn.czyugang.tcg.client.modules.aftersale.AftersaleServiceActivity;
import cn.czyugang.tcg.client.modules.order.ConfirmOrderActivity;
import cn.czyugang.tcg.client.modules.store.StoreActivity;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

public class HomepageFragment extends BaseFragment {

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
        ButterKnife.bind(this, rootView);
        rootView.findViewById(R.id.homepage_store1).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(),"919122791461220353"));
        rootView.findViewById(R.id.homepage_store2).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(),"930278266785427456"));
        rootView.findViewById(R.id.homepage_store3).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(),"918003175762620416"));

        rootView.findViewById(R.id.homepage_order).setOnClickListener(v -> ConfirmOrderActivity.startConfirmOrderActivity());

        rootView.findViewById(R.id.homepage_aftersale_list).setOnClickListener(v -> AftersaleListActivity.startAftersaleListActivity());
        rootView.findViewById(R.id.homepage_aftersale_service).setOnClickListener(v -> AftersaleServiceActivity.startAftersaleServiceActivity());


        return rootView;
    }
}
