package cn.czyugang.tcg.client.modules.score;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScoreUseActivity extends BaseActivity {

    public static void startScoreUseActivity( ){
        Intent intent=new Intent(getTopActivity(),ScoreUseActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_use);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }


}
