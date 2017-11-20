package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.BankCardStyle;
import cn.czyugang.tcg.client.utils.img.ImageLoader;

/**
 * Created by wuzihong on 2017/10/17.
 * 银行卡解除绑定
 */

public class UnbindBankCardActivity extends BaseActivity {
    public static final String KEY_BANK_CARD_INFO = "bankCardInfo";
    @BindView(R.id.fresco_bank_logo)
    SimpleDraweeView fresco_bank_logo;
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @BindView(R.id.tv_bank_type)
    TextView tv_bank_type;
    @BindView(R.id.tv_bank_number)
    TextView tv_bank_number;

    private BankCardInfo mBankCardInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbind_bank_card);
        ButterKnife.bind(this);
        mBankCardInfo = getIntent().getParcelableExtra(KEY_BANK_CARD_INFO);
        initView();
    }

    private void initView() {
        if (mBankCardInfo != null) {
            BankCardStyle bankCardStyle = mBankCardInfo.getBankCardStyle();
            if (bankCardStyle != null) {
                ImageLoader.loadImageToView(bankCardStyle.getIconId(), fresco_bank_logo);
            }
            BankCard bankCard = mBankCardInfo.getBankCard();
            if (bankCard != null) {
                tv_bank.setText(bankCard.getOwnedBank());
                tv_bank_type.setText(bankCard.getCardType());
                tv_bank_number.setText(bankCard.getCardNum());
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_unbind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_unbind:
                Intent intent = new Intent(context, AddBankCardPasswordActivity.class);
                intent.putExtra(AddBankCardPasswordActivity.KEY_TYPE, AddBankCardPasswordActivity.TYPE_UNBIND_BANK_CARD);
                intent.putExtra(AddBankCardPasswordActivity.KEY_BANK_CARD_INFO, mBankCardInfo);
                startActivity(intent);
                finish();
                break;
        }
    }
}
