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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;
import cn.czyugang.tcg.client.widget.LabelLayout;

/**
 * @author ruiaa
 * @date 2017/12/1
 * <p>
 * 搜索初始界面：搜索推荐，历史
 */

public class SearchActivity extends BaseActivity {

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
    @BindView(R.id.search_typeL)
    LinearLayout typeL;

    public static void startSearchActivity() {
        Intent intent = new Intent(MyApplication.getContext(), SearchActivity.class);
        MyApplication.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        LogRui.i("onCreate####", AppKeyStorage.getSearchHistory());
        historyLabel.setTexts(AppKeyStorage.getSearchHistory());
        historyLabel.setOnClickItemListener((text,textView) -> SearchResultActivity.startSearchResultActivity(text));

        List<String> list = new ArrayList<>();
        list.add("软键盘");
        list.add("hoeif");
        list.add("sadoj;as");
        list.add("搜索键");
        list.add("hoeif");
        list.add("软键盘的搜索键");
        hotLabel.setTexts(list);
        hotLabel.setOnClickItemListener((text,textView) -> SearchResultActivity.startSearchResultActivity(text));
    }

    @OnClick(R.id.title_right)
    public void onBack() {
        finish();
    }

    @OnEditorAction(R.id.title_input)
    public boolean onEditAction(TextView v, int actionId,KeyEvent event){
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return false;
            SearchResultActivity.startSearchResultActivity(text);
            AppKeyStorage.saveSearchHistory(text);
            finish();
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.title_input)
    public void onInput(CharSequence chars){
        String text=chars.toString().trim();
        if (text.isEmpty()){
            typeL.setVisibility(View.GONE);
        }else {
            typeL.setVisibility(View.VISIBLE);
            typeFood.setText(String.format("在外卖中搜索“%s”",text));
            typeGoods.setText(String.format("在商超中搜索“%s”",text));
        }
    }

    @OnClick(R.id.search_type_food)
    public void onSearchFood(){
        String text=input.getText().toString().trim();
        AppKeyStorage.saveSearchHistory(text);
        SearchResultActivity.startSearchResultActivity(text,SearchResultActivity.SEARCH_TYPE_FOOD);
    }

    @OnClick(R.id.search_type_goods)
    public void onSearchGoods(){
        String text=input.getText().toString().trim();
        AppKeyStorage.saveSearchHistory(text);
        SearchResultActivity.startSearchResultActivity(input.getText().toString().trim(),SearchResultActivity.SEARCH_TYPE_GOODS);
    }

    @OnClick(R.id.search_delete_history)
    public void onClearHistory() {
        if (historyLabel.getChildCount() == 0) return;
        MyDialog.Builder.newBuilder(this)
                .contentStr("您确定要删除历史搜索记录？")
                .onPositiveButton(myDialog -> {
                    historyLabel.clear();
                    AppKeyStorage.clearSearchHistory();
                    myDialog.dismiss();
                })
                .build()
                .show();
    }


}
