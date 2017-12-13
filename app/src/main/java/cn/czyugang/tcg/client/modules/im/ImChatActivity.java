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
 *  聊天
 */

public class ImChatActivity extends BaseActivity {
    public static void startImChatActivity( ){
        Intent intent=new Intent(getTopActivity(),ImChatActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_chat);
    }
}
