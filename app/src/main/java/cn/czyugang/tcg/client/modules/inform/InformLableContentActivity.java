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
import cn.czyugang.tcg.client.entity.InformFollow;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformLableContentActivity extends BaseActivity {


    @BindView(R.id.inform_label_content_list)
    RecyclerView labelContentList;
    @BindView(R.id.inform_label_content_title_name)
    TextView tvTitleName;

    static String titleName;

    public static void startInformLableContentActivity(String title){
        Intent intent=new Intent(getTopActivity(),InformLableContentActivity.class);
        getTopActivity().startActivity(intent);
        titleName=title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_label_content);
        ButterKnife.bind(this);

        tvTitleName.setText(titleName);

        List<InformFollow> followCotentsList = new ArrayList<InformFollow>();
        InformFollow followCotent = new InformFollow();
        followCotent.name=("博主名称");
        followCotent.content=("内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
        followCotent.commentNum=("1234567");
        followCotent.thumbNum=("777777777");
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        InformFollowFragment.FollowContentAdapter followContentAdapter = new InformFollowFragment.FollowContentAdapter(followCotentsList,this);
        labelContentList.setLayoutManager(new LinearLayoutManager(this));
        labelContentList.setAdapter(followContentAdapter);
    }
}
