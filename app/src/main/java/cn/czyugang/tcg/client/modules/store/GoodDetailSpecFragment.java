package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.os.Bundle;
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
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;

/**
 * @author ruiaa
 * @date 2017/11/27
 */

public class GoodDetailSpecFragment extends BaseFragment {

    @BindView(R.id.good_detail_spec)
    RecyclerView specR;
    Unbinder unbinder;

    private SpecAdapter adapter;
    private List<Item> list = new ArrayList<>();

    public static GoodDetailSpecFragment newInstance() {
        GoodDetailSpecFragment fragment = new GoodDetailSpecFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good_detail_spec, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        list.add(new Item("商品编号", "213131"));
        list.add(new Item("颜色", "黑色"));
        list.add(new Item("商品编号", "213131"));
        list.add(new Item("颜色", "黑色"));
        list.add(new Item("商品编号", "213131"));
        list.add(new Item("颜色", "黑色"));
        list.add(new Item("商品编号", "213131"));
        list.add(new Item("颜色", "黑色"));

        adapter = new SpecAdapter(list, getActivity());
        specR.setLayoutManager(new LinearLayoutManager(getActivity()));
        specR.setAdapter(adapter);
        specR.addItemDecoration(
                new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
                        ResUtil.getDimenInPx(R.dimen.dp_0_5), ResUtil.getColor(R.color.grey_500))
                        .setDrawBottom(true).setDrawTop(true)
        );

        return rootView;
    }

    @Override
    public String getLabel() {
        return "商品规格";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class SpecAdapter extends RecyclerView.Adapter<SpecAdapter.Holder> {
        private List<Item> list;
        private Activity activity;

        public SpecAdapter(List<Item> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_good_spec, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Item data = list.get(position);
            holder.name.setText(data.name);
            holder.content.setText(data.content);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView name;
            TextView content;
            public Holder(View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.item_name);
                content=itemView.findViewById(R.id.item_content);
            }
        }
    }

    private static class Item {
        String name = "";
        String content = "";

        public Item(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }
}
