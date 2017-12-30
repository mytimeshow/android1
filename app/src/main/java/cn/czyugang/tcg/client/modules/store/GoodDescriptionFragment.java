package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/30 0030.
 */

public class GoodDescriptionFragment extends BaseFragment {

    TextView descript;
    public static GoodDescriptionFragment newInstance() {
        GoodDescriptionFragment fragment = new GoodDescriptionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good_description, container, false);
        descript=rootView.findViewById(R.id.fragment_good_descript);
        return rootView;
    }

    @Override
    public void setText(String text) {
        if(text!=null){
            descript.setText(text);
        }else {
            descript.setText("");
        }

    }

    @Override
    public String getLabel() {
        return "商品描述";
    }

}