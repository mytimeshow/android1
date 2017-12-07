package cn.czyugang.tcg.client.modules.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/5
 */

public class ScanStoreActivity extends BaseActivity {
    public static void startScanStoreActivity( ){
        Intent intent=new Intent(getTopActivity(),ScanStoreActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_store);
    }
}
