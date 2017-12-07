package cn.czyugang.tcg.client.modules.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScanResultActivity extends BaseActivity {
    @BindView(R.id.scan_result_num)
    TextView resultNum;
    @BindView(R.id.scan_result_list)
    RecyclerView resultR;
    @BindView(R.id.scan_result_empty)
    FrameLayout empty;

    public static void startScanResultActivity(String result) {
        Intent intent = new Intent(getTopActivity(), ScanResultActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);

        empty.setVisibility(View.VISIBLE);
    }
}
