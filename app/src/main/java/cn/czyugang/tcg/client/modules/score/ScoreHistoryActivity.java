package cn.czyugang.tcg.client.modules.score;

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
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.string.RichText;
import cn.czyugang.tcg.client.widget.SpinnerSelectView;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScoreHistoryActivity extends BaseActivity {

    @BindView(R.id.score_history)
    RecyclerView historyR;
    @BindView(R.id.score_history_filter)
    TextView filter;
    @BindView(R.id.score_history_time)
    TextView time;
    @BindView(R.id.score_history_filterL)
    SpinnerSelectView filterL;
    @BindView(R.id.score_history_timeL)
    SpinnerSelectView timeL;
    private List<ScoreHistory> historyList = new ArrayList<>();
    private ScoreHistoryAdapter adapter;

    public static void startScoreHistoryActivity() {
        Intent intent = new Intent(getTopActivity(), ScoreHistoryActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);
        ButterKnife.bind(this);

        for (int i = 0; i < 20; i++) {
            if (i == 0) historyList.add(new ScoreHistory("本月"));
            if (i == 6) historyList.add(new ScoreHistory("11月"));
            if (i == 11) historyList.add(new ScoreHistory("10月"));
            historyList.add(new ScoreHistory());
        }

        adapter = new ScoreHistoryAdapter(historyList, this);
        historyR.setLayoutManager(new LinearLayoutManager(this));
        historyR.setAdapter(adapter);

        filterL.add("所有记录", "获取记录", "使用记录")
                .setOnSelectItemListener(text -> {
                    filter.setText(text);
                })
                .build();
        timeL.add("本月","11月","10月")
                .setOnSelectItemListener(text -> {
                    time.setText(text);
                })
                .build();
        filterL.select("所有记录");
        timeL.select("本月");
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.score_history_filter)
    public void onFilter() {
        filterL.setVisibility(filterL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        timeL.setVisibility(View.GONE);
    }

    @OnClick(R.id.score_history_time)
    public void onTime() {
        filterL.setVisibility(View.GONE);
        timeL.setVisibility(timeL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }


    private static class ScoreHistoryAdapter extends RecyclerView.Adapter<ScoreHistoryAdapter.Holder> {
        private List<ScoreHistory> list;
        private Activity activity;

        public ScoreHistoryAdapter(List<ScoreHistory> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ScoreHistory data = list.get(position);
            if (data.isTitle) {
                holder.title.setText(data.title);
                return;
            }

            holder.time.setText(data.getTime());
            holder.name.setText(RichText.newRichText(data.name)
                    .append(data.tip.equals("") ? "" : "\n")
                    .appendSpColorRes(data.tip, R.dimen.sp_12, R.color.text_gray)
                    .build());
            holder.score.setText(data.getScoreStr());
            holder.score.setTextColor(data.isValid ?
                    ResUtil.getColor(R.color.main_red) : ResUtil.getColor(R.color.text_gray));
            holder.invalid.setVisibility(data.isValid ? View.GONE : View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).isTitle ? R.layout.itme_score_history_title : R.layout.item_score_history;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView title;
            TextView time;
            TextView name;
            TextView score;
            View invalid;

            public Holder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.item_title);
                time = itemView.findViewById(R.id.item_time);
                name = itemView.findViewById(R.id.item_name);
                score = itemView.findViewById(R.id.item_score);
                invalid = itemView.findViewById(R.id.item_invalid);
            }
        }
    }

    private class ScoreHistory {
        private long getTime = 0;
        public String name = "每日签到";
        public String tip = "订单XXXXXXXXXXXXXXX";
        public int num = 11;
        public boolean isValid = true;

        public boolean isTitle = false;
        public String title = "本月";

        public ScoreHistory() {
        }

        public ScoreHistory(String title) {
            isTitle = true;
            this.title = title;
        }

        public String getTime() {
            return "今天";
        }

        public String getScoreStr() {
            return "+" + num;
        }
    }
}
