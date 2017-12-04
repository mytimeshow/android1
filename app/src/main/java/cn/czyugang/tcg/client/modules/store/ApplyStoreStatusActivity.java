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

    public static void startApplyStoreStatusActivity() {
        Intent intent = new Intent(getTopActivity(), ApplyStoreStatusActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store_status);
        ButterKnife.bind(this);

        status.setText("您的申请不通过\n原因：城市不再服务范围");
        contact.setVisibility(View.GONE);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }
    
    @OnClick(R.id.commit)
    public void onCommit(){
        ApplyStoreActivity.startApplyStoreActivity();
        finish();
    }
}
