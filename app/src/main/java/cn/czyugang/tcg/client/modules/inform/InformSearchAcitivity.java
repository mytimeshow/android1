package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/19.
 */

public class InformSearchAcitivity extends BaseActivity {


    public static  final int SEARCH_TYPE_ARTICLE =0;
    public static  final int SEARCH_TYPE_LABLE = 1;
    @BindView(R.id.title_input)
    EditText searchTitle;
    RecyclerView informSearchList;


    public static void startInformSearchAcitivity(String text){
        Intent intent=new Intent(getTopActivity(),InformSearchAcitivity.class);
        intent.putExtra("searchTitle",text);
        getTopActivity().startActivity(intent);
    }

    public static void startInformSearchAcitivity(String text,int type){
        Intent intent=new Intent(getTopActivity(),InformSearchAcitivity.class);
        intent.putExtra("searchTitle",text);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_search);
        ButterKnife.bind(this);
        searchTitle.setText(getIntent().getStringExtra("searchTitle"));


    }
    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
}
