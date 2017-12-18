package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/12.
 */

public class InformDetailsActivity extends BaseActivity {



    public static void startInformDetailsActivity( ){
        Intent intent=new Intent(getTopActivity(),InformDetailsActivity.class);
        getTopActivity().startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_details);
        ButterKnife.bind(this);
    }

}
