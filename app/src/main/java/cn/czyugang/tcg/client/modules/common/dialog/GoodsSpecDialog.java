package cn.czyugang.tcg.client.modules.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.GoodsSpec;
import cn.czyugang.tcg.client.entity.GoodsSpecResponse;
import cn.czyugang.tcg.client.entity.TrolleyGoods;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.GoodsPlusMinusView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.RecyclerViewMaxH;

/**
 * @author ruiaa
 * @date 2017/12/9
 */

public class GoodsSpecDialog extends DialogFragment {

    private Good good;
    private OnBuyListener onBuyListener=null;

    private View rootView;
    private View close;
    private ImgView imgView;
    private TextView name;
    private TextView remain;
    private TextView selectSpec;
    private TextView disCount;
    private RecyclerViewMaxH labelR;
    private TextView price;
    private GoodsPlusMinusView plusMinusView;

    private int num = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.BottomDialogAnimation);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(-1, -2);
        window.setGravity(Gravity.BOTTOM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.view_goods_spec, container);
        close = rootView.findViewById(R.id.view_close);
        imgView = rootView.findViewById(R.id.view_img);
        name = rootView.findViewById(R.id.view_name);
        remain = rootView.findViewById(R.id.view_remain);
        selectSpec = rootView.findViewById(R.id.view_select_spec);
        disCount = rootView.findViewById(R.id.view_discount);
        labelR = rootView.findViewById(R.id.view_spec_list);
        price = rootView.findViewById(R.id.view_price);
        plusMinusView = rootView.findViewById(R.id.view_plus_minus);


        imgView.id(good.pic);
        name.setText(good.title);
        close.setOnClickListener(v -> dismiss());

        plusMinusView.setIsMultiSpec(false)
                .setOnPlusMinusListener(addNum -> {     //规格弹框
                    return num += addNum;
                })
                .setNum(num);
        labelR.setLayoutManager(new LinearLayoutManager(getActivity()));
        labelR.setMaxHeight(ResUtil.getDimenInPx(R.dimen.dp_280));

        refreshSpec();

        return rootView;
    }


    //设置与规格相关的
    private void refreshSpec() {
        if (good.specResponse == null || rootView == null) return;
        if (labelR.getAdapter() == null) {
            good.specResponse.resetSelectLabel();   //首次打开，重置已选属性值为第一个
            labelR.setAdapter(new GoodsSpecAdapter(good.specResponse.data, getActivity()));
        }
        price.setText(good.specResponse.getPriceStr());
        remain.setText("库存："+good.specResponse.getInventory());
        selectSpec.setText(good.specResponse.getShowSelectSpec());
        plusMinusView.setClickable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onBuyListener!=null) onBuyListener.onBuyListener(good.specResponse.buy(good),num);
    }

    public GoodsSpecDialog setGood(Good good) {
        this.good = good;
        if (good.specResponse==null||good.specResponse.values==null) getAllSpec();
        return this;
    }

    public GoodsSpecDialog setOnBuyListener(OnBuyListener onBuyListener) {
        this.onBuyListener = onBuyListener;
        return this;
    }

    public static void showSpecDialog(Activity activity, Good good,OnBuyListener onBuyListener) {
        new GoodsSpecDialog().setGood(good).setOnBuyListener(onBuyListener).show(activity.getFragmentManager(), "GoodsSpecDialog");
    }

    private void getAllSpec() {
        StoreApi.getGoodsSpec(good.inventoryId)
                .subscribe(new BaseActivity.NetObserver<GoodsSpecResponse>() {
                    @Override
                    public void onNext(GoodsSpecResponse goodsSpecResponse) {
                        goodsSpecResponse.parse();
                        good.specResponse=goodsSpecResponse;
                        refreshSpec();
                    }
                });
    }

    private class GoodsSpecAdapter extends RecyclerView.Adapter<GoodsSpecAdapter.Holder> {
        private List<GoodsSpec> list;
        private Activity activity;

        public GoodsSpecAdapter(List<GoodsSpec> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_goods_spec, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            GoodsSpec data = list.get(position);

            holder.name.setText(data.name);
            holder.labelLayout.setTexts(data.labelList);
            holder.labelLayout.setOnClickItemListener((text, textView) -> {
                holder.labelLayout.lastSelectTextView.setBackgroundResource(R.drawable.border_rect_grey);
                holder.labelLayout.lastSelectTextView.setTextColor(ResUtil.getColor(R.color.text_black));
                textView.setBackgroundResource(R.drawable.bg_rect_light_red);
                textView.setTextColor(ResUtil.getColor(R.color.white));
                data.selectLabel = text;
                refreshSpec();
            });
            holder.labelLayout.lastSelectTextView.setBackgroundResource(R.drawable.bg_rect_light_red);
            holder.labelLayout.lastSelectTextView.setTextColor(ResUtil.getColor(R.color.white));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView name;
            LabelLayout labelLayout;

            public Holder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                labelLayout = itemView.findViewById(R.id.item_labels);
            }
        }
    }

    public interface OnBuyListener{
        void onBuyListener(TrolleyGoods trolleyGoods,int num);
    }
}