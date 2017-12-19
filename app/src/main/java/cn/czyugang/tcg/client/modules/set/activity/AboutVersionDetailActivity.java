package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/18
 */

public class AboutVersionDetailActivity extends BaseActivity {
    public static void startAboutVersionDetailActivity( ){
        Intent intent=new Intent(getTopActivity(),AboutVersionDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_version_detail);
    }
}
