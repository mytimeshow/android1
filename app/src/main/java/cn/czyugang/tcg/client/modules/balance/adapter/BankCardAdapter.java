package cn.czyugang.tcg.client.modules.balance.adapter;

import android.content.Context;
import android.content.res.Resources;
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
 * Created by wuzihong on 2017/10/17.
 * 银行卡Adapter
 */

public class BankCardAdapter extends BaseRecyclerAdapter<BankCardInfo> {
    private final int VIEW_TYPE_ADD_BANK_CARD = 1;
    private Resources mResources;

    public BankCardAdapter(Context context) {
        super(context);
        mResources = context.getResources();
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ADD_BANK_CARD) {
            return new BaseRecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_add_bank_card, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bank_card, parent, false));
        }
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int position) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        if (position == dataList.size()) {
            holder.itemView.setPadding(0, mResources.getDimensionPixelOffset(R.dimen.small), 0, mResources.getDimensionPixelOffset(R.dimen.small));
            return;
        }
        ((ViewHolder) holder).fresco_bank_logo.setImageURI("");
        BankCardInfo bankCardInfo = dataList.get(position);
        if (bankCardInfo != null) {
            BankCard bankCard = bankCardInfo.getBankCard();
            BankCardStyle bankCardStyle = bankCardInfo.getBankCardStyle();
            if (bankCardStyle != null) {
                ImageLoader.loadImageToView(bankCardStyle.getIconId(), ((ViewHolder) holder).fresco_bank_logo);
            }
            if (bankCard != null) {
                ((ViewHolder) holder).tv_bank.setText(bankCard.getOwnedBank());
                ((ViewHolder) holder).tv_bank_type.setText(bankCard.getCardType());
                ((ViewHolder) holder).tv_bank_number.setText(bankCard.getCardNum());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if ((type == 0 && dataList == null && position == getHeaderSize()) || (type == 0 && dataList != null && position == getHeaderSize() + dataList.size())) {
            return VIEW_TYPE_ADD_BANK_CARD;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.fresco_bank_logo)
        SimpleDraweeView fresco_bank_logo;
        @BindView(R.id.tv_bank)
        TextView tv_bank;
        @BindView(R.id.tv_bank_type)
        TextView tv_bank_type;
        @BindView(R.id.tv_bank_number)
        TextView tv_bank_number;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
