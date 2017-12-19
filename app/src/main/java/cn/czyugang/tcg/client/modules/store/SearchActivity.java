package cn.czyugang.tcg.client.modules.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.CommonApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.inform.InformSearchAcitivity;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;
import cn.czyugang.tcg.client.widget.LabelLayout;

/**
 * @author ruiaa
 * @date 2017/12/1
 * <p>
 * 搜索初始界面：搜索推荐，历史
 */

public class SearchActivity extends BaseActivity {
    public static final int SEARCH_STORE = 1;
    public static final int SEARCH_INFORM = 2;
    @BindView(R.id.title_input)
    EditText input;
    @BindView(R.id.search_hot)
    LabelLayout hotLabel;
    @BindView(R.id.search_history)
    LabelLayout historyLabel;
    @BindView(R.id.search_type_food)
    TextView typeFood;
    @BindView(R.id.search_type_goods)
    TextView typeGoods;
    @BindView(R.id.search_type_article)
    TextView typeArticle;
    @BindView(R.id.search_type_article_label)
    TextView typeArticleLabel;
    @BindView(R.id.search_typeL)
    LinearLayout typeL;

    private int searchType = SEARCH_STORE;

    public static void startSearchActivity(int searchType) {
        Intent intent = new Intent(getTopActivity(), SearchActivity.class);
        intent.putExtra("searchType", searchType);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchType = getIntent().getIntExtra("searchType", SEARCH_STORE);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        if (searchType == SEARCH_STORE) {
            typeArticle.setVisibility(View.GONE);
            typeArticleLabel.setVisibility(View.GONE);
        } else {
            typeGoods.setVisibility(View.GONE);
            typeFood.setVisibility(View.GONE);
        }

        historyLabel.setTexts(AppKeyStorage.getSearchHistory(searchType));
        historyLabel.setOnClickItemListener((text, textView) -> SearchResultActivity.startSearchResultActivity(text));

        CommonApi.getHotSearch().subscribe(new NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    List<String> list = new ArrayList<>();
                    list.addAll(Arrays.asList(response.values.optJSONObject("platformProperties")
                            .optString("value").split(",")));
                    hotLabel.setTexts(list);
                    hotLabel.setOnClickItemListener((text, textView) -> SearchResultActivity.startSearchResultActivity(text));
                }
            }
        });
    }

    @OnClick(R.id.title_right)
    public void onBack() {
        finish();
    }

    @OnEditorAction(R.id.title_input)
    public boolean onEditAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return false;
            SearchResultActivity.startSearchResultActivity(text);
            AppKeyStorage.saveSearchHistory(text, searchType);//键盘搜索键
            finish();
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.title_input)
    public void onInput(CharSequence chars) {
        String text = chars.toString().trim();
        if (text.isEmpty()) {
            typeL.setVisibility(View.GONE);
        } else {
            typeL.setVisibility(View.VISIBLE);
            typeFood.setText(String.format("在外卖中搜索“%s”", text));
            typeGoods.setText(String.format("在商超中搜索“%s”", text));
            typeArticle.setText(String.format("在文章中搜索“%s”", text));
            typeArticleLabel.setText(String.format("在文章标签中搜索“%s”", text));
        }
    }

    @OnClick(R.id.search_type_food)
    public void onSearchFood() {
        String text = input.getText().toString().trim();
        AppKeyStorage.saveSearchHistory(text, searchType);//搜索外卖
        SearchResultActivity.startSearchResultActivity(text, SearchResultActivity.SEARCH_TYPE_FOOD);
    }

    @OnClick(R.id.search_type_goods)
    public void onSearchGoods() {
        String text = input.getText().toString().trim();
        AppKeyStorage.saveSearchHistory(text, searchType);//搜索商超
        SearchResultActivity.startSearchResultActivity(text, SearchResultActivity.SEARCH_TYPE_GOODS);
    }

    @OnClick(R.id.search_type_article)
    public void onSearchArticle() {
        String text = input.getText().toString().trim();
        AppKeyStorage.saveSearchHistory(text, searchType);//搜索文章
        InformSearchAcitivity.startInformSearchAcitivity(text,InformSearchAcitivity.SEARCH_TYPE_ARTICLE);
    }

    @OnClick(R.id.search_type_article_label)
    public void onSearchArticleLabel() {
        String text = input.getText().toString().trim();
        AppKeyStorage.saveSearchHistory(text, searchType);//搜索文章标签
        InformSearchAcitivity.startInformSearchAcitivity(text,InformSearchAcitivity.SEARCH_TYPE_LABLE);

    }


    @OnClick(R.id.search_delete_history)
    public void onClearHistory() {
        if (historyLabel.getChildCount() == 0) return;
        MyDialog.Builder.newBuilder(this)
                .contentStr("您确定要删除历史搜索记录？")
                .onPositiveButton(myDialog -> {
                    historyLabel.clear();
                    AppKeyStorage.clearSearchHistory(searchType);
                    myDialog.dismiss();
                })
                .build()
                .show();
    }


}
