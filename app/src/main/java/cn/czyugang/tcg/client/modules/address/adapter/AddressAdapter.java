package cn.czyugang.tcg.client.modules.address.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.entity.Address;

/**
 * Created by wuzihong on 2017/10/9.
 * 地址Adapter
 */

public class AddressAdapter extends BaseRecyclerAdapter<Address> {
    public AddressAdapter(Context context) {
        super(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int position) {
        Address address = dataList.get(position);
        ((ViewHolder) holder).tv_name.setText(String.format("%s（%s） %s", address.getLinkman(), UserApi.MAN.equals(address.getSex()) ? "先生" : "女士", address.getPhone()));
        ((ViewHolder) holder).tv_tag.setText(address.getTag());
        ((ViewHolder) holder).tv_address.setText(address.getAddress());
        ((ViewHolder) holder).tv_default_address.setCompoundDrawablesWithIntrinsicBounds(("YES".equals(address.getDefaultAddress()) ? R.drawable.ic_checkbox_check : R.drawable.ic_checkbox_normal), 0, 0, 0);
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_tag)
        TextView tv_tag;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_default_address)
        TextView tv_default_address;
        @BindView(R.id.tv_edit)
        TextView tv_edit;
        @BindView(R.id.tv_delete)
        TextView tv_delete;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_edit.setOnClickListener(onItemClickListenerHelper);
            tv_delete.setOnClickListener(onItemClickListenerHelper);
            tv_default_address.setOnClickListener(onItemClickListenerHelper);
        }
    }
}
