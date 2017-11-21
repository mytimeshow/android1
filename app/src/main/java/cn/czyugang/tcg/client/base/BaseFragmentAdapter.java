package cn.czyugang.tcg.client.base;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> list;

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getPageTitle(int position) {
        return list.get(position).getLabel();
    }
}

