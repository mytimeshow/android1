package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class SpinnerSelectView extends LinearLayout {
    public SpinnerSelectView(Context context) {
        super(context);
    }

    public SpinnerSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List<String> textList = new ArrayList<>();
    private OnSelectItemListener onSelectItemListener = null;
    private TextView selectT=null;
    private View selectHook=null;


    public void add(String item) {
        textList.add(item);
    }

    public SpinnerSelectView add(String... items) {
        textList.addAll(Arrays.asList(items));
        return this;
    }

    public SpinnerSelectView setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.onSelectItemListener = onSelectItemListener;
        return this;
    }

    public void build() {
        for (String s : textList) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_select_text_hook,
                    this, false);
            TextView textView=view.findViewById(R.id.view_text);
            View hook=view.findViewById(R.id.view_hook);
            textView.setText(s);
            hook.setVisibility(GONE);
            view.setOnClickListener(v -> {
                if (selectT!=null) selectT.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
                if (selectHook!=null) selectHook.setVisibility(GONE);
                selectHook=hook;
                selectT=textView;
                selectHook.setVisibility(VISIBLE);
                selectT.setTextColor(ResUtil.getColor(R.color.main_red));
                if (onSelectItemListener!=null) onSelectItemListener.onSelect(selectT.getText().toString());
            });
            addView(view);
        }
    }

    public void select(String s){
        if (!textList.contains(s)) return;
        if (selectT!=null) selectT.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        if (selectHook!=null) selectHook.setVisibility(GONE);
        View view=getChildAt(textList.indexOf(s));
        selectT=view.findViewById(R.id.view_text);
        selectHook=view.findViewById(R.id.view_hook);
        selectHook.setVisibility(VISIBLE);
        selectT.setTextColor(ResUtil.getColor(R.color.main_red));
        if (onSelectItemListener!=null) onSelectItemListener.onSelect(selectT.getText().toString());
    }

    public static interface OnSelectItemListener {
        public void onSelect(String text);
    }
}
