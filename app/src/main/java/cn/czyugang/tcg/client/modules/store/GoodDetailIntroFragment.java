package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * @author ruiaa
 * @date 2017/11/27
 */

public class GoodDetailIntroFragment extends BaseFragment {
    public static GoodDetailIntroFragment newInstance() {
        GoodDetailIntroFragment fragment = new GoodDetailIntroFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good_detail_intro, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        return "商品介绍";
    }
}
