package cn.czyugang.tcg.client.modules.balance.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseDialogFragment;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.base.DefaultItemDecoration;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.modules.balance.activity.AddBankCardActivity;
import cn.czyugang.tcg.client.modules.balance.activity.AddBankCardPasswordActivity;
import cn.czyugang.tcg.client.modules.balance.adapter.SelectPayTypeAdapter;

/**
 * Created by wuzihong on 2017/11/2.
 */

public class SelectPayTypeDialog extends BaseDialogFragment implements BaseRecyclerAdapter.OnItemClickListener {
    public static final int WX = 0;
    public static final int ALIPAY = 1;
    public static final int RECHARGE_CARD = 2;
    public static final int BANK_CARD = 3;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SelectPayTypeAdapter mAdapter;

    private List<BankCardInfo> mBankCardList;
    private String mIsSetPassword;
    private OnSelectPayTypeListener mListener;

    public static SelectPayTypeDialog newInstance() {
        return new SelectPayTypeDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_select_pay_type, container);
            ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    @Override
    protected void initWindow(Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
    }

    public SelectPayTypeDialog setRankCardList(List<BankCardInfo> bankCardList, String isSetPassword) {
        mBankCardList = bankCardList;
        mIsSetPassword = isSetPassword;
        if (mAdapter != null) {
            mAdapter.setData(bankCardList);
            mAdapter.notifyDataSetChanged();
        }
        return this;
    }

    public SelectPayTypeDialog setOnSelectPayTypeListener(OnSelectPayTypeListener listener) {
        mListener = listener;
        return this;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DefaultItemDecoration itemDecoration = new DefaultItemDecoration(getResources(), DefaultItemDecoration.FOOTER_DECORATION);
        itemDecoration.setDividerHeightResource(R.dimen.divider);
        itemDecoration.setDividerColorResource(R.color.div_light);
        recyclerView.addItemDecoration(itemDecoration);
        mAdapter = new SelectPayTypeAdapter(context);
        mAdapter.setData(mBankCardList);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.tv_close)
    public void onClick() {
        dismiss();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object data) {
        switch (position) {
            case 0:
                if (mListener != null) {
                    mListener.OnSelectPayType(this, WX, null);
                }
                break;
            case 1:
                if (mListener != null) {
                    mListener.OnSelectPayType(this, ALIPAY, null);
                }
                break;
            case 2:
                if (mListener != null) {
                    mListener.OnSelectPayType(this, RECHARGE_CARD, null);
                }
                break;
            default:
                if (mBankCardList == null || position == mBankCardList.size() + 3) {
                    Intent intent = new Intent();
                    if ("YES".equals(mIsSetPassword)) {
                        intent.setClass(context, AddBankCardPasswordActivity.class);
                        intent.putExtra(AddBankCardPasswordActivity.KEY_TYPE, AddBankCardPasswordActivity.TYPE_ADD_BANK_CARD);
                    } else {
                        intent.setClass(context, AddBankCardActivity.class);
                    }
                    startActivity(intent);
                } else {
                    if (mListener != null) {
                        mListener.OnSelectPayType(this, BANK_CARD, data);
                    }
                }
                break;
        }
        dismiss();
    }

    public interface OnSelectPayTypeListener {
        void OnSelectPayType(SelectPayTypeDialog dialog, int type, Object data);
    }
}
