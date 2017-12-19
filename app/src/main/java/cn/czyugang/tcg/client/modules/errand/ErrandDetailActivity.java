package cn.czyugang.tcg.client.modules.errand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/18
 *
 *  跑腿订单详情
 */

public class ErrandDetailActivity extends BaseActivity {
    public static void startErrandDetailActivity( ){
        Intent intent=new Intent(getTopActivity(),ErrandDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_detail);
    }
}
