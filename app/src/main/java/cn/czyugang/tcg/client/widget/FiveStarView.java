package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.czyugang.tcg.client.R;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class FiveStarView extends LinearLayout {

    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private TextView textView;

    private int starResId=R.drawable.icon_yellowstar;

    public FiveStarView(Context context) {
        super(context);
        init(context);
    }

    public FiveStarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        parse(context,attrs);
    }

    public FiveStarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        parse(context,attrs);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_five_star, this, true);
        star1 = findViewById(R.id.view_five_star_1);
        star2 = findViewById(R.id.view_five_star_2);
        star3 = findViewById(R.id.view_five_star_3);
        star4 = findViewById(R.id.view_five_star_4);
        star5 = findViewById(R.id.view_five_star_5);
        textView = findViewById(R.id.view_five_star_text);
    }

    private void parse(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FiveStarView);
        int score = typedArray.getInt(R.styleable.FiveStarView_five_star_score, 0);
        String str = typedArray.getString(R.styleable.FiveStarView_five_star_text);
        setScore(score);
        if (str != null && !str.equals("")) setText(str);
        typedArray.recycle();
    }

    public void setScore(double score){
        setScore((float) score);
    }

    public void setScore(float score) {
        if (score <= 0) return;
        if (score >= 1) star1.setImageResource(starResId);
        if (score >= 2) star2.setImageResource(starResId);
        if (score >= 3) star3.setImageResource(starResId);
        if (score >= 4) star4.setImageResource(starResId);
        if (score >= 5) star5.setImageResource(starResId);
        setText(String.format("%.1f 分", (float)score));
    }

    public void setText(String text) {
        if (text == null) text = "";
        textView.setText(text);
    }

    public FiveStarView setStarResId(int starResId) {
        this.starResId = starResId;
        return this;
    }
}
