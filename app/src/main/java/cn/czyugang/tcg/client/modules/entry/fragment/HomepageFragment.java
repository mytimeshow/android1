package cn.czyugang.tcg.client.modules.entry.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
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
        rootView.findViewById(R.id.homepage_store).setOnClickListener(v -> StoreActivity.startStoreActivity(getActivity(),"919122791461220353"));
        rootView.findViewById(R.id.homepage_order).setOnClickListener(v -> ConfirmOrderActivity.startConfirmOrderActivity());


        return rootView;
    }
}
