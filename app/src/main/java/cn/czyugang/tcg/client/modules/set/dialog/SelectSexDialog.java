package cn.czyugang.tcg.client.modules.set.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseDialogFragment;

/**
 * Created by wuzihong on 2017/9/30.
 * 选择性别对话框
 */

public class SelectSexDialog extends BaseDialogFragment {
    public static final int MAN = 0;
    public static final int WOMAN = 1;
    private OnSelectSexListener mListener;

    public static SelectSexDialog newInstance() {
        return new SelectSexDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_select_sex, container);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    protected void initWindow(Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
    }

    public SelectSexDialog setOnSelectSexListener(OnSelectSexListener listener) {
        mListener = listener;
        return this;
    }

    @OnClick({R.id.tv_man, R.id.tv_woman, R.id.tv_cancel})
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.tv_man:
                    mListener.onSelectSex(this, MAN);
                    break;
                case R.id.tv_woman:
                    mListener.onSelectSex(this, WOMAN);
                    break;
            }
        }
        dismiss();
    }

    public interface OnSelectSexListener {
        void onSelectSex(SelectSexDialog dialog, int sex);
    }
}
