package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.R;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class CalculateOrderView extends LinearLayout {
    public CalculateOrderView(Context context) {
        super(context);

    }

    public CalculateOrderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public CalculateOrderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    private List<Item> items = new ArrayList<>();
    private boolean foldable = true;

    public void clear() {
        items.clear();
        removeAllViews();
    }

    public CalculateOrderView addActivityItem(String type, String name) {
        items.add(new Item(type, name, null));
        return this;
    }

    public CalculateOrderView addItem(String name, String price) {
        items.add(new Item(null, name, price));
        return this;
    }

    public CalculateOrderView addItem(String type, String name, String price) {
        items.add(new Item(type, name, price));
        return this;
    }

    public CalculateOrderView addItem(String name, String price, View.OnClickListener listener) {
        items.add(new Item(name, listener, price));
        return this;
    }


    public void hideOrShowMore() {
        int size = getChildCount();
        if (size <= 2) return;
        int show = getChildAt(2).getVisibility() == GONE ? View.VISIBLE : View.GONE;
        for (int i = 2; i < size; i++) {
            getChildAt(2).setVisibility(show);
        }
    }

    public void build() {
        for (int i = 0, size = items.size(); i < size; i++) {
            Item item = items.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_calculate_order, this, false);
            //LogRui.i("build####",view+"        "+getChildCount());
            ActivityTextView nameT = view.findViewById(R.id.view_calculate_order_name);
            TextView priceT = view.findViewById(R.id.view_calculate_order_price);
            ImageView img = view.findViewById(R.id.view_calculate_order_img);
            nameT.setText(R.color.text_dark_gray, R.dimen.sp_14);
            nameT.setText(item.name);
            if (item.type != null) nameT.setType(item.type);
            if (item.listener != null) {
                img.setImageResource(item.icon);
                img.setOnClickListener(item.listener);
            }
            priceT.setText(item.price);

            if (i == 1 && foldable) {
                ImageView imageView = (ImageView) LayoutInflater.from(getContext())
                        .inflate(R.layout.view_icon_right, (ViewGroup) view, false);
                imageView.setImageResource(R.drawable.icon_arrow_down);
                imageView.setOnClickListener(v -> hideOrShowMore());
                ((ViewGroup) view).addView(imageView);
            }
            if (i > 1 && foldable) {
                view.setVisibility(GONE);
            }

            addView(view);
        }
    }

    private static class Item {
        String type = null;
        String name = null;
        int icon = R.drawable.ic_tip_grey_white;
        String price = "";
        View.OnClickListener listener = null;

        private Item(String type, String name, String price) {
            this.type = type;
            this.name = name;
            this.price = price;
        }

        private Item(String name, View.OnClickListener listener, String price) {
            this.name = name;
            this.listener = listener;
            this.price = price;
        }

        private Item(String name, int icon, View.OnClickListener listener, String price) {
            this.name = name;
            this.icon = icon;
            this.listener = listener;
            this.price = price;
        }
    }
}
