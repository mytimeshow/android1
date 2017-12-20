package cn.czyugang.tcg.client.modules.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

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
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class GoodDetailActivity extends BaseActivity {
    @BindView(R.id.good_detail_tab)
    TabLayout tabLayout;
    @BindView(R.id.good_detail_pager)
    ViewPager viewPager;
    private List<BaseFragment> fragments = new ArrayList<>();

    public String id = "";
    public String storeId = "";

    private GoodFragment goodFragment;
    private GoodDetailFragment goodDetailFragment;
    private GoodCommentFragment goodCommentFragment;

    public static void startGoodDetailActivity(String id, String storeId) {
        Intent intent = new Intent(getTopActivity(), GoodDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("storeId", storeId);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        storeId = getIntent().getStringExtra("storeId");

        setContentView(R.layout.activity_good_detail);
        ButterKnife.bind(this);

        goodFragment = GoodFragment.newInstance();
        fragments.add(goodFragment);
        goodDetailFragment = GoodDetailFragment.newInstance();
        fragments.add(goodDetailFragment);
        goodCommentFragment = GoodCommentFragment.newInstance();
        fragments.add(goodCommentFragment);

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        LogRui.i("onCreate####");
        getGoodDetail();
    }

    private void getGoodDetail() {
        StoreApi.getGoodDetail(id).subscribe(new NetObserver<Response<Good>>() {
            @Override
            public void onNext(Response<Good> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    response.data.storeId=storeId;
                    if (response.values != null) {
                        ArrayList<String> list=new ArrayList<>();
                        list.add("919910512769269760");
                        list.add("919910512769269760");
                        list.add("919910512769269760");
                        list.add("919910512769269760");
                        response.data.picList=list;
                        JSONArray jsonArray=response.values.optJSONArray("inventoryList");
                        JSONObject jsonObject=jsonArray.optJSONObject(0);
                        response.data.inventoryId=jsonObject.optString("id");
                    }
                    goodFragment.setGoodInfo(response.data);
                }
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }
}
