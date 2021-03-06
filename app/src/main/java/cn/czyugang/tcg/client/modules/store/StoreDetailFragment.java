package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Store;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class StoreDetailFragment extends BaseFragment {

    public static StoreDetailFragment newInstance() {
        StoreDetailFragment fragment = new StoreDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_detail, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        return "详情";
    }

    public void init(Store store){

    }
}
