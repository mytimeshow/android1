package cn.czyugang.tcg.client.modules.scan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScanActivity extends BaseActivity {

    public static void startScanActivity() {
        Intent intent = new Intent(getTopActivity(), ScanActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        //执行扫码Fragment的初始化操作
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.view_scan);
        //二维码解析回调函数
        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                onScanSuccess(result);
            }

            @Override
            public void onAnalyzeFailed() {
                onScanFail();
            }
        });
        //替换扫描控件
        getSupportFragmentManager().beginTransaction().replace(R.id.scan_scaner, captureFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        parseImg(requestCode, resultCode, data);
    }

    @OnClick(R.id.scan_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.scan_gallery)
    public void onGallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_white_style)
                .selectionMode(PictureConfig.SINGLE)
                .glideOverride(160, 160)
                .compress(true)
                .minimumCompressSize(100)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private boolean parseImg(int requestCode, int resultCode, Intent data) {
        if (requestCode != PictureConfig.CHOOSE_REQUEST) return false;
        if (resultCode != Activity.RESULT_OK) return true;
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (selectList.size() > 0) {
            try {
                CodeUtils.analyzeBitmap(selectList.get(0).getCompressPath(), new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        onScanSuccess(result);
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        onScanFail();
                    }
                });
            } catch (Exception e) {
                LogRui.e("onActivityResult####", e);
            }
        }
        return true;
    }

    private boolean isOpeningFlash = false;

    @OnClick(R.id.scan_flash)
    public void onFlash() {
        isOpeningFlash = !isOpeningFlash;
        CodeUtils.isLightEnable(isOpeningFlash);
    }

    @OnClick(R.id.scan_history)
    public void onHistory() {
        ScanHistoryActivity.startScanHistoryActivity();
    }

    @OnClick(R.id.scan_input_code)
    public void onInputCode() {
        ScanInputActivity.startScanInputActivity();
    }

    private void onScanSuccess(String result) {
        ScanResultHandler.deal(result,this);
    }

    private void onScanFail() {

    }
}
