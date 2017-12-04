package cn.czyugang.tcg.client.modules.address.activity;

import android.app.Dialog;
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
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.Address;
import cn.czyugang.tcg.client.modules.address.adapter.AddressAdapter;
import cn.czyugang.tcg.client.modules.address.contract.AddressManageContract;
import cn.czyugang.tcg.client.modules.address.presenter.AddressManagePresenter;
import cn.czyugang.tcg.client.modules.common.dialog.MessageDialog;

/**
 * Created by wuzihong on 2017/10/7.
 * 地址管理
 */

public class AddressManageActivity extends BaseActivity implements AddressManageContract.View,
        BaseRecyclerAdapter.OnItemClickListener, MessageDialog.OnClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AddressAdapter mAdapter;

    private AddressManageContract.Presenter mPresenter;

    public static void startAddressManageActivity() {
        Intent intent = new Intent(getTopActivity(), AddressManageActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
        ButterKnife.bind(this);
        mPresenter = new AddressManagePresenter(this);
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
        DefaultItemDecoration itemDecoration = new DefaultItemDecoration(getResources(), DefaultItemDecoration.NOT_HEADER_FOOTER_DECORATION);
        itemDecoration.setDividerHeightResource(R.dimen.small);
        recyclerView.addItemDecoration(itemDecoration);
        mAdapter = new AddressAdapter(context);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateAddressList(List<Address> addressList) {
        mAdapter.setData(addressList);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_back, R.id.fl_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_add:
                AddAddressActivity.startAddAddressActivityForAdd();
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object data) {
        switch (view.getId()) {
            case R.id.tv_default_address:
                if ("NO".equals(((Address) data).getDefaultAddress())) {
                    mPresenter.setDefaultAddress(((Address) data).getId());
                }
                break;
            case R.id.tv_edit:
                AddAddressActivity.startAddAddressActivityForEdit((Address) data);
                break;
            case R.id.tv_delete:
                MessageDialog messageDialog = MessageDialog.newInstance()
                        .setMessage("确定删除地址吗？")
                        .setNegativeButton("取消")
                        .setPositiveButton("确定")
                        .setOnClickListener(this);
                Bundle args = new Bundle();
                args.putString("addressId", ((Address) data).getId());
                messageDialog.setArguments(args);
                messageDialog.show(getSupportFragmentManager(), "MessageDialog");
                break;
        }
    }

    @Override
    public void onClick(MessageDialog dialog, int what) {
        switch (what) {
            case Dialog.BUTTON_POSITIVE:
                mPresenter.deleteAddress(dialog.getArguments().getString("addressId"));
                break;
        }
    }
}
