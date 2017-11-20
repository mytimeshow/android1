package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by wuzihong on 2017/10/6.
 * 支付密码控件
 */

public class PayPasswordEditText extends android.support.v7.widget.AppCompatTextView implements TextWatcher {
    private final int BACKGROUND_COLOR = Color.WHITE;//背景颜色
    private final int BORDER_COLOR = 0xffcccccc;//边框颜色
    private final int DIVIDER_COLOR = 0xffe5e5e5;//分割线颜色
    private final float STROKE_WIDTH = 0.5f;//线条宽度
    private final int DOT_RADIUS = 5;//密码点半径
    private final int PASSWORD_LENGTH = 6;//密码长度

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private DisplayMetrics mDm;
    private OnEntryCompleteListener mListener;

    public PayPasswordEditText(Context context) {
        super(context);
        init();
    }

    public PayPasswordEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PayPasswordEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnEntryCompleteListener(OnEntryCompleteListener listener) {
        mListener = listener;
    }

    private void init() {
        mDm = getResources().getDisplayMetrics();
        setCursorVisible(false);
        setFocusableInTouchMode(true);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(PASSWORD_LENGTH)});
        addTextChangedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * 0.75f / PASSWORD_LENGTH);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景
        canvas.drawColor(BACKGROUND_COLOR);
        //边框
        mPaint.setColor(BORDER_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH * mDm.density);
        canvas.drawRect(STROKE_WIDTH / 2 * mDm.density, STROKE_WIDTH / 2 * mDm.density, getWidth(), getHeight(), mPaint);
        //分割线
        mPaint.setColor(DIVIDER_COLOR);
        int w = getWidth() / PASSWORD_LENGTH;
        for (int i = 0; i < PASSWORD_LENGTH - 1; i++) {
            canvas.drawLine((i + 1) * w, 0, (i + 1) * w, getHeight(), mPaint);
        }
        //密码
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        int x = w / 2;
        int y = getHeight() / 2;
        for (int i = 0; i < length(); i++) {
            canvas.drawCircle(i * w + x, y, DOT_RADIUS * mDm.density, mPaint);
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //每次选择游标都设置在最后
        Selection.setSelection((Editable) getText(), length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == PASSWORD_LENGTH && mListener != null) {
            mListener.onEntryComplete(s.toString());
        }
    }

    public interface OnEntryCompleteListener {
        void onEntryComplete(String password);
    }
}
