package cn.czyugang.tcg.client.modules.entry.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruiaa.bottomnavigation.BottomBarView;
import com.ruiaa.bottomnavigation.ItemView;
import com.ruiaa.bottomnavigation.ScrollFrameView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

//资讯



public class InformFragment extends BaseFragment {

    @BindView(R.id.inform_frame)
    ScrollFrameView informFrame;
    @BindView(R.id.inform_bottom)
    BottomBarView bottomBar;

    Context context=getContext();
    private ArrayList<Fragment> fragments = new ArrayList<>();


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


        return rootView;
    }

    public void selectFragment(int index){
        bottomBar.setSelectWithFrame(index);
    }

    private void onChangeFragment(int index){

    }
}
