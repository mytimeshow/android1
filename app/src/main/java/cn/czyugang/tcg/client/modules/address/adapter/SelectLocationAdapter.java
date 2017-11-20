package cn.czyugang.tcg.client.modules.address.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;

/**
 * Created by wuzihong on 2017/10/31.
 */

public class SelectLocationAdapter extends BaseRecyclerAdapter<PoiItem> {
    public SelectLocationAdapter(Context context) {
        super(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_location, parent, false));
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int position) {
        PoiItem item = dataList.get(position);
        ((ViewHolder) holder).tv_name.setText(item.getTitle());
        ((ViewHolder) holder).tv_location.setText(item.getProvinceName() + item.getCityName() + item.getAdName() + item.getSnippet());
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_location)
        TextView tv_location;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
