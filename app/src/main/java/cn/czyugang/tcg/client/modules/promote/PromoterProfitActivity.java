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
import cn.czyugang.tcg.client.api.PromoterApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.PromoterReport;
import cn.czyugang.tcg.client.entity.Response;

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
    @BindView(R.id.promote_profit_tab_down)
    TabLayout tabLayoutDown;
    @BindView(R.id.promote_profit_viewpager_down)
    ViewPager viewPagerDown;

    private String lastMonthIncome;
    private String monthIncome;

    private String clickNumByToday;
    private String productNumByToday;
    private String registerNumByToday;
    private String totalCommissionByToday;

    private String clickNumByYesterday;
    private String productNumByYesterday;
    private String registerNumByYesterday;
    private String totalCommissionByYesterday;

    private String clickNumByWeek;
    private String productNumByWeek;
    private String registerNumByWeek;
    private String totalCommissionByWeek;

    private String clickNumByMonth;
    private String productNumByMonth;
    private String registerNumByMonth;
    private String totalCommissionByMonth;

    private List<BaseFragment> fragments=new ArrayList<>();
    private List<BaseFragment> fragmentsDown=new ArrayList<>();

    public static void startPromoterProfitActivity() {
        Intent intent = new Intent(getTopActivity(), PromoterProfitActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promoter_profit);
        ButterKnife.bind(this);

        PromoterProfitTimeFragment fragToday=PromoterProfitTimeFragment.newInstance("今天");
        PromoterProfitTimeFragment fragYesterday=PromoterProfitTimeFragment.newInstance("昨天");
        PromoterProfitTimeFragment fragWeek=PromoterProfitTimeFragment.newInstance("近7日");
        PromoterProfitTimeFragment fragMonth=PromoterProfitTimeFragment.newInstance("近30日");
        PromoterApi.getReport().subscribe(new NetObserver<Response<PromoterReport>>() {
            @Override
            public void onNext(Response<PromoterReport> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;

                lastMonthIncome=String.valueOf(response.data.lastMonthIncome);
                monthIncome=String.valueOf(response.data.monthIncome);

                clickNumByToday=String.valueOf(response.data.todayReport.clickNum);
                productNumByToday=String.valueOf(response.data.todayReport.productNum);
                registerNumByToday=String.valueOf(response.data.todayReport.registerNum);
                totalCommissionByToday=String.valueOf(response.data.todayReport.totalCommission);
                fragToday.setData(clickNumByToday,productNumByToday,registerNumByToday,totalCommissionByToday);

                clickNumByYesterday=String.valueOf(response.data.yesterdayReport.clickNum);
                productNumByYesterday=String.valueOf(response.data.yesterdayReport.productNum);
                registerNumByYesterday=String.valueOf(response.data.yesterdayReport.registerNum);
                totalCommissionByYesterday=String.valueOf(response.data.yesterdayReport.totalCommission);
                fragYesterday.setData(clickNumByYesterday,productNumByYesterday,registerNumByYesterday,totalCommissionByYesterday);

                clickNumByWeek=String.valueOf(response.data.sevenDayReport.clickNum);
                productNumByWeek=String.valueOf(response.data.sevenDayReport.productNum);
                registerNumByWeek=String.valueOf(response.data.sevenDayReport.registerNum);
                totalCommissionByWeek=String.valueOf(response.data.sevenDayReport.totalCommission);
                fragWeek.setData(clickNumByWeek,productNumByWeek,registerNumByWeek,totalCommissionByWeek);

                clickNumByMonth=String.valueOf(response.data.thirtyDayReport.clickNum);
                productNumByMonth=String.valueOf(response.data.thirtyDayReport.productNum);
                registerNumByMonth=String.valueOf(response.data.thirtyDayReport.registerNum);
                totalCommissionByMonth=String.valueOf(response.data.thirtyDayReport.totalCommission);
                fragMonth.setData(clickNumByMonth,productNumByMonth,registerNumByMonth,totalCommissionByMonth);

                thisMonth.setText("本月结算预估收入\n￥"+monthIncome);
                lastMonth.setText("上月结算预估收入\n￥"+lastMonthIncome);

            }
        });

        //今天 昨天 近7日 近30日
        fragments.add(fragToday);
        fragments.add(fragYesterday);
        fragments.add(fragWeek);
        fragments.add(fragMonth);


        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

         //所有订单 邀请注册  推广商品  失效订单
        fragmentsDown.add(PromoterProfitOrderFragment.newInstance("所有订单"));
        fragmentsDown.add(PromoterProfitOrderFragment.newInstance("邀请注册"));
        fragmentsDown.add(PromoterProfitOrderFragment.newInstance("推广商品"));
        fragmentsDown.add(PromoterProfitOrderFragment.newInstance("失效订单"));


        viewPagerDown.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),fragmentsDown));
        viewPagerDown.setOffscreenPageLimit(4);
        //tabLayout.setupWithViewPager(viewPagerDown);
        tabLayoutDown.setupWithViewPager(viewPagerDown);
        tabLayoutDown.setTabMode(TabLayout.MODE_FIXED);

    }


}
