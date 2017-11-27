package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
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
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class MyOrderActivity extends BaseActivity {

    @BindView(R.id.myorder_tab)
    TabLayout tabLayout;
    @BindView(R.id.myorder_pager)
    ViewPager viewPager;

    private List<BaseFragment> fragments=new ArrayList<>();

    public static void startMyOrderActivity(Activity activity) {
        Intent intent = new Intent(activity, MyOrderActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);

        fragments.add(OrderListFragment.newInstance(OrderListFragment.MYORDER_TYPE_ALL));
        fragments.add(OrderListFragment.newInstance(OrderListFragment.MYORDER_TYPE_NO_PAY));
        fragments.add(OrderListFragment.newInstance(OrderListFragment.MYORDER_TYPE_NO_SEND));
        fragments.add(OrderListFragment.newInstance(OrderListFragment.MYORDER_TYPE_NO_RECEIVED));
        fragments.add(OrderListFragment.newInstance(OrderListFragment.MYORDER_TYPE_NO_COMMENT));

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
