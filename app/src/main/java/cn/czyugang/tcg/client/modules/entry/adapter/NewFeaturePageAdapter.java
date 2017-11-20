package cn.czyugang.tcg.client.modules.entry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BasePageAdapter;

/**
 * Created by wuzihong on 2017/9/12.
 * 新特性适配器
 */

public class NewFeaturePageAdapter extends BasePageAdapter {

    public NewFeaturePageAdapter(Context context) {
        super(context);
    }

    @Override
    protected BasePageAdapter.ViewHolder onCreate(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pager_new_feature, parent, false));
    }

    @Override
    protected void onBind(BasePageAdapter.ViewHolder holder, int position) {
        ((ViewHolder) holder).iv_new_feature.setImageResource((Integer) dataList.get(position));
        if (position == getCount() - 1) {
            ((ViewHolder) holder).tv_entry.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolder) holder).tv_entry.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends BasePageAdapter.ViewHolder {
        @BindView(R.id.iv_new_feature)
        ImageView iv_new_feature;
        @BindView(R.id.tv_entry)
        TextView tv_entry;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_entry.setOnClickListener(onClickListenerHelper);
        }
    }
}
