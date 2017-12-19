package cn.czyugang.tcg.client.modules.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.StoreApplyInfo;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 入驻申请状态
 */

public class ApplyStoreStatusActivity extends BaseActivity {
    @BindView(R.id.apply_store_status)
    TextView status;
    @BindView(R.id.apply_store_status_contact)
    TextView contact;
    @BindView(R.id.commit)
    TextView commit;

    private StoreApplyInfo storeApplyInfo = null;
    private String hotlinePhone = "";

    public static void startApplyStoreStatusActivity(String hotlinePhone) {
        Intent intent = new Intent(getTopActivity(), ApplyStoreStatusActivity.class);
        intent.putExtra("hotlinePhone", hotlinePhone);
        getTopActivity().startActivity(intent);
    }

    public static void startApplyStoreStatusActivity(StoreApplyInfo storeApplyInfo) {
        Intent intent = new Intent(getTopActivity(), ApplyStoreStatusActivity.class);
        MyApplication.getInstance().activityTransferData = storeApplyInfo;
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.getInstance().activityTransferData != null) {
            storeApplyInfo = (StoreApplyInfo) MyApplication.getInstance().activityTransferData;
            MyApplication.getInstance().activityTransferData = null;
        } else {
            hotlinePhone = getIntent().getStringExtra("hotlinePhone");
        }

        setContentView(R.layout.activity_apply_store_status);
        ButterKnife.bind(this);


        if (storeApplyInfo == null) {
            status.setText("您的申请已经提交，正在审核中。\n请耐心等待");
            contact.setText("客服电话：" + hotlinePhone);
            commit.setVisibility(View.GONE);
        } else {
            //申请单状态(REJECT-被拒绝，WAITING-等待审核，DOING-业务员跟进中，SUCCESS-审核成功)
            //操作后对应的状态为：商家入驻申请->WAITING; 后台初审->DIONG；后台复审->SUCCESS；初审拒绝->REJECT；复审拒绝-REVIEW-初审"
            switch (storeApplyInfo.status) {
                case "WAITING": {
                    //等待审核
                    status.setText("您的申请已经提交，正在审核中。\n请耐心等待");
                    contact.setText("客服电话：" + storeApplyInfo.consumerHotline);
                    commit.setVisibility(View.GONE);
                    break;
                }
                case "DOING": {
                    //业务员跟进中
                    status.setText("您的申请已有业务经理在处理，\n请耐心等待");
                    contact.setText("业务经理：" + storeApplyInfo.businessManagerName
                            + "\n联系电话：" + storeApplyInfo.businessManagerPhone);
                    commit.setVisibility(View.GONE);
                    break;
                }
                case "SUCCESS": {
                    //审核成功
                    showToast("您已是商户，无法进行申请");
                    finish();
                    break;
                }
                case "REJECT": {
                    //被拒绝
                    status.setText("您的申请不通过\n原因：" + storeApplyInfo.reason);
                    contact.setVisibility(View.GONE);
                    commit.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.commit)
    public void onCommit() {
        ApplyStoreActivity.startApplyStoreActivity();
        finish();
    }
}
