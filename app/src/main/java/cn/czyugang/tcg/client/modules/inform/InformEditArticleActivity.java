package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.img.UploadImg;
import cn.czyugang.tcg.client.utils.rxbus.EditArticleInputLinkEvent;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformEditArticleActivity extends BaseActivity {

    @BindView(R.id.edit_article_type_content)
    LinearLayout typeContent;
    @BindView(R.id.edit_article_type)
    Button type;
    @BindView(R.id.edit_article_content)
    RecyclerView content;

    private List<RichTextItem> richTextList = new ArrayList<>();
    private RichTextAdapter adapter;


    public static void startInformEditArticleActivity() {
        Intent intent = new Intent(getTopActivity(), InformEditArticleActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);
        ButterKnife.bind(this);

        adapter = new RichTextAdapter(richTextList, this);
        content.setLayoutManager(new LinearLayoutManager(this));
        content.setAdapter(adapter);
        content.setNestedScrollingEnabled(false);
        adapter.notifyChange();

        mCompositeDisposable.add(RxBus.toObservable(EditArticleInputLinkEvent.class).subscribe(event -> {
            LogRui.i("onCreate####", event.title);
        }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (uploadImg != null && uploadImg.parseResult(requestCode, resultCode, data)) {

            richTextList.add(RichTextItem.imgByPath(uploadImg.compressPath));
            adapter.notifyChange();

            uploadImg.upload(uploadImg.compressPath, "INFO_PIC", upload -> {

            });
            uploadImg = null;
            return;
        }
    }

    @OnClick(R.id.edit_article_type)
    void onType() {
        if (typeContent.getVisibility() == View.GONE) {
            typeContent.setVisibility(View.VISIBLE);
            return;
        }
        if (typeContent.getVisibility() == View.VISIBLE) {
            typeContent.setVisibility(View.GONE);
            return;
        }
    }

    @OnClick(R.id.edit_article_type_content_original)
    void selectOriginal() {
        type.setText("原创");
        typeContent.setVisibility(View.GONE);
    }

    @OnClick(R.id.edit_article_type_content_reprint)
    void selectReprint() {
        type.setText("转载");
        typeContent.setVisibility(View.GONE);
        MyDialog.informEditSourceDialog(this);
    }

    //
    private UploadImg uploadImg = null;

    @OnClick({R.id.edit_article_input_img, R.id.edit_article_input_photo})
    public void onInputImg() {
        uploadImg = new UploadImg();
        uploadImg.openSelector(this);
    }

    //
    @OnClick(R.id.edit_article_to_link)
    void onToLink() {
        MyDialog.informEditLinkDialog(this);
    }

    //
    @OnClick(R.id.edit_article_input_goods)
    public void onInputGoods() {

    }

    //
    @OnClick(R.id.edit_article_input_back)
    public void onInputBack() {

    }

    @OnClick(R.id.edit_article_input_forward)
    public void onInputForward() {

    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    private static class RichTextAdapter extends RecyclerView.Adapter<RichTextAdapter.Holder> {
        private List<RichTextItem> list;
        private Activity activity;
        private TextWatcher textWatcher;

        public RichTextAdapter(List<RichTextItem> list, Activity activity) {
            this.list = list;
            this.activity = activity;
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()==0) notifyChange();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).isText() ? R.layout.item_richtext_edittext : R.layout.item_richtext_img;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            RichTextItem data = list.get(position);
            if (holder.editText != null) {
                holder.editText.setText(data.content);
                holder.editText.addTextChangedListener(textWatcher);
            } else {
                holder.imgView.imgFile(data.imgPath);
                holder.imgClose.setOnClickListener(v -> {
                    list.remove(data);
                    notifyChange();
                });
            }
        }

        public void notifyChange() {
            if (format()) notifyDataSetChanged();
        }

        public boolean format() {
            boolean hadChange = false;
            if (list.size() == 0) {
                list.add(RichTextItem.text(""));
                hadChange = true;
                return hadChange;
            }
            if (!list.get(0).isText()) {
                list.add(RichTextItem.text(""));
                hadChange = true;
            }
            Iterator<RichTextItem> itemIterator = list.iterator();
            RichTextItem lastItem = itemIterator.next();
            if (itemIterator.hasNext()) {
                RichTextItem thisItem = itemIterator.next();
                if (lastItem.isText() && thisItem.isText()) {
                    lastItem.content = lastItem.content + "\n" + thisItem.content;
                    itemIterator.remove();
                    hadChange = true;
                }
            }
            if (list.size() == 0 || !list.get(list.size() - 1).isText()) {
                list.add(RichTextItem.text(""));
                hadChange = true;
            }
            return hadChange;
        }

        class Holder extends RecyclerView.ViewHolder {
            EditText editText;
            ImgView imgView;
            View imgClose;

            public Holder(View itemView) {
                super(itemView);
                editText = itemView.findViewById(R.id.item_edit);
                imgView = itemView.findViewById(R.id.item_img);
                imgClose = itemView.findViewById(R.id.item_img_close);
            }
        }
    }

    static class RichTextItem {
        private int type = 0;  // 100文本   200图片
        public String title = "";
        public String content = "";
        public String imgUrl = "";    //201
        public String imgPath = "";   //202
        public int imgResId = 0;      //203

        private RichTextItem() {

        }

        public boolean isText() {
            return type / 100 == 1;
        }

        public boolean isImg() {
            return type / 100 == 2;
        }

        public static RichTextItem text(String text) {
            RichTextItem richTextItem = new RichTextItem();
            richTextItem.type = 100;
            richTextItem.content = text;
            return richTextItem;
        }

        public static RichTextItem imgByPath(String path) {
            RichTextItem richTextItem = new RichTextItem();
            richTextItem.type = 202;
            richTextItem.imgPath = path;
            return richTextItem;
        }

        public static void minFormat(List<RichTextItem> list){
            Iterator<RichTextItem> itemIterator = list.iterator();
            if (itemIterator.hasNext()) {
                RichTextItem thisItem = itemIterator.next();
                if (thisItem.isText()&&thisItem.content.isEmpty()) itemIterator.remove();
            }
            if (list.isEmpty()) return;

            itemIterator = list.iterator();
            RichTextItem lastItem = itemIterator.next();
            if (itemIterator.hasNext()) {
                RichTextItem thisItem = itemIterator.next();
                if (lastItem.isText() && thisItem.isText()) {
                    lastItem.content = lastItem.content + "\n" + thisItem.content;
                    itemIterator.remove();
                }
            }
        }
    }
}
