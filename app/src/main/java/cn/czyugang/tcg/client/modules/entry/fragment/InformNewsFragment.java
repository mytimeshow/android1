package cn.czyugang.tcg.client.modules.entry.fragment;

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

public class InformNewsFragment extends BaseFragment {
    public static InformNewsFragment newInstance() {
        InformNewsFragment fragment = new InformNewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good_comment, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        return "最新资讯";
    }
}
