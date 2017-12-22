package cn.czyugang.tcg.client.modules.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/21
 *
 *  消息免打扰
 */

public class ImSettingNoticeActivity extends BaseActivity {
    public static void startImSettingNoticeActivity( ){
        Intent intent=new Intent(getTopActivity(),ImSettingNoticeActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_setting_notice);
    }
}
