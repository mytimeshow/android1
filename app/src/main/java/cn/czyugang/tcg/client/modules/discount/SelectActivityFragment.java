package cn.czyugang.tcg.client.modules.discount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * @author ruiaa
 * @date 2018/1/5
 */

public class SelectActivityFragment extends BaseFragment {

    private boolean isPlatform=false;
    public static SelectActivityFragment newInstance(boolean isPlatform) {
        SelectActivityFragment fragment = new SelectActivityFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean("isPlatform",isPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPlatform=getArguments().getBoolean("isPlatform");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discount_select_activity, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        isPlatform=getArguments().getBoolean("isPlatform");
        return "";
    }
}
