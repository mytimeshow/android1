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
                .addItem(new ItemView(bottomBar).setContent("首页", R.drawable.ic_address_location, R.drawable.ic_address_location))
                .addItem(new ItemView(bottomBar).setContent("资讯", R.drawable.ic_address_location, R.drawable.ic_address_location))
                .addItem(new ItemView(bottomBar).setContent("分类", R.drawable.ic_address_location, R.drawable.ic_address_location))
                .addItem(new ItemView(bottomBar).setContent("购物车", R.drawable.ic_address_location, R.drawable.ic_address_location))
                .addItem(new ItemView(bottomBar).setContent("我的", R.drawable.ic_address_location, R.drawable.ic_address_location))
                .setOnSelectListener(this::onChangeFragment)
                .init();

        fragments.add(HomepageFragment.newInstance());
        fragments.add(InformFragment.newInstance());
        fragments.add(CategoryFragment.newInstance());
        fragments.add(TrolleyFragment.newInstance());
        fragments.add(MyFragment.newInstance());

        mainFrame.setFragmentList(getSupportFragmentManager(),fragments);
        mainFrame.setBottomBarView(bottomBar);

    }

    private void onChangeFragment(int index){

    }
}
