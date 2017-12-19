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
 *  跑腿下单 帮我买
 */

public class ErrandOrderActivity extends BaseActivity {
    public static void startErrandOrderActivity( ){
        Intent intent=new Intent(getTopActivity(),ErrandOrderActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_order);
    }
}
