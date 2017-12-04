package cn.czyugang.tcg.client.modules.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.widget.NoScrollViewPager;

/**
 * @author ruiaa
 * @date 2017/12/1
 * <p>
 * 搜索结果：商品，店铺列表
 */

public class SearchResultActivity extends BaseActivity {
    public static final int SEARCH_TYPE_ALL = 0;
    public static final int SEARCH_TYPE_FOOD = 1;
    public static final int SEARCH_TYPE_GOODS = 2;
    @BindView(R.id.title_input)
    EditText input;
    @BindView(R.id.search_result_tab)
    TabLayout tabLayout;
    @BindView(R.id.search_result_viewpager)
    NoScrollViewPager viewPager;

    private List<BaseFragment> fragments=new ArrayList<>();
    private SearchResultFragment searchResultFragment;
    private SearchResultFragment searchStoreFragment;

    public static void startSearchResultActivity(String key) {
        startSearchResultActivity(key, SEARCH_TYPE_ALL);
    }

    public static void startSearchResultActivity(String key, int searchType) {
        Intent intent = new Intent(getTopActivity(), SearchResultActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("type", searchType);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        searchResultFragment = SearchResultFragment.newInstance(SearchResultFragment.SEARCH_TYPE_GOODS);
        searchStoreFragment=SearchResultFragment.newInstance(SearchResultFragment.SEARCH_TYPE_STORE);
        fragments.add(searchResultFragment);
        fragments.add(searchStoreFragment);

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setTabLayoutIndicator(tabLayout, ResUtil.getDimenInPx(R.dimen.dp_72),ResUtil.getDimenInPx(R.dimen.dp_72));
            }
        },50);
    }



}
