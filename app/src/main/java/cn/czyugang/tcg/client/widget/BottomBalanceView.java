package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.TrolleyCheckResponse;
import cn.czyugang.tcg.client.entity.TrolleyGoods;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.order.ConfirmOrderActivity;

/**
 * @author ruiaa
 * @date 2017/12/8
 */

public class BottomBalanceView extends LinearLayout {
    public BottomBalanceView(Context context) {
        super(context);
        init(context);
    }

    public BottomBalanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomBalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private TrolleyStore trolleyStore = null;

    public ImageView trolleyImg;
    private TextView num;
    private TextView price;
    private TextView commit;
    private TextView rest;

    private void init(@NonNull Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_balance, this, true);
        trolleyImg = findViewById(R.id.view_balance_trolley_img);
        num = findViewById(R.id.view_balance_num);
        price = findViewById(R.id.view_balance_price);
        commit = findViewById(R.id.view_balance_commit);
        rest = findViewById(R.id.view_balance_rest);
        setOnClickListener(v -> {
        });
        commit.setOnClickListener(v -> {
            sync();
        });
    }

    private void sync() {
        StoreApi.syncTrolleyGoods(trolleyStore).subscribe(new BaseActivity.NetObserver<Response<List<TrolleyGoods>>>() {
            @Override
            public void onNext(Response<List<TrolleyGoods>> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    for (TrolleyGoods sync : response.data)
                        sync.setInfoFromSyncToLocal(trolleyStore);
                    checkTrolley();
                }
            }
        });
    }

    private void checkTrolley() {
        StringBuilder builder = new StringBuilder();
        for (TrolleyGoods t : trolleyStore.trolleyGoodsMap.values()) {
            if (!t.hadDeleted() && t.isSelect && !t.trolleyId.equals("")) {
                builder.append(t.trolleyId);
                builder.append(",");
            }
        }
        if (builder.length() == 0) return;
        builder.deleteCharAt(builder.length() - 1);
        String shoppingCartIds=builder.toString();
        StoreApi.checkTrolley(shoppingCartIds).subscribe(new BaseActivity.NetObserver<TrolleyCheckResponse>() {
            @Override
            public void onNext(TrolleyCheckResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    if (response.errorTrolleyGoods.isEmpty()) {
                        ConfirmOrderActivity.startConfirmOrderActivity(shoppingCartIds);
                    }
                }
            }
        });
    }

    public void setTrolleyStore(TrolleyStore trolleyStore) {
        this.trolleyStore = trolleyStore;
        refresh();
    }

    public void refresh() {
        int buyNum = trolleyStore.getGoodsBuyNum();
        if (buyNum == 0) {
            num.setVisibility(GONE);
            price.setText("未选购商品哦");
        } else {
            num.setVisibility(VISIBLE);
            num.setText(String.valueOf(buyNum));
            price.setText(trolleyStore.getAllPriceStr());
        }
        if (trolleyStore.getAllPrice() >= 10) {
            commit.setText("结算");
            commit.setBackgroundResource(R.color.main_red);
            commit.setClickable(true);
        } else {
            commit.setText("" + 10 + "元起送");
            commit.setBackgroundResource(R.color.grey_350);
            commit.setClickable(false);
        }
    }
}
