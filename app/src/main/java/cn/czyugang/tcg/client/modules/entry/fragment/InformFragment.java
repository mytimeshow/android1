package cn.czyugang.tcg.client.modules.entry.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruiaa.bottomnavigation.BottomBarView;
import com.ruiaa.bottomnavigation.ItemView;
import com.ruiaa.bottomnavigation.ScrollFrameView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.modules.store.GoodCommentFragment;
import cn.czyugang.tcg.client.modules.store.GoodDetailFragment;
import cn.czyugang.tcg.client.modules.store.GoodFragment;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

//资讯



public class InformFragment extends BaseFragment {

    @BindView(R.id.inform_detail_tab)
    TabLayout tabLayout;
    @BindView(R.id.inform_detail_pager)
    ViewPager viewPager;

    Context context=getContext();
    private List<BaseFragment> fragments=new ArrayList<>();


    public static InformFragment newInstance() {
        InformFragment fragment = new InformFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inform, container, false);
        ButterKnife.bind(this, rootView);


        viewPager.setAdapter(new BaseFragmentAdapter(getActivity().getSupportFragmentManager(),fragments));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        return rootView;
    }


}
