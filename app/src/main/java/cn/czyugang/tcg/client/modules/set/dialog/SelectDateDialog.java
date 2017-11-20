package cn.czyugang.tcg.client.modules.set.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseDialogFragment;
import cn.czyugang.tcg.client.utils.DateFormat;

/**
 * Created by wuzihong on 2017/9/30.
 * 选择生日对话框
 */

public class SelectDateDialog extends BaseDialogFragment {
    @BindView(R.id.datePicker)
    DatePicker datePicker;
    private OnSelectDateListener mListener;

    public static SelectDateDialog newInstance() {
        return new SelectDateDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_select_date, container);
            ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    private void initView() {
        datePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    protected void initWindow(Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
    }

    public SelectDateDialog setOnSelectDateListener(OnSelectDateListener listener) {
        mListener = listener;
        return this;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.tv_confirm:
                    mListener.onSelectDate(this, DateFormat.parseDate(datePicker.getYear() + "-" +
                            (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth()));
                    break;
            }
        }
        dismiss();
    }

    public interface OnSelectDateListener {
        void onSelectDate(SelectDateDialog dialog, Date date);
    }
}
