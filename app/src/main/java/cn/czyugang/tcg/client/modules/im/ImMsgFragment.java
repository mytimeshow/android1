package cn.czyugang.tcg.client.modules.im;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * @author ruiaa
 * @date 2017/12/21
 */

public class ImMsgFragment extends BaseFragment {
    public static ImMsgFragment newInstance() {
        ImMsgFragment fragment = new ImMsgFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_im_msg, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        return "聊天";
    }
}
