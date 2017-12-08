package cn.czyugang.tcg.client.modules.entry.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.modules.inform.InformColumnFragment;
import cn.czyugang.tcg.client.modules.inform.InformFollowFragment;
import cn.czyugang.tcg.client.modules.inform.InformNewsFragment;
import cn.czyugang.tcg.client.modules.inform.MySelfActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;

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


        fragments.add(InformNewsFragment.newInstance());
        fragments.add(InformFollowFragment.newInstance());
        fragments.add(InformColumnFragment.newInstance());

        viewPager.setAdapter(new BaseFragmentAdapter(getActivity().getSupportFragmentManager(),fragments));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setTabLayoutIndicator(tabLayout, ResUtil.getDimenInPx(R.dimen.dp_12),ResUtil.getDimenInPx(R.dimen.dp_12));
            }
        },50);
        return rootView;
    }

    @OnClick(R.id.img_head)
    public void onMyHead(){
        MySelfActivity.startMySelfActivity();
    }

}
