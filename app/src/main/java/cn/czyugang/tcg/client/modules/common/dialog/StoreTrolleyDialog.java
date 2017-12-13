package cn.czyugang.tcg.client.modules.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.rxbus.TrolleyBuyNumChangedEvent;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import cn.czyugang.tcg.client.widget.BottomBalanceView;
import cn.czyugang.tcg.client.widget.RecyclerViewMaxH;
import cn.czyugang.tcg.client.widget.SelectButton;
import io.reactivex.disposables.Disposable;

/**
 * @author ruiaa
 * @date 2017/12/8
 */

public class StoreTrolleyDialog extends DialogFragment {

    private Activity activity;
    private TrolleyStore trolleyStore = null;
    private SelectButton selectButton;
    private TextView name;
    private View clear;
    private RecyclerViewMaxH goods;
    private TextView packFee;
    private BottomBalanceView bottomBalanceView;
    private DialogInterface.OnDismissListener onDismissListener = null;

    private Disposable disposableRefreshBottomTrolley = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.BottomDialogAnimation);

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) onDismissListener.onDismiss(dialog);
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(-1, -2);
        window.setGravity(Gravity.BOTTOM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_store_trolley, container);
        selectButton = rootView.findViewById(R.id.view_select);
        name = rootView.findViewById(R.id.view_name);
        clear = rootView.findViewById(R.id.view_clear);
        goods = rootView.findViewById(R.id.view_goods);
        packFee = rootView.findViewById(R.id.view_pack_fee);
        bottomBalanceView = rootView.findViewById(R.id.view_bottom);

        bottomBalanceView.trolleyImg.setOnClickListener(v -> dismiss());
        bottomBalanceView.setTrolleyStore(trolleyStore);

        goods.setMaxHeight(ResUtil.getDimenInPx(R.dimen.dp_280));

        selectButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            trolleyStore.selectAll(isChecked);
            trolleyStore.adapter.notifyDataSetChanged();
            bottomBalanceView.refresh();
        });
        clear.setOnClickListener(v -> {
            trolleyStore.clearAll();
            trolleyStore.adapter.notifyDataSetChanged();
            bottomBalanceView.refresh();
            dismiss();
        });

        disposableRefreshBottomTrolley = RxBus.toObservable(TrolleyBuyNumChangedEvent.class).subscribe(event -> {
            bottomBalanceView.refresh();
        });

        trolleyStore.bindGoodsAdapter(activity, goods, true,trolleyStore);
        bottomBalanceView.refresh();
        selectButton.setChecked(false);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposableRefreshBottomTrolley != null) {
            disposableRefreshBottomTrolley.dispose();
            disposableRefreshBottomTrolley = null;
        }
    }

    public void setTrolleyStore(TrolleyStore trolleyStore, Activity activity) {
        this.trolleyStore = trolleyStore;
        this.activity = activity;
    }

    public void setOnDismissRefresh(DialogInterface.OnDismissListener listener) {
        onDismissListener = listener;
    }


}
