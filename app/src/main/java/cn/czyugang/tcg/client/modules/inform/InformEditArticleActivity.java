package cn.czyugang.tcg.client.modules.inform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformEditArticleActivity extends BaseActivity {

    @BindView(R.id.edit_article_type_content)
    LinearLayout typeContent;

    @BindView(R.id.edit_article_type)
    TextView type;

    public static void startInformEditArticleActivity( ){
        Intent intent=new Intent(getTopActivity(),InformEditArticleActivity.class);
        getTopActivity().startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.edit_article_type)
    void onType(){
        if(typeContent.getVisibility()== View.GONE){
            typeContent.setVisibility(View.VISIBLE);
            return;
        }
        if(typeContent.getVisibility()== View.VISIBLE){
            typeContent.setVisibility(View.GONE);
            return;
        }
    }
    @OnClick(R.id.edit_article_type_content_original)
    void selectOriginal(){
        type.setText("原创");
        typeContent.setVisibility(View.GONE);
    }

    @OnClick(R.id.edit_article_type_content_reprint)
    void selectReprint(){
        type.setText("转载");
        typeContent.setVisibility(View.GONE);
    }
}
