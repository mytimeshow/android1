package cn.czyugang.tcg.client.modules.promote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.balance.activity.AddBankCardActivity;
import cn.czyugang.tcg.client.modules.set.activity.AccountInfoActivity;
import cn.czyugang.tcg.client.modules.set.activity.RealNameAuthActivity;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class BecomePromoterActivity extends BaseActivity {

    @BindView(R.id.promote_become_head)
    ImgView head;
    @BindView(R.id.promote_become_name)
    TextView name;
    @BindView(R.id.promote_become_basedata_status)
    TextView basedataStatus;
    @BindView(R.id.promote_become_auth_status)
    TextView authStatus;
    @BindView(R.id.promote_become_card_status)
    TextView cardStatus;
    @BindView(R.id.promote_become_commit)
    TextView commit;

    public static void startBecomePromoterActivity() {
        Intent intent = new Intent(getTopActivity(), BecomePromoterActivity.class);
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promoter_become);
        ButterKnife.bind(this);

        head.drawableId(R.drawable.icon_filter_label_bg);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.promote_become_basedata)
    public void onBasedata() {
        AccountInfoActivity.startAccountInfoActivity();
    }

    @OnClick(R.id.promote_become_auth)
    public void onAuth() {
        RealNameAuthActivity.startRealNameAuthActivity();
    }

    @OnClick(R.id.promote_become_card)
    public void onCard() {
        AddBankCardActivity.startAddBankCardActivity();
    }

    @OnClick(R.id.promote_become_commit)
    public void onCommit() {

    }


}
