package cn.czyugang.tcg.client.modules.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScanInputActivity extends BaseActivity {
    @BindView(R.id.scan_input)
    EditText input;

    public static void startScanInputActivity() {
        Intent intent = new Intent(getTopActivity(), ScanInputActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_input);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.scan_input_confirm)
    public void onConfirm(){
        if (input.getText().length()==0) {
            showToast("请输入二维码/条形码");
            return;
        }
        ScanResultHandler.deal(input.getText().toString(),this);
    }

    @OnClick(R.id.scan_check_scan)
    public void onCheckScan(){
        ScanActivity.startScanActivity();
    }
}
