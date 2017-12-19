package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/19.
 */

public class InformSearchAcitivity extends BaseActivity {

    public static  final int SEARCH_TYPE_ARTICLE =0;
    public static  final int SEARCH_TYPE_LABLE = 1;

    public static void startInformSearchAcitivity(String text,int type){
        Intent intent=new Intent(getTopActivity(),InformSearchAcitivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
