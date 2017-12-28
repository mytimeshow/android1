package cn.czyugang.tcg.client.modules.promote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.PromoterApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
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

    public static void startBecomePromoterActivity(boolean baseInfoFlag, boolean bankCardFlag, boolean realNameAuthFlag) {
        Intent intent = new Intent(getTopActivity(), BecomePromoterActivity.class);
        intent.putExtra("baseInfoFlag", baseInfoFlag);
        intent.putExtra("bankCardFlag", bankCardFlag);
        intent.putExtra("realNameAuthFlag", realNameAuthFlag);
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promoter_become);
        ButterKnife.bind(this);

        head.id(UserOAuth.getUserPhotoId());
        name.setText(UserOAuth.getUserNickname());
        init();

    }

    void init() {
        boolean baseInfoFlag = getIntent().getBooleanExtra("baseInfoFlag", false);
        boolean bankCardFlag = getIntent().getBooleanExtra("bankCardFlag", false);
        boolean realNameAuthFlag = getIntent().getBooleanExtra("realNameAuthFlag", false);
        basedataStatus.setText(baseInfoFlag?"已完成":"未完成");
        authStatus.setText(realNameAuthFlag?"已完成":"未完成");
        cardStatus.setText(bankCardFlag?"已完成":"未完成");
        basedataStatus.setTextColor(baseInfoFlag?getResources().getColor(R.color.main_red):getResources().getColor(R.color.text_gray));
        authStatus.setTextColor(realNameAuthFlag?getResources().getColor(R.color.main_red):getResources().getColor(R.color.text_gray));
        cardStatus.setTextColor(bankCardFlag?getResources().getColor(R.color.main_red):getResources().getColor(R.color.text_gray));
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
        PromoterApi.becomePromoter().subscribe(new NetObserver<Response<String>>() {
            @Override
            public void onNext(Response<String> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                Toast.makeText(BecomePromoterActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
