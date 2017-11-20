package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.base.DefaultItemDecoration;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.modules.balance.adapter.BankCardAdapter;
import cn.czyugang.tcg.client.modules.balance.contract.BankCardManageContract;
import cn.czyugang.tcg.client.modules.balance.presenter.BankCardManagePresenter;

/**
 * Created by wuzihong on 2017/10/17.
 * 银行卡管理
 */

public class BankCardManageActivity extends BaseActivity implements BankCardManageContract.View,
        BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private BankCardAdapter mAdapter;

    private BankCardManageContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_manage);
        ButterKnife.bind(this);
        mPresenter = new BankCardManagePresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DefaultItemDecoration itemDecoration = new DefaultItemDecoration(getResources(), DefaultItemDecoration.FOOTER_DECORATION);
        itemDecoration.setDividerHeightResource(R.dimen.medium);
        recyclerView.addItemDecoration(itemDecoration);
        mAdapter = new BankCardAdapter(context);
        TextView header_bank_card_manage = (TextView) LayoutInflater.from(context).inflate(R.layout.header_bank_card_manage, recyclerView, false);
        mAdapter.addHeaderView(header_bank_card_manage);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateBankCardList(List<BankCardInfo> bankCardInfoList) {
        mAdapter.setData(bankCardInfoList);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object data) {
        Intent intent = new Intent();
        if (data == null) {
            if ("YES".equals(mPresenter.getIsSetPayPassword())) {
                intent.setClass(context, AddBankCardPasswordActivity.class);
                intent.putExtra(AddBankCardPasswordActivity.KEY_TYPE, AddBankCardPasswordActivity.TYPE_ADD_BANK_CARD);
            } else {
                intent.setClass(context, AddBankCardActivity.class);
            }
        } else {
            intent.setClass(context, UnbindBankCardActivity.class);
            intent.putExtra(UnbindBankCardActivity.KEY_BANK_CARD_INFO, (BankCardInfo) data);
        }
        startActivity(intent);
    }
}
