package cn.czyugang.tcg.client.modules.entry.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.modules.entry.fragment.MyFragment;

/**
 * Created by wuzihong on 2017/9/13.
 * 主页
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.group_tab)
    RadioGroup group_tab;

    private MyFragment mMyFragment;
    private BaseFragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        group_tab.setOnCheckedChangeListener(this);
    }

    /**
     * 显示界面
     *
     * @param fragment
     */
    private void showFragment(BaseFragment fragment) {
        if (fragment == mCurrentFragment) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mCurrentFragment != null) {
            ft.detach(mCurrentFragment);
        }
        if (fragment.isDetached()) {
            ft.attach(fragment);
        } else {
            ft.add(R.id.fl_content, fragment);
        }
        ft.commit();
        mCurrentFragment = fragment;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_my:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance();
                }
                showFragment(mMyFragment);
                break;
        }
    }
}
