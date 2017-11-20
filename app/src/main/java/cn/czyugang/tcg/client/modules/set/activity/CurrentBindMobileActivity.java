package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
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
 * Created by wuzihong on 2017/10/5.
 * 当前绑定的手机
 */

public class CurrentBindMobileActivity extends BaseActivity {
    public static final String KEY_MOBILE = "mobile";
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;

    private String mMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_bind_mobile);
        ButterKnife.bind(this);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        initView();
    }

    private void initView() {
        tv_mobile.setText(mMobile);
    }

    @OnClick({R.id.iv_back, R.id.tv_change_mobile})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_change_mobile: {
                Intent intent = new Intent(context, CurrentMobileCodeActivity.class);
                intent.putExtra(CurrentMobileCodeActivity.KEY_MOBILE, mMobile);
                startActivity(intent);
                finish();
                break;
            }
        }
    }
}
