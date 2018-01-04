package cn.czyugang.tcg.client.modules.person;

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
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class FootmarkActivity extends BaseActivity {

    public static final int ROOTMARK_TYPE_SHOP = 0;
    public static final int ROOTMARK_TYPE_GOODS = 1;
    public static final int ROOTMARK_TYPE_INFORM = 2;

    @BindView(R.id.footmark_tab)
    TabLayout tabLayout;
    @BindView(R.id.footmark_viewpager)
    ViewPager viewPager;
    @BindView(R.id.title_right)
    TextView title_right;

    private BaseFragmentAdapter adapter;
    private List<BaseFragment> fragments;

    public static void startFootmarkActivity() {
        Intent intent = new Intent(getTopActivity(), FootmarkActivity.class);
        getTopActivity().startActivity(intent);
    }

    public static void startFootmarkActivity(int showType) {
        Intent intent = new Intent(getTopActivity(), FootmarkActivity.class);
        intent.putExtra("showType", showType);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footmark);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragments.add(FootmarkFragment.newInstance(ROOTMARK_TYPE_SHOP));
        fragments.add(FootmarkFragment.newInstance(ROOTMARK_TYPE_GOODS));
        fragments.add(FootmarkFragment.newInstance(ROOTMARK_TYPE_INFORM));
        adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(getIntent().getIntExtra("showType", 0));
    }

    @OnClick(R.id.title_right)
    public void onTitleRight() {
        if (title_right.getText().equals("编辑")) {
            title_right.setText("完成");
            for (BaseFragment f : fragments) {
                ((FootmarkFragment) f).showBottom(true);
            }
        } else {
            title_right.setText("编辑");
            for (BaseFragment f : fragments) {
                ((FootmarkFragment) f).showBottom(false);
            }
        }
    }

    public void cancelEdit(){
        title_right.setText("编辑");
        for(BaseFragment f:fragments){
            ((CollectionFragment)f).showBottom(false);
        }
    }


}