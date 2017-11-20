package cn.czyugang.tcg.client.modules.balance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.utils.DateFormat;

/**
 * Created by wuzihong on 2017/10/18.
 * 账单Adapter
 */

public class BillAdapter extends BaseRecyclerAdapter<Bill> {
    private Calendar mYesterday;
    private Calendar mToday;

    public BillAdapter(Context context) {
        super(context);
        Calendar now = Calendar.getInstance();
        mToday = Calendar.getInstance();
        mToday.clear();
        mToday.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        mYesterday = Calendar.getInstance();
        mYesterday.clear();
        mYesterday.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - 1);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false));
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int position) {
        Bill bill = dataList.get(position);
        if (bill != null) {
            ((ViewHolder) holder).tv_type.setText(bill.getActionTypeStr());
            ((ViewHolder) holder).tv_money.setText(String.format(BalanceApi.TYPE_INCOME.equals(bill.getSteamType()) ? "+%.2f" : "-%.2f", new BigDecimal(bill.getTradePrice())));
            Date updateTime = bill.getCreateTime();
            if (updateTime != null) {
                Calendar updateTimeCalendar = Calendar.getInstance();
                updateTimeCalendar.setTime(updateTime);
                if (updateTimeCalendar.after(mToday)) {
                    ((ViewHolder) holder).tv_time.setText(DateFormat.format("今天 HH:mm", updateTime));
                } else if (updateTimeCalendar.after(mYesterday)) {
                    ((ViewHolder) holder).tv_time.setText(DateFormat.format("昨天 HH:mm", updateTime));
                } else {
                    ((ViewHolder) holder).tv_time.setText(DateFormat.format("yyyy-MM-dd HH:mm", updateTime));
                }
            }
            ((ViewHolder) holder).tv_status.setText(bill.getStatusStr());
        }
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.tv_type)
        TextView tv_type;
        @BindView(R.id.tv_money)
        TextView tv_money;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_status)
        TextView tv_status;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
