package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.base.DefaultItemDecoration;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.modules.balance.adapter.SelectBankCardAdapter;

/**
 * Created by wuzihong on 2017/11/3.
 */

public class SelectBankCardActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {
    public static final String KEY_BANK_CARD = "bankCard";
    public static final String KEY_BANK_CARD_ID = "bankCardId";
    public static final String KEY_BANK_CARD_LIST = "bankCardList";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SelectBankCardAdapter mAdapter;

    private List<BankCardInfo> mBankCardList;
    private String mBankCardId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank_card);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mBankCardList = intent.getParcelableArrayListExtra(KEY_BANK_CARD_LIST);
        mBankCardId = intent.getStringExtra(KEY_BANK_CARD_ID);
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DefaultItemDecoration itemDecoration = new DefaultItemDecoration(getResources(), DefaultItemDecoration.NOT_HEADER_FOOTER_DECORATION);
        itemDecoration.setDividerColorResource(R.color.div_light);
        itemDecoration.setDividerHeightResource(R.dimen.divider);
        recyclerView.addItemDecoration(itemDecoration);
        mAdapter = new SelectBankCardAdapter(context, mBankCardId);
        mAdapter.setData(mBankCardList);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object data) {
        Intent intent = new Intent();
        intent.putExtra(KEY_BANK_CARD, (BankCardInfo) data);
        setResult(RESULT_OK, intent);
        finish();
    }
}
