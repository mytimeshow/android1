package cn.czyugang.tcg.client.modules.errand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Errand;

/**
 * @author ruiaa
 * @date 2017/12/18
 * <p>
 * 我的跑腿列表
 */

public class ErrandListActivity extends BaseActivity {

    @BindView(R.id.errand_list)
    RecyclerView errandR;

    private ErrandAdapter adapter;
    private List<Errand> errandList = new ArrayList<>();

    public static void startErrandListActivity() {
        Intent intent = new Intent(getTopActivity(), ErrandListActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_list);
        ButterKnife.bind(this);

        errandList.add(new Errand());
        errandList.add(new Errand());
        errandList.add(new Errand());
        errandList.add(new Errand());

        adapter = new ErrandAdapter(errandList, this);
        errandR.setLayoutManager(new LinearLayoutManager(this));
        errandR.setAdapter(adapter);
    }

    private static class ErrandAdapter extends RecyclerView.Adapter<ErrandAdapter.Holder> {
        private List<Errand> list;
        private Activity activity;

        public ErrandAdapter(List<Errand> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_errand_order, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Errand data = list.get(position);

            holder.itemView.setOnClickListener(v -> {
                ErrandDetailActivity.startErrandDetailActivity();
            });
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
