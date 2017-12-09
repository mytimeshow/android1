package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;

/**
 * @author ruiaa
 * @date 2017/12/9
 */

public class GoodsPlusMinusView extends FrameLayout {

    @BindView(R.id.view_plus_minusL)
    View plusMinusL;
    @BindView(R.id.view_multi_specL)
    View multiSpecL;
    @BindView(R.id.view_minus)
    View minus;
    @BindView(R.id.view_minus_bar)
    View minusBar;
    @BindView(R.id.view_num)
    TextView num;
    @BindView(R.id.view_num_bar)
    View numBar;
    @BindView(R.id.view_plus)
    ImageView plus;
    @BindView(R.id.view_multi_spec)
    View multiSpec;
    @BindView(R.id.view_multi_spec_num)
    TextView multiSpecNum;

    private boolean isMultiSpec = false;
    private OnPlusMinusListener onPlusMinusListener = null;
    private OnOpenSpecListener onOpenSpecListener = null;

    public GoodsPlusMinusView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public GoodsPlusMinusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoodsPlusMinusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_goods_plus_minus, this, true);
        ButterKnife.bind(this, this);

        plus.setOnClickListener(v -> {
            if (onPlusMinusListener == null) return;
            setNum(onPlusMinusListener.plusMinus(1));
        });
        minus.setOnClickListener(v -> {
            if (onPlusMinusListener == null) return;
            setNum(onPlusMinusListener.plusMinus(-1));
        });
        multiSpecL.setOnClickListener(v -> {
            if (onOpenSpecListener==null) return;
            onOpenSpecListener.openSpec();
        });
    }

    public GoodsPlusMinusView setIsMultiSpec(boolean isMultiSpec) {
        this.isMultiSpec = isMultiSpec;
        return this;
    }

    public void setNum(int num) {
        plusMinusL.setVisibility(isMultiSpec ? GONE : VISIBLE);
        multiSpecL.setVisibility(isMultiSpec ? VISIBLE : GONE);

        if (isMultiSpec) {
            multiSpecNum.setVisibility(num > 0 ? VISIBLE : GONE);
            multiSpecNum.setText(String.valueOf(num));
        } else {
            minus.setVisibility(num > 0 ? VISIBLE : GONE);
            minusBar.setVisibility(num > 0 ? VISIBLE : GONE);
            this.num.setVisibility(num > 0 ? VISIBLE : GONE);
            numBar.setVisibility(num > 0 ? VISIBLE : GONE);
            this.num.setText(String.valueOf(num));
        }
    }


    public GoodsPlusMinusView setOnPlusMinusListener(OnPlusMinusListener onPlusMinusListener) {
        this.onPlusMinusListener = onPlusMinusListener;
        return this;
    }

    public GoodsPlusMinusView setOnOpenSpecListener(OnOpenSpecListener onOpenSpecListener) {
        this.onOpenSpecListener = onOpenSpecListener;
        return this;
    }

    public static interface OnPlusMinusListener {
        int plusMinus(int addNum);
    }

    public static interface OnOpenSpecListener {
        void openSpec();
    }
}
