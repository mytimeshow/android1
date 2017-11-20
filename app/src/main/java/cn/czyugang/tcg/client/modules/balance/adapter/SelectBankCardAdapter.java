package cn.czyugang.tcg.client.modules.balance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.BankCardStyle;
import cn.czyugang.tcg.client.utils.ImageLoader;

/**
 * Created by wuzihong on 2017/11/3.
 */

public class SelectBankCardAdapter extends BaseRecyclerAdapter<BankCardInfo> {
    private String mBankCardId;

    public SelectBankCardAdapter(Context context, String bankCardId) {
        super(context);
        mBankCardId = bankCardId;
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_pay_type, parent, false));
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).fresco_bank_logo.setImageURI("");
        BankCardInfo bankCardInfo = dataList.get(position);
        ((ViewHolder) holder).onItemClickListenerHelper.setData(bankCardInfo);
        if (bankCardInfo != null) {
            BankCardStyle bankCardStyle = bankCardInfo.getBankCardStyle();
            if (bankCardStyle != null) {
                ImageLoader.loadImageToView(bankCardStyle.getIconId(), ((ViewHolder) holder).fresco_bank_logo);
            }
            BankCard bankCard = bankCardInfo.getBankCard();
            if (bankCard != null) {
                ((ViewHolder) holder).tv_bank.setText(String.format("%s%s(%s)", bankCard.getOwnedBank(), bankCard.getCardType(), bankCard.getCardNum().replace("*", "")));
                if (bankCard.getId().equals(mBankCardId)) {
                    ((ViewHolder) holder).tv_bank.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_select_bank_card_tick, 0);
                } else {
                    ((ViewHolder) holder).tv_bank.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        }
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.fresco_bank_logo)
        SimpleDraweeView fresco_bank_logo;
        @BindView(R.id.tv_bank)
        TextView tv_bank;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
