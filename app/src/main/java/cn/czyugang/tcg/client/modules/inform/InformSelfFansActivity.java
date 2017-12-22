package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserFansFollow;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformSelfFansActivity extends BaseActivity {

    @BindView(R.id.inform_self_follow_fans_list)
    RecyclerView selfFasList;
    @BindView(R.id.inform_self_follow_fans_title_name)
    TextView tvTitleName;

    InformSelfFollowActivity.SelfFollowFansListAdapter SelfFollowFansListAdapter;
    List<UserFansFollow> informColumns=new ArrayList<UserFansFollow>();

    public static void startInformSelfFansActivity(String title,String userId){
        Intent intent=new Intent(getTopActivity(),InformSelfFansActivity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("titleName",title);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_self_follow_fans);
        ButterKnife.bind(this);

        tvTitleName.setText(getIntent().getStringExtra("titleName"));


        SelfFollowFansListAdapter=new InformSelfFollowActivity.SelfFollowFansListAdapter(informColumns,this);
        selfFasList.setLayoutManager(new LinearLayoutManager(this));
        selfFasList.setAdapter(SelfFollowFansListAdapter);
        InformApi.userFansList(getIntent().getStringExtra("userId"),1).subscribe(new NetObserver<Response<List<UserFansFollow>>>() {
            @Override
            public void onNext(Response<List<UserFansFollow>> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)){
                    informColumns.clear();
                    informColumns.addAll(response.data);
                    SelfFollowFansListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
