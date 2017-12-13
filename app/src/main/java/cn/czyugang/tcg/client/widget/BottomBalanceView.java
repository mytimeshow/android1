package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.entity.TrolleyStore;

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

    private TrolleyStore trolleyStore=null;

    public ImageView trolleyImg;
    private TextView num;
    private TextView price;
    private TextView commit;
    private TextView rest;

    private void init(@NonNull Context context){
        LayoutInflater.from(context).inflate(R.layout.view_bottom_balance,this,true);
        trolleyImg=findViewById(R.id.view_balance_trolley_img);
        num=findViewById(R.id.view_balance_num);
        price=findViewById(R.id.view_balance_price);
        commit=findViewById(R.id.view_balance_commit);
        rest=findViewById(R.id.view_balance_rest);
        setOnClickListener(v -> {});
    }

    public void setTrolleyStore(TrolleyStore trolleyStore){
        this.trolleyStore=trolleyStore;
    }

    public void refresh(){

    }
}
