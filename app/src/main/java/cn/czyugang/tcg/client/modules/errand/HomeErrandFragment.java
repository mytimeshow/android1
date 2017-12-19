package cn.czyugang.tcg.client.modules.errand;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class HomeErrandFragment extends BaseFragment {

    Unbinder unbinder;

    public static HomeErrandFragment newInstance() {
        HomeErrandFragment fragment = new HomeErrandFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_errand, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.home_errand_buy)
    public void onBuy(){
        ErrandBookActivity.startErrandBookActivity(ErrandBookActivity.ERRAND_TYPE_BUY);
    }

    @OnClick(R.id.home_errand_send)
    public void onSend(){
        ErrandBookActivity.startErrandBookActivity(ErrandBookActivity.ERRAND_TYPE_SEND);
    }

    @OnClick(R.id.home_errand_take)
    public void onTake(){
        ErrandBookActivity.startErrandBookActivity(ErrandBookActivity.ERRAND_TYPE_TAKE);
    }


    @Override
    public String getLabel() {
        return "跑腿";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
