package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.RecordApi;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.common.dialog.StoreTrolleyDialog;
import cn.czyugang.tcg.client.modules.im.ImChatActivity;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;
import cn.czyugang.tcg.client.utils.string.RichText;
import cn.czyugang.tcg.client.widget.BottomBalanceView;
import cn.czyugang.tcg.client.widget.LabelLayout;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class StoreActivity extends BaseActivity {

    @BindView(R.id.store_tab)
    TabLayout tabLayout;
    @BindView(R.id.store_pager)
    ViewPager viewPager;
    @BindView(R.id.store_title)
    LinearLayout title;
    @BindView(R.id.store_img)
    ImgView storeImg;
    @BindView(R.id.store_name)
    TextView storeName;
    @BindView(R.id.store_notice)
    TextView storeNotice;
    @BindView(R.id.store_collect)
    TextView storeCollect;
    @BindView(R.id.store_label)
    LabelLayout storeLabel;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbar;
    @BindView(R.id.store_intro)
    View introL;
    @BindView(R.id.title_input)
    EditText input;

    //底部购物
    @BindView(R.id.store_bottomL)
    BottomBalanceView bottomBalanceView;
    @BindView(R.id.store_bottom_bar)
    View bottomBar;

    @BindView(R.id.store_bottom_homepage)
    View bottomHomepage;


    public String id;
    private List<BaseFragment> fragments = new ArrayList<>();
    private FoodListFragment foodListFragment = null;
    private GoodsListFragment goodsListFragment = null;
    public Store store = null;

    //购物车
    public TrolleyStore trolleyStore = null;
    private StoreTrolleyDialog storeTrolleyDialog = null;

    public static void startStoreActivity(String id) {
        Intent intent = new Intent(getTopActivity(), StoreActivity.class);
        intent.putExtra("id", id);
        getTopActivity().startActivity(intent);
    }

    public static void startStoreActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, StoreActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");

        storeNotice.requestFocus();
        input.setHint(RichText.newRichText("    搜索店内商品").addimgRes(0, 2, R.drawable.ic_search, R.dimen.dp_14).build());

        getStoreInfo(id);
        getTrolleyStore(id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppKeyStorage.saveTrolleyStore(id, trolleyStore);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initFragment() {
        if (store == null || trolleyStore == null) return;
        boolean isFood = store.isFoodStore();
        fragments.add(StoreHomeFragment.newInstance());
        if (isFood) {
            foodListFragment = FoodListFragment.newInstance();
            fragments.add(foodListFragment);
        } else {
            goodsListFragment = GoodsListFragment.newInstance();
            fragments.add(goodsListFragment);
        }
        fragments.add(CommentFragment.newInstance());
        fragments.add(CouponFragment.newInstance());
        fragments.add(StoreDetailFragment.newInstance());

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBalanceView.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
                bottomBar.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
                bottomHomepage.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(1);
    }

    /*
    *   购物车
    * */
    private void getTrolleyStore(String storeId) {
        if (false) AppKeyStorage.clearTrolleyStore(null);
        trolleyStore = AppKeyStorage.getTrolleyStore(storeId);
        StoreApi.getTrolley(storeId);
/*        .subscribe(new NetObserver<TrolleyResponse>() {
            @Override
            public void onNext(TrolleyResponse response) {
                super.onNext(response);
            }
        });*/

        initFragment();
        initBottomTrolley();
        refreshBottomTrolley();
        refreshGoodsListBuyNum();
    }

    private void initBottomTrolley() {
        bottomBalanceView.trolleyImg.setOnClickListener(v -> {
            if (storeTrolleyDialog == null) {
                storeTrolleyDialog = new StoreTrolleyDialog();
                storeTrolleyDialog.setTrolleyStore(trolleyStore, this);
                storeTrolleyDialog.setOnDismissRefresh(dialog -> {
                    if (foodListFragment != null) foodListFragment.refreshBuyNums();
                    if (goodsListFragment != null) goodsListFragment.refreshBuyNums();
                    refreshBottomTrolley();
                });
            }
            storeTrolleyDialog.show(getFragmentManager(), "StoreTrolleyDialog");
        });
    }

    public void refreshBottomTrolley() {
        if (trolleyStore == null) return;
        bottomBalanceView.setTrolleyStore(trolleyStore);
        bottomBalanceView.refresh();
    }

    public void refreshGoodsListBuyNum() {
        if (foodListFragment != null) foodListFragment.refreshBuyNums();
        if (goodsListFragment != null) goodsListFragment.refreshBuyNums();
    }

    private void postTrolley() {

    }

    /*
    *   店铺的分类 信息 收藏
    * */
    private void getStoreInfo(String storeId) {
        StoreApi.getStoreById(storeId).subscribe(new NetObserver<Response<Store>>() {
            @Override
            public void onNext(Response<Store> storeResponse) {
                super.onNext(storeResponse);
                if (ErrorHandler.judge200(storeResponse)) {
                    store = storeResponse.getData();
                    store.init(storeResponse.getValues());
                    initFragment();
                    isFoodStore(store.isFoodStore());
                    setInfo(store);
                    hadCollected(store.collected);
                }
            }
        });
    }

    private void isFoodStore(boolean is) {
        title.setBackgroundResource(is ? R.color.bg_store_food : R.color.bg_store_good);
        introL.setBackgroundResource(is ? R.color.bg_store_food : R.color.bg_store_good);
    }

    private void setInfo(Store store) {
        storeImg.id(store.avatarId);
        storeName.setText(store.name);
        storeNotice.setText(store.notices);
        storeLabel.setTexts(store.tagList);
    }

    private void hadCollected(boolean had) {
        storeCollect.setCompoundDrawablesWithIntrinsicBounds(null,
                ResUtil.getDrawable(had ? R.drawable.icon_collect_down : R.drawable.icon_collect), null, null);
    }


    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnEditorAction(R.id.title_input)
    public boolean onEditAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return false;
            LogRui.i("onEditAction####", text);
            AppUtil.hideKeyBoard(input);
            return true;
        }
        return false;
    }

    @OnClick(R.id.title_im)
    public void onIm() {
        ImChatActivity.startImChatActivity();
    }

    @OnClick(R.id.title_more)
    public void onMore() {
        MyDialog.moreDialog(this, new MyDialog.MoreDialogListener());
    }

    @OnClick(R.id.store_intro)
    public void onInfo() {
        StoreInfoActivity.startStoreInfoActivity(this, store);
    }

    @OnClick(R.id.store_collect)
    public void onCollect() {
        if (store == null) return;
        if (store.collected) {
            RecordApi.deleteCollect("STORE",store.id).subscribe(new NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (ErrorHandler.judge200(response)) {
                        store.collected = false;
                        hadCollected(store.collected);
                    }
                }
            });
        } else {
            RecordApi.collect("STORE",store.id).subscribe(new NetObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    super.onNext(response);
                    if (ErrorHandler.judge200(response)) {
                        store.collected = true;
                        hadCollected(store.collected);
                    }
                }
            });
        }
    }

}
