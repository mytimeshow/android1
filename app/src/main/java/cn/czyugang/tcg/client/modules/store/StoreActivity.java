package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.RichText;
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
    @BindView(R.id.store_trolley_img)
    ImageView trolleyImg;
    @BindView(R.id.store_good_nums)
    TextView buyGoodNums;
    @BindView(R.id.store_buy_commit)
    TextView buyCommit;
    @BindView(R.id.store_bottomL)
    LinearLayout storeBottomL;
    @BindView(R.id.store_bottom_bar)
    View bottomBar;


    private String id;
    private List<BaseFragment> fragments = new ArrayList<>();
    private StoreHomeFragment storeHomeFragment;
    private FoodListFragment foodListFragment;
    private CommentFragment commentFragment;
    private StoreDetailFragment storeDetailFragment;
    public Response<Store> response;

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

        fragments.add(StoreHomeFragment.newInstance(id));
        fragments.add(GoodsListFragment.newInstance());
        fragments.add(FoodListFragment.newInstance());
        fragments.add(CommentFragment.newInstance(id));
        fragments.add(CouponFragment.newInstance(id));
        fragments.add(StoreDetailFragment.newInstance(id));

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                storeBottomL.setVisibility(position==1?View.VISIBLE:View.GONE);
                bottomBar.setVisibility(position==1?View.VISIBLE:View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(1);

        StoreApi.getStoreById(id).subscribe(new NetObserver<Response<Store>>() {
            @Override
            public void onNext(Response<Store> storeResponse) {
                super.onNext(storeResponse);
                response = storeResponse;
                isFoodStore(response.getData().isFoodStore());
                setInfo(response.getData());
                hadCollected(response.getData().collected);
            }
        });

        storeNotice.requestFocus();
        input.setHint(RichText.newRichText("    搜索店内商品").addimgRes(0, 2, R.drawable.ic_search, R.dimen.dp_14).build());
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

    @OnClick(R.id.store_intro)
    public void onInfo() {
        StoreInfoActivity.startStoreInfoActivity(this, response.getData());
    }
}
