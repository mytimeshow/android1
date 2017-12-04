package cn.czyugang.tcg.client.modules.store;

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
 * @date 2017/11/24
 */

public class GoodDetailActivity extends BaseActivity {
    @BindView(R.id.good_detail_tab)
    TabLayout tabLayout;
    @BindView(R.id.good_detail_pager)
    ViewPager viewPager;
    private List<BaseFragment> fragments=new ArrayList<>();

    public static void startGoodDetailActivity() {
        Intent intent = new Intent(getTopActivity(), GoodDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        ButterKnife.bind(this);

        fragments.add(GoodFragment.newInstance());
        fragments.add(GoodDetailFragment.newInstance());
        fragments.add(GoodCommentFragment.newInstance());

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),fragments));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
}
