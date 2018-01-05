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

public class MyCouponFragment extends BaseFragment {

    private int type=1;

    public static MyCouponFragment newInstance(int type) {
        //  1 未使用       2 已使用       3 已过期
        MyCouponFragment fragment = new MyCouponFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discount_my_coupon, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        type=getArguments().getInt("type");
        return type==1?"未使用":(type==2?"已使用":"已过期");
    }
}
