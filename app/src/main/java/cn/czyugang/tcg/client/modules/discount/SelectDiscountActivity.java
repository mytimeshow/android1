package cn.czyugang.tcg.client.modules.discount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.entity.Discount;

/**
 * @author ruiaa
 * @date 2018/1/5
 */

public class SelectDiscountActivity extends BaseActivity {

    @BindView(R.id.discount_tab)
    TabLayout tabLayout;
    @BindView(R.id.discount_viewpager)
    ViewPager viewPager;

    private String type="";
    private String storeId="";

    public static void startSelectDiscountActivity(String type, @NonNull String storeId) {
        Intent intent = new Intent(getTopActivity(), SelectDiscountActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("storeId",storeId);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type=getIntent().getStringExtra("type");
        storeId=getIntent().getStringExtra("storeId");

        setContentView(R.layout.activity_discount_select);
        ButterKnife.bind(this);

        List<BaseFragment> fragments=new ArrayList<>();
        switch (type){
            case Discount.TYPE_PLATFORM_ACTIVITY:{
                fragments.add(SelectActivityFragment.newInstance(true));
                tabLayout.setVisibility(View.GONE);
                break;
            }
            case Discount.TYPE_STORE_ACTIVITY:{
                fragments.add(SelectActivityFragment.newInstance(false));
                tabLayout.setVisibility(View.GONE);
                break;
            }
            case Discount.TYPE_PLATFORM_COUPON:{
                fragments.add(SelectCouponFragment.newInstance(true,true));
                fragments.add(SelectCouponFragment.newInstance(true,false));
                break;
            }
            case Discount.TYPE_STORE_COUPON:{
                fragments.add(SelectCouponFragment.newInstance(false,true));
                fragments.add(SelectCouponFragment.newInstance(false,false));
                break;
            }
            default:return;
        }
        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
}
