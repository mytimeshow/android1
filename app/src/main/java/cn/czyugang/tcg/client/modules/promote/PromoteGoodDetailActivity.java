package cn.czyugang.tcg.client.modules.promote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoteGoodDetailActivity extends BaseActivity {
    public static void startPromoteGoodDetailActivity( ){
        Intent intent=new Intent(getTopActivity(),PromoteGoodDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_goods_detail);
    }
}
