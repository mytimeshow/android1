package cn.czyugang.tcg.client.modules.login.dialog;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseDialogFragment;

/**
 * Created by wuzihong on 2017/9/27.
 * 输入图形验证码对话框
 */

public class InputCodeDialog extends BaseDialogFragment {
    @BindView(R.id.et_verification_code)
    EditText et_verification_code;
    @BindView(R.id.fresco_verification_code)
    SimpleDraweeView fresco_verification_code;

    private Uri mCodeUri;
    private OnClickListener mListener;

    public static InputCodeDialog newInstance() {
        return new InputCodeDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_input_code, container);
            ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    public InputCodeDialog setCodeUrl(String url) {
        if (url != null) {
            mCodeUri = Uri.parse(url);
        }
        return this;
    }

    public InputCodeDialog setOnClickListener(OnClickListener listener) {
        mListener = listener;
        return this;
    }

    private void initView() {
        Fresco.getImagePipeline().evictFromCache(mCodeUri);
        fresco_verification_code.setImageURI(mCodeUri);
    }

    @OnClick({R.id.fresco_verification_code, R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fresco_verification_code:
                Fresco.getImagePipeline().evictFromCache(mCodeUri);
                fresco_verification_code.setImageURI(mCodeUri);
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                mListener.onClick(this, et_verification_code.getText().toString());
                break;
        }
    }

    public interface OnClickListener {
        void onClick(InputCodeDialog dialog, String code);
    }
}
