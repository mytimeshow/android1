package cn.czyugang.tcg.client.modules.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.BuildConfig;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by wuzihong on 2017/10/7.
 * 关于我们
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.tv_update_version)
    TextView tv_update_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        String versionName=BuildConfig.VERSION_NAME;
        String[] versionNameTypes=versionName.split("\\.");
        LogRui.i("onCreate####",versionNameTypes[0]);
        if (versionNameTypes.length>=3){
            versionName=versionNameTypes[0]+"."+versionNameTypes[1]+"."+versionNameTypes[2];
        }
        tv_version.setText("V"+versionName);
    }

    private void onVersionIntro() {
        AboutVersionIntroActivity.startAboutVersionIntroActivity();
    }

    private void onFeedBack() {

    }

    private void onCooperation() {
        MyDialog.phoneDialog(this,"0768-2500123","拨打热线  0768-2500123");
    }

    private void onClause() {

    }

    private void onCheckUpdate() {

    }


    @OnClick({R.id.iv_back, R.id.tv_share, R.id.tv_help, R.id.tv_clause, R.id.tv_feedback,
            R.id.tv_cooperation, R.id.tv_update, R.id.tv_version_intro})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_share:
                break;
            case R.id.tv_help:
                break;
            case R.id.tv_clause:
                onClause();
                break;
            case R.id.tv_feedback:
                onFeedBack();
                break;
            case R.id.tv_cooperation:
                onCooperation();
                break;
            case R.id.tv_update:
                onCheckUpdate();
                break;
            case R.id.tv_version_intro:
                onVersionIntro();
                break;
        }
    }
}
