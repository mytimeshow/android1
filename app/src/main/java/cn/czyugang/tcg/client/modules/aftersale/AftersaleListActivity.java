package cn.czyugang.tcg.client.modules.aftersale;

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
import cn.czyugang.tcg.client.common.MyApplication;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 退款/售后列表
 */

public class AftersaleListActivity extends BaseActivity {

    @BindView(R.id.aftersale_list_tab)
    TabLayout tabLayout;
    @BindView(R.id.aftersale_list_viewpager)
    ViewPager viewPager;
    private List<BaseFragment> fragments=new ArrayList<>();

    public static void startAftersaleListActivity() {
        Intent intent = new Intent(getTopActivity(), AftersaleListActivity.class);
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersale_list);
        ButterKnife.bind(this);

        fragments.add(AftersaleListFragment.newInstance(AftersaleListFragment.AFTER_SALE_TYPE_ALL));
        fragments.add(AftersaleListFragment.newInstance(AftersaleListFragment.AFTER_SALE_TYPE_DEAL));
        fragments.add(AftersaleListFragment.newInstance(AftersaleListFragment.AFTER_SALE_TYPE_FINISH));

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }


}
