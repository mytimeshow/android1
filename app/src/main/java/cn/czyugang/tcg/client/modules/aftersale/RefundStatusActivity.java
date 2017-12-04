package cn.czyugang.tcg.client.modules.aftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 钱款去向
 */

public class RefundStatusActivity extends BaseActivity {
    public static void startRefundStatusActivity() {
        Intent intent = new Intent(getTopActivity(), RefundStatusActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersale_refund_status);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }
}
