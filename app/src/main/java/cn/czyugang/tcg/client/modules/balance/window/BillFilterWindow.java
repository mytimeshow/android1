package cn.czyugang.tcg.client.modules.balance.window;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BasePopupWindow;

/**
 * Created by wuzihong on 2017/10/18.
 * 账单筛选弹窗
 */

public class BillFilterWindow extends BasePopupWindow {
    public static final int ALL = 0;
    public static final int INCOME = 1;
    public static final int EXPENSE = 2;

    private OnBillFilterListener mListener;

    public BillFilterWindow(Activity activity) {
        super(activity);
        View view = LayoutInflater.from(context).inflate(R.layout.window_bill_filter, null);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    public BillFilterWindow setOnBillFilterListener(OnBillFilterListener listener) {
        mListener = listener;
        return this;
    }

    @OnClick({R.id.tv_all, R.id.tv_income, R.id.tv_expense})
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.tv_all:
                    mListener.onBillFilter(this, ALL);
                    break;
                case R.id.tv_income:
                    mListener.onBillFilter(this, INCOME);
                    break;
                case R.id.tv_expense:
                    mListener.onBillFilter(this, EXPENSE);
                    break;
            }
        }
        dismiss();
    }

    public interface OnBillFilterListener {
        void onBillFilter(BillFilterWindow window, int type);
    }
}
