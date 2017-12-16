package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.TrolleyGoods;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.RichText;
import cn.czyugang.tcg.client.widget.LabelLayout;

/**
 * @author ruiaa
 * @date 2017/12/15
 */

public class OrderGoodsListActivity extends BaseActivity {

    @BindView(R.id.title_right)
    TextView goodsNum;
    @BindView(R.id.order_goods_list)
    RecyclerView goodsR;
    private List<TrolleyGoods> trolleyGoodsList;

    public static void startOrderGoodsListActivity(List<TrolleyGoods> trolleyGoodsList) {
        Intent intent = new Intent(getTopActivity(), OrderGoodsListActivity.class);
        MyApplication.getInstance().activityTransferData = trolleyGoodsList;
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trolleyGoodsList = (List<TrolleyGoods>) MyApplication.getInstance().activityTransferData;
        MyApplication.getInstance().activityTransferData = null;
        setContentView(R.layout.activity_order_goods_list);
        ButterKnife.bind(this);

        showGoodsNum();
        goodsR.setLayoutManager(new LinearLayoutManager(this));
        goodsR.setAdapter(new GoodsListAdapter(trolleyGoodsList, this));
    }

    private void showGoodsNum() {
        int num = 0;
        for (TrolleyGoods t : trolleyGoodsList) {
            num += t.num;
        }
        goodsNum.setText(num + "件");
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    private static void showPriceQuestion(Activity activity) {
        MyDialog.Builder.newBuilder(activity)
                .title("价格说明")
                .contentStr("因可能存在系统缓存、页面更新导致价格变动一场等不确定性情况出现，商品售价以本结算页商品价格为准")
                .oneButton("知道了")
                .onOneButton()
                .build()
                .show();
    }

    private static class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.Holder> {
        private List<TrolleyGoods> list;
        private Activity activity;

        public GoodsListAdapter(List<TrolleyGoods> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (position >= getItemCount() - 1) {
                holder.question.setText(RichText.newRichText("对价格有疑问，点此进行了解    ")
                        .addimgRes(16, 17, R.drawable.ic_question_grey_white, R.dimen.dp_12).build());
                holder.question.setOnClickListener(v -> showPriceQuestion(activity));
                return;
            }
            TrolleyGoods data = list.get(position);
            holder.imgView.id(data.pic);
            holder.name.setText(data.name);
            holder.spec.setText(data.spec);
            holder.labelLayout.setTexts(data.label);
            holder.price.setText(data.getRealPriceStr());
            if (data.price != data.realPrice) holder.originPrice.setText(data.getPriceStr());
            holder.num.setText("x" + data.num);
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < getItemCount() - 1) return R.layout.view_item_good;
            return R.layout.view_textview;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView question;
            ImgView imgView;
            TextView name;
            TextView spec;
            LabelLayout labelLayout;
            TextView price;
            TextView originPrice;
            TextView num;

            public Holder(View itemView) {
                super(itemView);
                question = itemView.findViewById(R.id.text);
                if (question != null) return;

                imgView = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                spec = itemView.findViewById(R.id.item_spec);
                labelLayout = itemView.findViewById(R.id.item_labels);
                price = itemView.findViewById(R.id.item_price);
                originPrice = itemView.findViewById(R.id.item_price_origin);
                num = itemView.findViewById(R.id.item_num);

                labelLayout.setLabelId(R.layout.label_red);
            }
        }
    }
}
