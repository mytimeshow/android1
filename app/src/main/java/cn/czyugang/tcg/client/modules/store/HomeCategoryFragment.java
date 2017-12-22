package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/12/21
 */

public class HomeCategoryFragment extends BaseFragment {

    @BindView(R.id.home_category_class)
    RecyclerView classR;
    @BindView(R.id.home_category_select_class)
    TextView selectClass;
    @BindView(R.id.home_category_class_detail)
    RecyclerView classDetailR;
    Unbinder unbinder;

    private int type;
    private ClassAdapter classAdapter;
    private ClassDetailAdapter classDetailAdapter;
    private List<String> classList = new ArrayList<>();
    private List<String> classDetailList = new ArrayList<>();

    public static HomeCategoryFragment newInstance(int type) {
        HomeCategoryFragment fragment = new HomeCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_category, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        classList.addAll(Arrays.asList("1.", "2.", "3.", "4.", "5.", "6.", "7.", "8.", "9.", "10."));
        classDetailList.addAll(Arrays.asList(".", ".", ".", ".", ".", ".", ".", ".", ".", "."));

        classAdapter = new ClassAdapter(classList, getActivity());
        classR.setLayoutManager(new LinearLayoutManager(getActivity()));
        classR.setAdapter(classAdapter);

        classDetailAdapter = new ClassDetailAdapter(classDetailList, getActivity());
        classDetailR.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        classDetailR.setAdapter(classDetailAdapter);


        return rootView;
    }

    @Override
    public String getLabel() {
        type = getArguments().getInt("type");
        if (type == Store.STORE_TYPE_GOODS) return "商超";
        return "外卖";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void onSelectClassTile(String classStr) {
        selectClass.setText(classStr);
    }

    private void onSelectClassDetail() {

    }

    private class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.Holder> {
        private List<String> list;
        private Activity activity;
        private String selected = "";

        public ClassAdapter(List<String> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_home_category_title, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String data = list.get(position);

            if (selected.equals(data)){
                holder.name.setTextColor(ResUtil.getColor(R.color.main_red));
                holder.bar.setVisibility(View.VISIBLE);
            }else {
                holder.name.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
                holder.bar.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> {
                selected=data;
                notifyDataSetChanged();
                onSelectClassTile(data);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView name;
            View bar;
            public Holder(View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.item_name);
                bar=itemView.findViewById(R.id.item_bar);
            }
        }
    }

    private class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.Holder> {
        private List<String> list;
        private Activity activity;

        public ClassDetailAdapter(List<String> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_home_category, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String data = list.get(position);

            holder.itemView.setOnClickListener(v -> onSelectClassDetail());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
