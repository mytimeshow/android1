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

public class CommentFragment extends BaseFragment {

    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_comment, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        return "评论";
    }
}
