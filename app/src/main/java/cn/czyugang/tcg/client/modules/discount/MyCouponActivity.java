package cn.czyugang.tcg.client.modules.discount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.DiscountApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.entity.DiscountsResponse;

/**
 * @author ruiaa
 * @date 2018/1/5
 */

public class MyCouponActivity extends BaseActivity {

    @BindView(R.id.discount_tab)
    TabLayout tabLayout;
    @BindView(R.id.discount_viewpager)
    ViewPager viewPager;

    public static void startMyCouponActivity() {
        Intent intent = new Intent(getTopActivity(), MyCouponActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_my_coupon);
        ButterKnife.bind(this);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(MyCouponFragment.newInstance(1));
        fragments.add(MyCouponFragment.newInstance(3));
        fragments.add(MyCouponFragment.newInstance(2));
        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        DiscountApi.getPlatformActivity(1,null).subscribe(new BaseActivity.NetObserver<DiscountsResponse>() {
            @Override
            public void onNext(DiscountsResponse response) {
                super.onNext(response);
            }
        });
        DiscountApi.getStoreActivity(1,null).subscribe(new BaseActivity.NetObserver<DiscountsResponse>() {
            @Override
            public void onNext(DiscountsResponse response) {
                super.onNext(response);
            }
        });
        DiscountApi.getStoreCoupon("918003175762620416",1,null).subscribe(new BaseActivity.NetObserver<DiscountsResponse>() {
            @Override
            public void onNext(DiscountsResponse response) {
                super.onNext(response);
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
}
