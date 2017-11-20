package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.entity.StaticDict;
import cn.czyugang.tcg.client.modules.balance.adapter.BillAdapter;
import cn.czyugang.tcg.client.modules.balance.contract.BillFilterContract;
import cn.czyugang.tcg.client.modules.balance.dialog.SelectBillTypeDialog;
import cn.czyugang.tcg.client.modules.balance.presenter.BillFilterPresenter;
import cn.czyugang.tcg.client.modules.balance.window.BillFilterWindow;
import cn.czyugang.tcg.client.utils.DateFormat;

/**
 * Created by wuzihong on 2017/10/18.
 * 账单筛选
 */

public class BillFilterActivity extends BaseActivity implements BillFilterContract.View,
        View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener,
        BillFilterWindow.OnBillFilterListener, SelectBillTypeDialog.OnSelectBillTypeListener {
    private final int REQUEST_SELECT_DATE = 0;
    @BindView(R.id.tv_filter)
    TextView tv_filter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    TextView tv_select_type;
    TextView tv_select_date;
    TextView tv_income;
    TextView tv_expense;
    private BillFilterWindow mBillFilterWindow;
    private BillAdapter mAdapter;

    private BillFilterContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_filter);
        ButterKnife.bind(this);
        mPresenter = new BillFilterPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_DATE:
                if (resultCode == RESULT_OK) {
                    Date startTime = null;
                    Date endTime = null;
                    if (data != null) {
                        startTime = (Date) data.getSerializableExtra(BillSelectDateActivity.KEY_START_TIME);
                        endTime = (Date) data.getSerializableExtra(BillSelectDateActivity.KEY_END_TIME);
                    }
                    String startTimeStr = null;
                    String endTimeStr = null;
                    if (startTime == null && endTime == null) {
                        tv_select_date.setText("按时间选择");
                    } else if (endTime == null) {
                        startTimeStr = DateFormat.format("yyyy-MM", startTime);
                        tv_select_date.setText(startTimeStr);
                    } else {
                        startTimeStr = DateFormat.formatDate(startTime);
                        endTimeStr = DateFormat.formatDate(endTime);
                        tv_select_date.setText(String.format("%s 至 %s", startTimeStr, endTimeStr));
                    }
                    mPresenter.selectDate(startTimeStr, endTimeStr);
                }
                break;
        }
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new BillAdapter(context);
        View header_bill = LayoutInflater.from(context).inflate(R.layout.header_bill_filter, recyclerView, false);
        mAdapter.addHeaderView(header_bill);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        tv_select_type = (TextView) header_bill.findViewById(R.id.tv_select_type);
        tv_select_date = (TextView) header_bill.findViewById(R.id.tv_select_date);
        tv_income = (TextView) header_bill.findViewById(R.id.tv_income);
        tv_expense = (TextView) header_bill.findViewById(R.id.tv_expense);

        tv_select_type.setOnClickListener(this);
        tv_select_date.setOnClickListener(this);
    }

    @Override
    public void updateBillList(List<Bill> billList, boolean isMore) {
        mAdapter.setData(billList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateIncomeExpense(double income, double expense) {
        tv_income.setText(String.format("+%.2f", new BigDecimal(income)));
        tv_expense.setText(String.format("-%.2f", new BigDecimal(expense)));
    }

    @OnClick({R.id.iv_back, R.id.tv_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_filter:
                if (mBillFilterWindow == null) {
                    mBillFilterWindow = new BillFilterWindow(this)
                            .setOnBillFilterListener(this);
                    mBillFilterWindow.setOnDismissListener(() -> tv_filter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_unfold_arrow, 0));
                }
                tv_filter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_fold_arrow, 0);
                mBillFilterWindow.showAsDropDown(tv_filter);
                break;
            case R.id.tv_select_type:
                SelectBillTypeDialog.newInstance()
                        .setBillTypeDictList(mPresenter.getBillTypeDictList())
                        .setOnSelectBillTypeListener(this)
                        .show(getSupportFragmentManager(), "SelectBillTypeDialog");
                break;
            case R.id.tv_select_date:
                startActivityForResult(new Intent(context, BillSelectDateActivity.class), REQUEST_SELECT_DATE);
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object data) {
        startActivity(new Intent(context, BillDetailsActivity.class));
    }

    @Override
    public void onBillFilter(BillFilterWindow window, int type) {
        switch (type) {
            case BillFilterWindow.ALL:
                mPresenter.selectAll();
                tv_filter.setText("全部");
                break;
            case BillFilterWindow.INCOME:
                mPresenter.selectIncome();
                tv_filter.setText("收入");
                break;
            case BillFilterWindow.EXPENSE:
                mPresenter.selectExpense();
                tv_filter.setText("支出");
                break;
        }
    }

    @Override
    public void onSelectBillType(SelectBillTypeDialog dialog, StaticDict billTypeDict) {
        if (billTypeDict != null) {
            tv_select_type.setText(billTypeDict.getName());
            mPresenter.selectType(billTypeDict.getId());
        }
    }
}
