package cn.czyugang.tcg.client.modules.entry.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BasePageAdapter;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.modules.entry.adapter.NewFeaturePageAdapter;
import cn.czyugang.tcg.client.modules.entry.contract.NewFeatureContract;
import cn.czyugang.tcg.client.modules.entry.presenter.NewFeaturePresenter;

/**
 * Created by wuzihong on 2017/9/12.
 * 新特性页面
 */

public class NewFeatureActivity extends BaseActivity implements NewFeatureContract.View,
        BasePageAdapter.OnItemClickListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.pager_new_feature)
    ViewPager pager_new_feature;
    @BindView(R.id.group_tab)
    RadioGroup group_tab;

    private NewFeatureContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feature);
        ButterKnife.bind(this);
        mPresenter = new NewFeaturePresenter(this, context.getSharedPreferences(Config.CONFIG_NAME, Context.MODE_PRIVATE));
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        //新特性图片数据k
        List<Integer> newFeatureImgList = new ArrayList<>();
        newFeatureImgList.add(R.drawable.def_new_feature1);
        newFeatureImgList.add(R.drawable.def_new_feature2);
        newFeatureImgList.add(R.drawable.def_new_feature3);
        newFeatureImgList.add(R.drawable.def_new_feature4);
        //初始化轮播图
        NewFeaturePageAdapter adapter = new NewFeaturePageAdapter(context);
        adapter.setData(newFeatureImgList);
        adapter.setOnItemClickListener(this);
        pager_new_feature.setAdapter(adapter);
        pager_new_feature.addOnPageChangeListener(this);
        //初始化标签
        for (int i = 0, length = newFeatureImgList.size(); i < length; i++) {
            View tab = LayoutInflater.from(context).inflate(R.layout.include_main_dot_tab, group_tab, false);
            tab.setId(i);
            group_tab.addView(tab);
        }
        group_tab.check(0);
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void startADActivity() {
        //TODO 启动广告页面
    }

    @Override
    public void onItemClick(ViewPager viewPager, View view, int position, Object data) {
        switch (view.getId()) {
            case R.id.tv_entry:
                mPresenter.gotoNext();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        group_tab.check(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
