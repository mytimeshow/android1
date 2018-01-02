package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/30 0030.
 */

public class GoodConmentCountFragment extends BaseFragment {
    private int count=0;
    @BindView(R.id.fragment_good_descript)
    TextView descript;
    public static GoodConmentCountFragment newInstance() {
        GoodConmentCountFragment fragment = new GoodConmentCountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good_description, container, false);

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
    public void getCommetNum(int num) {
        count=num;
        getLabel();
    }

    @Override
    public String getLabel() {

        return "商品评价"+"("+count+")";
    }

}