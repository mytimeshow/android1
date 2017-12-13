package cn.czyugang.tcg.client.modules.entry.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ruiaa.bottomnavigation.BottomBarView;
import com.ruiaa.bottomnavigation.ItemView;
import com.ruiaa.bottomnavigation.ScrollFrameView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.entry.fragment.CategoryFragment;
import cn.czyugang.tcg.client.modules.entry.fragment.HomepageFragment;
import cn.czyugang.tcg.client.modules.entry.fragment.InformFragment;
import cn.czyugang.tcg.client.modules.entry.fragment.MyFragment;
import cn.czyugang.tcg.client.modules.entry.fragment.TrolleyFragment;

/**
 * Created by wuzihong on 2017/9/13.
 * 主页
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_frame)
    ScrollFrameView mainFrame;
    @BindView(R.id.main_bottom)
    BottomBarView bottomBar;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomBar
                .setImgSizeRes(R.dimen.dp_20)
                .setTextImgDistanceRes(R.dimen.dp_4)
                .setTextSize(12)
                .setTextColorRes(R.color.main_red,R.color.text_gray)
                .addItem(new ItemView(bottomBar).setContent("商城", R.drawable.icon_mall_red, R.drawable.icon_mall))
                .addItem(new ItemView(bottomBar).setContent("资讯", R.drawable.icon_local_red, R.drawable.icon_local))
                .addItem(new ItemView(bottomBar).setContent("分类", R.drawable.icon_classify_red, R.drawable.icon_classify))
                .addItem(new ItemView(bottomBar).setContent("购物车", R.drawable.trolley_red, R.drawable.trolley_grey))
                .addItem(new ItemView(bottomBar).setContent("我的", R.drawable.icon_mine_red, R.drawable.icon_mine))
                .setOnSelectListener(this::onChangeFragment)
                .init();

        fragments.add(HomepageFragment.newInstance());
        fragments.add(InformFragment.newInstance());
        fragments.add(CategoryFragment.newInstance());
        fragments.add(TrolleyFragment.newInstance());
        fragments.add(MyFragment.newInstance());

        mainFrame.setFragmentList(getSupportFragmentManager(), fragments);
        mainFrame.setBottomBarView(bottomBar);

    }

    public void selectFragment(int index) {
        bottomBar.setSelectWithFrame(index);
    }

    private void onChangeFragment(int index) {

    }

    public static void openAndSelectFragment(int index){
        clearAllActivityExceptMain().selectFragment(index);
    }
}
