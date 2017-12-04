package cn.czyugang.tcg.client.modules.aftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 退款/售后详情
 */

public class AftersaleDetailActivity extends BaseActivity {
    @BindView(R.id.aftersale_status)
    TextView status;
    @BindView(R.id.aftersale_status_tip)
    TextView statusTip;
    @BindView(R.id.aftersale_store_name)
    TextView storeName;
    @BindView(R.id.aftersale_service_id)
    TextView serviceId;
    @BindView(R.id.aftersale_type)
    TextView type;
    @BindView(R.id.aftersale_money)
    TextView money;
    @BindView(R.id.aftersale_reason)
    TextView reason;
    @BindView(R.id.aftersale_explain)
    TextView explain;
    @BindView(R.id.aftersale_time_apply)
    TextView timeApply;
    @BindView(R.id.aftersale_time_refund)
    TextView timeRefund;
    @BindView(R.id.aftersale_time_close)
    TextView timeClose;
    @BindView(R.id.aftersale_revoke)
    TextView revoke;
    @BindView(R.id.aftersale_platform_interpose)
    TextView platformInterpose;
    @BindView(R.id.aftersale_return_goods)
    TextView returnGoods;

    public static void startAftersaleDetailActivity() {
        Intent intent = new Intent(getTopActivity(), AftersaleDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersale_detail);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.aftersale_statusL)
    public void onOpenTrack(){
        AftersaleTrackActivity.startAftersaleTrackActivity();
    }
}
