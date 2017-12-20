package cn.czyugang.tcg.client.modules.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
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
    private SearchResultGoodsFragment searchGoodsFragment;
    private SearchResultStoreFragment searchStoreFragment;

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
        int type=getIntent().getIntExtra("type",SEARCH_TYPE_ALL);
        String key=getIntent().getStringExtra("key");

        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        input.setText(key);

        searchGoodsFragment = SearchResultGoodsFragment.newInstance();
        searchStoreFragment=SearchResultStoreFragment.newInstance();
        fragments.add(searchGoodsFragment);
        fragments.add(searchStoreFragment);
        searchGoodsFragment.setSearchKey(key,type);
        searchStoreFragment.setSearchKey(key,type);

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

    @OnEditorAction(R.id.title_input)
    public boolean onEditAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return false;
            searchStoreFragment.setSearchKey(text);
            searchGoodsFragment.setSearchKey(text);
            return true;
        }
        return false;
    }

    @OnClick(R.id.title_more)
    public void onMore(){
        MyDialog.moreDialog(this,new MyDialog.MoreDialogListener(){
            @Override
            public boolean showFootprint() {
                return true;
            }

            @Override
            public boolean showShare() {
                return false;
            }
        });
    }

}
