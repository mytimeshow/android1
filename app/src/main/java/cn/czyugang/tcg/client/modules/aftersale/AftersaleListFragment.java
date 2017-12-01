package cn.czyugang.tcg.client.modules.aftersale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Aftersale;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;

/**
 * @author ruiaa
 * @date 2017/11/30
 */

public class AftersaleListFragment extends BaseFragment {

    public static final int AFTER_SALE_TYPE_ALL = 1;
    public static final int AFTER_SALE_TYPE_DEAL = 2;
    public static final int AFTER_SALE_TYPE_FINISH = 3;

    @BindView(R.id.aftersale_list)
    RecyclerView aftersaleR;
    Unbinder unbinder;

    private int type = AFTER_SALE_TYPE_ALL;
    private List<Aftersale> aftersaleList = new ArrayList<>();
    private AftersaleAdapter adapter;

    public static AftersaleListFragment newInstance(int aftersaleType) {
        AftersaleListFragment fragment = new AftersaleListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", aftersaleType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) type = getArguments().getInt("type", AFTER_SALE_TYPE_ALL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_aftersale_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        aftersaleList.add(new Aftersale());
        aftersaleList.add(new Aftersale());


        adapter = new AftersaleAdapter(aftersaleList, getActivity());
        aftersaleR.setLayoutManager(new LinearLayoutManager(getActivity()));
        aftersaleR.setAdapter(adapter);
        aftersaleR.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
                ResUtil.getDimenInPx(R.dimen.dp_10), ResUtil.getColor(R.color.bg)).setDrawBottom(true));

        return rootView;
    }

    @Override
    public String getLabel() {
        if (getArguments() != null) type = getArguments().getInt("type", AFTER_SALE_TYPE_ALL);
        switch (type) {
            case AFTER_SALE_TYPE_ALL:
                return "全部";
            case AFTER_SALE_TYPE_DEAL:
                return "处理中";
            default:
                return "已完结";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class AftersaleAdapter extends RecyclerView.Adapter<AftersaleAdapter.Holder> {
        private List<Aftersale> list;
        private Activity activity;

        public AftersaleAdapter(List<Aftersale> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_aftersale, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Aftersale data = list.get(position);
            holder.itemView.setOnClickListener(v -> AftersaleDetailActivity.startAftersaleDetailActivity());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
