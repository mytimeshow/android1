package cn.czyugang.tcg.client.modules.groupon;

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
import cn.czyugang.tcg.client.api.RecordApi;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.im.ImChatActivity;
import cn.czyugang.tcg.client.modules.store.GoodCommentFragment;
import cn.czyugang.tcg.client.modules.store.GoodDetailFragment;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;

public class GroupGoodActivity extends BaseActivity {
    @BindView(R.id.good_detail_tab)
    TabLayout tabLayout;
    @BindView(R.id.good_detail_pager)
    ViewPager viewPager;
    private List<BaseFragment> fragments = new ArrayList<>();

    public String id = "";
    public String activityId = "";

    private ReduceProductFragment pageFragment;
    private GoodDetailFragment goodDetailFragment;
    private GoodCommentFragment goodCommentFragment;
    private Good good=null;

    public static void startGroupGoodActivity(String id, String activityId) {
        Intent intent = new Intent(getTopActivity(), GroupGoodActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("storeId", activityId);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        activityId = getIntent().getStringExtra("storeId");

        setContentView(R.layout.activity_group_good);
        ButterKnife.bind(this);

        pageFragment = ReduceProductFragment.newInstance();
        fragments.add(pageFragment);

        goodDetailFragment = GoodDetailFragment.newInstance();
        fragments.add(goodDetailFragment);
        goodCommentFragment = GoodCommentFragment.newInstance();
        fragments.add(goodCommentFragment);

        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        LogRui.i("onCreate####");
       // getGoodDetail();
    }

    private void getGoodDetail() {
        StoreApi.getGoodDetail(id).subscribe(new NetObserver<Response<Good>>() {
            @Override
            public void onNext(Response<Good> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    good=response.data;
                    good.storeId=activityId;
                    if (response.values != null) {
                        ArrayList<String> list=new ArrayList<>();
                        JSONArray jsonArray=response.values.optJSONArray("productPicList");
                        if (jsonArray!=null&&jsonArray.length()>0){
                            for(int i=0,size=jsonArray.length();i<size;i++){
                                JSONObject jsonObject=jsonArray.optJSONObject(i);
                                list.add(jsonObject.optString("picId"));
                            }
                        }
                        list.add("919910512769269760");
                        list.add("919910512769269760");
                        list.add("919910512769269760");
                        list.add("919910512769269760");
                        response.data.picList=list;
                        jsonArray=response.values.optJSONArray("storeInventoryList");
                        if (jsonArray!=null&&jsonArray.length()>0){
                            if (jsonArray.length()>1)  good.skuType="MULTI";
                            JSONObject jsonObject=jsonArray.optJSONObject(0);
                            good.showPrice=jsonObject.optDouble("price");
                            good.inventoryId=jsonObject.optString("id");
                            good.packagePrice=jsonObject.optDouble("packagePrice");
                        }
                        good.mergeInfoFromProductInfo(response.values.optJSONObject("productInfo"));
                        good.parseTagList(response.values);
                        good.hadCollect=response.values.optString("isCollect").equals("YES");
                    }
                    pageFragment.setGoodInfo(good);
                }
            }
        });

    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.title_im)
    public void onIm(){
        ImChatActivity.startImChatActivity();
    }

    @OnClick(R.id.title_more)
    public void onMore(){
        if (good==null) return;
        MyDialog.moreDialog(this,new MyDialog.MoreDialogListener(){
            @Override
            public void onCollect() {
                if (!good.hadCollect){
                    RecordApi.collect("PRODUCT",good.id).subscribe(new NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (ErrorHandler.judge200(response)){
                                AppUtil.toast("收藏成功");
                                good.hadCollect=true;
                            }
                        }
                    });
                }
            }

            @Override
            public boolean showCollect() {
                return true;
            }

            @Override
            public boolean hadCollect() {
                return good.hadCollect;
            }
        });
    }
}
