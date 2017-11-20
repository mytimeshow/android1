package cn.czyugang.tcg.client.modules.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by wuzihong on 2017/10/7.
 * 关于我们
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_share, R.id.tv_help, R.id.tv_clause, R.id.tv_feedback,
            R.id.tv_cooperation, R.id.tv_update})
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
                break;
            case R.id.tv_feedback:
                break;
            case R.id.tv_cooperation:
                break;
            case R.id.tv_update:
                break;
        }
    }
}
