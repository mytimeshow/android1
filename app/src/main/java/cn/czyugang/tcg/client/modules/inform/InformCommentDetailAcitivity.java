package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by Administrator on 2018/1/11.
 */

public class InformCommentDetailAcitivity extends BaseActivity {

    public static void startInformCommentDetailAcitivity( ){
        Intent intent=new Intent(getTopActivity(),InformCommentDetailAcitivity.class);
        getTopActivity().startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_comment_detail);
    }
}
