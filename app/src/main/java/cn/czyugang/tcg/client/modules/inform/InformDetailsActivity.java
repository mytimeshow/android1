package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.api.RecordApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.app.AppUtil;

/**
 * Created by Administrator on 2017/12/12.
 */

public class InformDetailsActivity extends BaseActivity {

    private String id;

    public static void startInformDetailsActivity(String id) {
        Intent intent = new Intent(getTopActivity(), InformDetailsActivity.class);
        intent.putExtra("id", id);
        getTopActivity().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id=getIntent().getStringExtra("id");

        setContentView(R.layout.activity_inform_details);
        ButterKnife.bind(this);

        getInformDetail();
    }

    private void getInformDetail(){
        InformApi.getInformDetail(id).subscribe(new NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                super.onNext(response);
            }
        });
    }

    @OnClick(R.id.inform_detail_shoucang)
    public void onCollect(){
        RecordApi.collect("INFO",id).subscribe(new NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)){
                    AppUtil.toast("成功收藏");
                }
            }
        });
    }
}
