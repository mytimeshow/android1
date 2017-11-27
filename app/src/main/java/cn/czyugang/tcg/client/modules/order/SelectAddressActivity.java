package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.AddressApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Address;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.address.activity.AddAddressActivity;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class SelectAddressActivity extends BaseActivity {

    @BindView(R.id.select_address_list)
    RecyclerView addressR;
    private List<Address> addressList = new ArrayList<>();
    private AddressAdapter adapter;

    public static void startSelectAddressActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectAddressActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);

        adapter = new AddressAdapter(addressList, this);
        addressR.setLayoutManager(new LinearLayoutManager(this));
        addressR.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        AddressApi.loadAddressList().subscribe(new NetObserver<Response<List<Address>>>() {
            @Override
            public void onNext(Response<List<Address>> response) {
                super.onNext(response);
                addressList.addAll(response.getData());
                Address.clearNotReceivedAddress(addressList);
                adapter.selectAddress = Address.findDefaultAddress(addressList);
                adapter.notifyDataSetChanged();
            }
        });
        super.onResume();
    }

    @OnClick(R.id.select_address_add)
    public void onAddressAdd(){
        AddAddressActivity.startAddAddressActivityForAdd();
    }

    static class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Holder> {
        private List<Address> list;
        private Activity activity;
        public Address selectAddress = null;

        public AddressAdapter(List<Address> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_select_address, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (selectAddress == null) selectAddress = list.get(0);
            Address data = list.get(position);
            holder.name.setText(data.getLinkman() + "   " + data.getPhone());
            holder.location.setText(data.getAddress());
            holder.isDefault.setVisibility(data.isDefaultAddress() ? View.VISIBLE : View.GONE);
            holder.edit.setOnClickListener(v -> {
                AddAddressActivity.startAddAddressActivityForEdit(data);
            });
            holder.select.setChecked(data == selectAddress);
            holder.select.setOnClickListener(v -> {
                selectAddress = data;
                notifyDataSetChanged();
            });
            holder.itemView.setOnClickListener(v -> {
                selectAddress = data;
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_select_address_default)
            View isDefault;
            @BindView(R.id.item_select_address_disable)
            View isDisable;
            @BindView(R.id.item_select_address_edit)
            View edit;
            @BindView(R.id.item_select_address_location)
            TextView location;
            @BindView(R.id.item_select_address_name)
            TextView name;
            @BindView(R.id.item_select_address_select)
            SelectButton select;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
