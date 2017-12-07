package cn.czyugang.tcg.client.modules.promote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;

/**
 * @author ruiaa
 * @date 2017/12/6
 * <p>
 * 报表
 */

public class PromoterProfitActivity extends BaseActivity {

    @BindView(R.id.promote_profit_this_month)
    TextView thisMonth;
    @BindView(R.id.promote_profit_last_month)
    TextView lastMonth;
    @BindView(R.id.promote_profit_tab)
    TabLayout tabLayout;
    @BindView(R.id.promote_profit_viewpager)
    ViewPager viewPager;

    private List<BaseFragment> fragments=new ArrayList<>();

    public static void startPromoterProfitActivity() {
        Intent intent = new Intent(getTopActivity(), PromoterProfitActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promoter_profit);
        ButterKnife.bind(this);

        //今天 昨天 近7日 近30日
        fragments.add(PromoterProfitTimeFragment.newInstance("今天"));
        fragments.add(PromoterProfitTimeFragment.newInstance("昨天"));
        fragments.add(PromoterProfitTimeFragment.newInstance("近7日"));
        fragments.add(PromoterProfitTimeFragment.newInstance("近30日"));

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
