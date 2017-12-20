package cn.czyugang.tcg.client.modules.errand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class ErrandBookActivity extends BaseActivity {
    public static final int ERRAND_TYPE_BUY=1;
    public static final int ERRAND_TYPE_SEND=2;
    public static final int ERRAND_TYPE_TAKE=3;

    public static void startErrandBookActivity(int type){
        Intent intent=new Intent(getTopActivity(),ErrandBookActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_book);
    }
}