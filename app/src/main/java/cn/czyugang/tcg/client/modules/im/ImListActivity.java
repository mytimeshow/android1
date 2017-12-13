package cn.czyugang.tcg.client.modules.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/11
 *
 *  im主页 通知 + 聊天 列表
 */

public class ImListActivity extends BaseActivity {
    public static void startImListActivity( ){
        Intent intent=new Intent(getTopActivity(),ImListActivity.class);
        getTopActivity().startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_list);
    }
}
