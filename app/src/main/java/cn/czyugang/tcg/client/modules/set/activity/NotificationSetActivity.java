package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.Config;

/**
 * Created by wuzihong on 2017/10/4.
 * 消息提醒
 */

public class NotificationSetActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.switch_notification)
    SwitchCompat switch_notification;
    @BindView(R.id.switch_sound)
    SwitchCompat switch_sound;
    @BindView(R.id.switch_vibrate)
    SwitchCompat switch_vibrate;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_set);
        ButterKnife.bind(this);
        mSharedPreferences = context.getSharedPreferences(Config.CONFIG_NAME, Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        switch_notification.setChecked(mSharedPreferences.getBoolean(Config.CONFIG_KEY_NOTIFICATION_REMIND, true));
        switch_notification.setOnCheckedChangeListener(this);
        switch_sound.setChecked(mSharedPreferences.getBoolean(Config.CONFIG_KEY_SOUND_REMIND, true));
        switch_sound.setOnCheckedChangeListener(this);
        switch_vibrate.setChecked(mSharedPreferences.getBoolean(Config.CONFIG_KEY_VIBRATE_REMIND, true));
        switch_vibrate.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_notification:
                mSharedPreferences.edit().putBoolean(Config.CONFIG_KEY_NOTIFICATION_REMIND, isChecked).apply();
                break;
            case R.id.switch_sound:
                mSharedPreferences.edit().putBoolean(Config.CONFIG_KEY_SOUND_REMIND, isChecked).apply();
                break;
            case R.id.switch_vibrate:
                mSharedPreferences.edit().putBoolean(Config.CONFIG_KEY_VIBRATE_REMIND, isChecked).apply();
                break;
        }
    }
}
