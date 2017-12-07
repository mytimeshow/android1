package cn.czyugang.tcg.client.modules.promote;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoterProfitTimeFragment extends BaseFragment {

    @BindView(R.id.promote_profit_money)
    TextView money;
    @BindView(R.id.promote_profit_click_num)
    TextView clickNum;
    @BindView(R.id.promote_profit_register_num)
    TextView registerNum;
    @BindView(R.id.promote_profit_pay_nums)
    TextView payNums;
    @BindView(R.id.promote_profit_tab)
    TabLayout tabLayout;
    @BindView(R.id.promote_profit_viewpager)
    ViewPager viewPager;
    Unbinder unbinder;

    private String type=null;
    private List<BaseFragment> fragments=new ArrayList<>();

    // 今天 昨天 近7日 近30日
    public static PromoterProfitTimeFragment newInstance(String type) {
        PromoterProfitTimeFragment fragment = new PromoterProfitTimeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type==null) type=getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_promoter_profit_time, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        //所有订单 邀请注册  推广商品  失效订单
        fragments.add(PromoterProfitOrderFragment.newInstance("所有订单"));
        fragments.add(PromoterProfitOrderFragment.newInstance("邀请注册"));
        fragments.add(PromoterProfitOrderFragment.newInstance("推广商品"));
        fragments.add(PromoterProfitOrderFragment.newInstance("失效订单"));

        viewPager.setAdapter(new BaseFragmentAdapter(getChildFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getLabel() {
        if (type==null) type=getArguments().getString("type");
        return type;
    }
}
