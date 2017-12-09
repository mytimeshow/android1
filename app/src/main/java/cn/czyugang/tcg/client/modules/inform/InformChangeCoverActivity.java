package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/9.
 */

public class InformChangeCoverActivity extends BaseActivity {

    public static void startChangeCoverActivity( ){
        Intent intent=new Intent(getTopActivity(),InformChangeCoverActivity.class);
        getTopActivity().startActivity(intent);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_change_cover);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
}
