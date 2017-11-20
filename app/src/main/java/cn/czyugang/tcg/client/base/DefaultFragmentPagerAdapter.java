package cn.czyugang.tcg.client.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wuzihong on 2017/9/16.
 * 默认的FragmentPager适配器
 */

public class DefaultFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mList;

    public DefaultFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
}
