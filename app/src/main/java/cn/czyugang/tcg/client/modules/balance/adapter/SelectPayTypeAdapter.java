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
import cn.czyugang.tcg.client.utils.img.ImageLoader;

/**
 * Created by wuzihong on 2017/10/17.
 * 银行卡Adapter
 */

public class SelectPayTypeAdapter extends BaseRecyclerAdapter<BankCardInfo> {
    public SelectPayTypeAdapter(Context context) {
        super(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_pay_type, parent, false));
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                ((ViewHolder) holder).fresco_bank_logo.setImageURI("res:///" + R.drawable.ic_recharge_wx);
                ((ViewHolder) holder).tv_bank.setText("微信支付");
                break;
            case 1:
                ((ViewHolder) holder).fresco_bank_logo.setImageURI("res:///" + R.drawable.ic_recharge_alipay);
                ((ViewHolder) holder).tv_bank.setText("支付宝");
                break;
            case 2:
                ((ViewHolder) holder).fresco_bank_logo.setImageURI("res:///" + R.drawable.ic_recharge_recharge_card);
                ((ViewHolder) holder).tv_bank.setText("充值卡");
                break;
            default:
                if (dataList == null || position == dataList.size() + 3) {
                    ((ViewHolder) holder).fresco_bank_logo.setImageURI("res:///" + R.drawable.ic_recharge_add_bank);
                    ((ViewHolder) holder).tv_bank.setText("添加新银行卡付款");
                } else {
                    ((ViewHolder) holder).fresco_bank_logo.setImageURI("");
                    BankCardInfo bankCardInfo = dataList.get(position - 3);
                    ((ViewHolder) holder).onItemClickListenerHelper.setData(bankCardInfo);
                    if (bankCardInfo != null) {
                        BankCardStyle bankCardStyle = bankCardInfo.getBankCardStyle();
                        if (bankCardStyle != null) {
                            ImageLoader.loadImageToView(bankCardStyle.getIconId(), ((ViewHolder) holder).fresco_bank_logo);
                        }
                        BankCard bankCard = bankCardInfo.getBankCard();
                        if (bankCard != null) {
                            ((ViewHolder) holder).tv_bank.setText(String.format("%s%s(%s)", bankCard.getOwnedBank(), bankCard.getCardType(), bankCard.getCardNum().replace("*", "")));
                        }
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 4;
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
