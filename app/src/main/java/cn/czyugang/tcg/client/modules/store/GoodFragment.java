package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class GoodFragment extends BaseFragment {
    public static GoodFragment newInstance() {
        GoodFragment fragment = new GoodFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        return "商品";
    }
}
