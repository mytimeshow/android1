package cn.czyugang.tcg.client.modules.discount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2018/1/5
 *
 *  领券中心
 */

public class GetCouponActivity extends BaseActivity {
    public static void startGetCouponActivity( ){
        Intent intent=new Intent(getTopActivity(),GetCouponActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_get_coupon);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
}
