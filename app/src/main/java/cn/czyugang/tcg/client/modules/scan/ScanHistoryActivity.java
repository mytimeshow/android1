package cn.czyugang.tcg.client.modules.scan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScanHistoryActivity extends BaseActivity {
    @BindView(R.id.scan_history)
    RecyclerView historyR;
    private List<ScanHistory> historyList = new ArrayList<>();
    private ScanHistoryAdapter adapter;

    public static void startScanHistoryActivity() {
        Intent intent = new Intent(getTopActivity(), ScanHistoryActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_history);
        ButterKnife.bind(this);

        for (int i = 0; i < 10; i++) {
            historyList.add(new ScanHistory());
        }

        adapter = new ScanHistoryAdapter(historyList, this);
        historyR.setLayoutManager(new LinearLayoutManager(this));
        historyR.setAdapter(adapter);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    private static class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.Holder> {
        private List<ScanHistory> list;
        private Activity activity;

        public ScanHistoryAdapter(List<ScanHistory> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_scan_history, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ScanHistory data = list.get(position);
            holder.type.setText(data.type);
            holder.content.setText(data.content);
            holder.time.setText(data.getTimeStr());
            holder.typeImg.setImageResource(data.type.contains("条形码") ?
                    R.drawable.icon_dbarcode_light_gray : R.drawable.icon_qrcode_light_gray);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView type;
            TextView content;
            TextView time;
            ImageView typeImg;

            public Holder(View itemView) {
                super(itemView);
                type = itemView.findViewById(R.id.item_type);
                content = itemView.findViewById(R.id.item_content);
                time = itemView.findViewById(R.id.item_time);
                typeImg = itemView.findViewById(R.id.item_scan_img);
            }
        }
    }

    public static class ScanHistory {
        public String type = "链接";
        public String content = "http://www.baidu.com";
        private long time = 0;

        public String getTimeStr() {
            return "2017-8-14";
        }
    }
}
