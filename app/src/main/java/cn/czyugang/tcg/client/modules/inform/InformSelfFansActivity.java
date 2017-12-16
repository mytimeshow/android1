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
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Inform;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformSelfFansActivity extends BaseActivity {

    @BindView(R.id.inform_self_follow_fans_list)
    RecyclerView selfFasList;
    @BindView(R.id.inform_self_follow_fans_title_name)
    TextView tvTitleName;

    static String titleName;

    public static void startInformSelfFansActivity(String title ){
        Intent intent=new Intent(getTopActivity(),InformSelfFansActivity.class);
        getTopActivity().startActivity(intent);
        titleName=title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_self_follow_fans);
        ButterKnife.bind(this);

        tvTitleName.setText(titleName);
        List<Inform> informColumns=new ArrayList<Inform>();
        Inform informColumn=new Inform();
        informColumn.name=("宇宙无敌大帅比");
        informColumn.isFollow=(false);
        informColumn.followNum=("442455");
        informColumns.add(informColumn);
        Inform informColumn2=new Inform();
        informColumn2.name="Amshine";
        informColumn2.isFollow=true;
        informColumn2.followNum="6746341";
        informColumns.add(informColumn2);
        informColumns.add(informColumn2);
        informColumns.add(informColumn);
        informColumns.add(informColumn);
        informColumns.add(informColumn2);
        InformSelfFollowActivity.SelfFollowFansListAdapter SelfFollowFansListAdapter=new InformSelfFollowActivity.SelfFollowFansListAdapter(informColumns,this);
        selfFasList.setLayoutManager(new LinearLayoutManager(this));
        selfFasList.setAdapter(SelfFollowFansListAdapter);
    }
}
