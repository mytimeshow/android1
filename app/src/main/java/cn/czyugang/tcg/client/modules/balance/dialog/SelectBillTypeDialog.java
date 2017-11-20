package cn.czyugang.tcg.client.modules.balance.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseDialogFragment;
import cn.czyugang.tcg.client.entity.StaticDict;
import io.reactivex.Observable;

/**
 * Created by wuzihong on 2017/10/27.
 */

public class SelectBillTypeDialog extends BaseDialogFragment {
    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;
    private List<StaticDict> mBillTypeDictList;
    private OnSelectBillTypeListener mListener;

    public static SelectBillTypeDialog newInstance() {
        return new SelectBillTypeDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_select_bill_type, container);
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

    public SelectBillTypeDialog setBillTypeDictList(List<StaticDict> billTypeDictList) {
        mBillTypeDictList = billTypeDictList;
        return this;
    }

    public SelectBillTypeDialog setOnSelectBillTypeListener(OnSelectBillTypeListener listener) {
        mListener = listener;
        return this;
    }

    private void initView() {
        numberPicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        if (mBillTypeDictList != null && mBillTypeDictList.size() > 0) {
            List<String> nameList = new ArrayList<>();
            Observable.fromIterable(mBillTypeDictList)
                    .subscribe(billTypeDict -> nameList.add(billTypeDict.getName()));
            numberPicker.setDisplayedValues(nameList.toArray(new String[nameList.size()]));
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(nameList.size() - 1);
        } else {
            numberPicker.setDisplayedValues(new String[]{" "});
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(0);
        }
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.tv_confirm:
                    int position = numberPicker.getValue();
                    if (mBillTypeDictList != null && mBillTypeDictList.size() > position) {
                        mListener.onSelectBillType(this, mBillTypeDictList.get(position));
                    }
                    break;
            }
        }
        dismiss();
    }

    public interface OnSelectBillTypeListener {
        void onSelectBillType(SelectBillTypeDialog dialog, StaticDict billTypeDict);
    }
}
