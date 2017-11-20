package cn.czyugang.tcg.client.modules.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by wuzihong on 2017/10/7.
 * 通用
 */

public class GeneralActivity extends BaseActivity {
    @BindView(R.id.tv_cache)
    TextView tv_cache;
    @BindView(R.id.switch_night_mode)
    SwitchCompat switch_night_mode;
    @BindView(R.id.switch_auto_play)
    SwitchCompat switch_auto_play;
    @BindView(R.id.switch_download_image)
    SwitchCompat switch_download_image;
    @BindView(R.id.switch_follow_push)
    SwitchCompat switch_follow_push;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_toolbox, R.id.fl_clear_cache, R.id.tv_font_size})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_toolbox:
                break;
            case R.id.fl_clear_cache:
                break;
            case R.id.tv_font_size:
                break;
        }
    }
}
