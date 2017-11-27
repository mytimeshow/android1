package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class StoreHomeFragment extends BaseFragment {
    private String id;

    public static StoreHomeFragment newInstance(String id) {
        StoreHomeFragment fragment = new StoreHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_home, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        return "首页";
    }
}
