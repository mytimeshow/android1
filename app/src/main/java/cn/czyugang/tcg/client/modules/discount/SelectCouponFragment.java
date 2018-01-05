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

public class SelectCouponFragment extends BaseFragment {

    private boolean isPlatform=false;
    private boolean isUseful=false;

    public static SelectCouponFragment newInstance(boolean isPlatform,boolean isUseful) {
        SelectCouponFragment fragment = new SelectCouponFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean("isPlatform",isPlatform);
        bundle.putBoolean("isUseful",isUseful);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPlatform=getArguments().getBoolean("isPlatform");
        isUseful=getArguments().getBoolean("isUseful");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discount_select_coupon, container, false);
        return rootView;
    }

    @Override
    public String getLabel() {
        isPlatform=getArguments().getBoolean("isPlatform");
        isUseful=getArguments().getBoolean("isUseful");
        return isUseful?"可用优惠券":"不可用优惠券";
    }
}
