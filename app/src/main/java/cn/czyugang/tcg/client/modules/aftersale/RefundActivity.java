package cn.czyugang.tcg.client.modules.aftersale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.OrderApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.OrderGoods;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.common.UploadImgAdapter;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 退款申请
 */

public class RefundActivity extends BaseActivity {

    @BindView(R.id.refund_goods_list)
    RecyclerView goodsR;
    @BindView(R.id.title_text)
    TextView title;
    @BindView(R.id.refund_goods_status)
    EditText goodsStatus;
    @BindView(R.id.refund_reason)
    EditText reason;
    @BindView(R.id.refund_money_input)
    EditText moneyInput;
    @BindView(R.id.refund_money_max)
    TextView moneyMax;
    @BindView(R.id.refund_explain)
    EditText explain;
    @BindView(R.id.refund_upload_img)
    RecyclerView uploadImg;

    private List<OrderGoods> goodsList;
    private boolean onlyRefund = true;
    private UploadImgAdapter adapter;

    private HashMap<String,String> reasonMap;

    public static void startRefundActivity(boolean onlyRefund, OrderGoods orderGoods) {
        Intent intent = new Intent(getTopActivity(), RefundActivity.class);
        intent.putExtra("onlyRefund", onlyRefund);
        List<OrderGoods> goodsList=new ArrayList<>();
        goodsList.add(orderGoods);
        MyApplication.getInstance().activityTransferData = goodsList;
        getTopActivity().startActivity(intent);
    }

    public static void startRefundActivity(boolean onlyRefund, List<OrderGoods> orderGoodsList) {
        Intent intent = new Intent(getTopActivity(), RefundActivity.class);
        intent.putExtra("onlyRefund", onlyRefund);
        MyApplication.getInstance().activityTransferData = orderGoodsList;
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onlyRefund = getIntent().getBooleanExtra("onlyRefund", true);
        goodsList = (List<OrderGoods>) MyApplication.getInstance().activityTransferData;
        MyApplication.getInstance().activityTransferData = null;

        setContentView(R.layout.activity_aftersale_refund);
        ButterKnife.bind(this);

        showGoods();

        adapter = new UploadImgAdapter(this);
        uploadImg.setLayoutManager(new GridLayoutManager(this, 3));
        uploadImg.setAdapter(adapter);
        uploadImg.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,
                ResUtil.getDimenInPx(R.dimen.dp_6), ResUtil.getColor(R.color.white)));

        if (onlyRefund) {
            //仅退款
            findViewById(R.id.refund_goods_status_arrow).setVisibility(View.GONE);
            goodsStatus.setText("商家未发货");
            goodsStatus.setClickable(false);
            title.setText("退款申请");
        }

        OrderApi.aftersaleDict().subscribe(new BaseActivity.NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                reasonMap=new HashMap<>();
                if (response.values==null) return;
                JSONArray jsonArray=response.values.optJSONArray("reasonList");
                for(int i=0,size=jsonArray.length();i<size;i++){
                    JSONObject jsonObject=jsonArray.optJSONObject(i);
                    if (jsonObject==null) continue;
                    reasonMap.put(jsonObject.optString("reason"),jsonObject.optString("id"));
                }
            }
        });
    }

    private void showGoods() {
        GoodsAdapter adapter=new GoodsAdapter(goodsList,this);
        goodsR.setLayoutManager(new LinearLayoutManager(this));
        goodsR.setAdapter(adapter);
        goodsR.setNestedScrollingEnabled(false);

        moneyMax.setText(String.format("最多退￥%.2f，含配送费￥%.2f",getPayPrice(),getPayPackagePrice()));
    }

    private double getPayPrice(){
        double d=0;
        for(OrderGoods orderGoods:goodsList){
            d+=orderGoods.payPrice;
        }
        return d;
    }

    private double getPayPackagePrice(){
        double d=0;
        for(OrderGoods orderGoods:goodsList){
            d+=orderGoods.packagePrice;
        }
        return d;
    }

    private String getOderGoodsIds(){
        StringBuilder builder=new StringBuilder();
        for(OrderGoods orderGoods:goodsList){
            builder.append(orderGoods.id);
            builder.append(",");
        }
        if (builder.length()>1) builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.parseResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    private List<String> goodsStatusList = new ArrayList<>();
    private OptionsPickerView goodsStatusPicker = null;

    @OnClick(R.id.refund_goods_status)
    public void onGoodsStatus() {
        if (goodsStatusPicker == null) {
            goodsStatusList.add("已收到商品");
            goodsStatusList.add("未收到商品");
            //goodsStatusList.add("商家未发货");

            goodsStatusPicker = new OptionsPickerView.Builder(this,
                    (options1, options2, options3, v) -> goodsStatus.setText(goodsStatusList.get(options1)))
                    .setTitleText("选择商品状态")
                    .setTitleBgColor(ResUtil.getColor(R.color.bg))
                    .setTitleColor(ResUtil.getColor(R.color.text_black))
                    .setTitleSize(17)
                    .setCancelColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .setSubmitColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .build();
            goodsStatusPicker.setPicker(goodsStatusList);
        }

        goodsStatusPicker.show();
    }

    private List<String> reasonList = new ArrayList<>();
    private OptionsPickerView reasonPicker = null;

    @OnClick(R.id.refund_reason)
    public void onReason() {
        if (reasonPicker == null) {
            reasonList.addAll(reasonMap.keySet());

            reasonPicker = new OptionsPickerView.Builder(this,
                    (options1, options2, options3, v) -> reason.setText(reasonList.get(options1)))
                    .setTitleText("选择退款原因")
                    .setTitleBgColor(ResUtil.getColor(R.color.bg))
                    .setTitleColor(ResUtil.getColor(R.color.text_black))
                    .setTitleSize(17)
                    .setCancelColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .setSubmitColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .build();
            reasonPicker.setPicker(reasonList);
        }

        reasonPicker.show();
    }

    private boolean checkInput() {
        if (goodsStatus.getText().toString().equals("")) {
            AppUtil.toast("请选择商品状态");
            return false;
        }
        if (reason.getText().toString().equals("")) {
            AppUtil.toast("请选择退款原因");
            return false;
        }
        if (moneyInput.getText().toString().equals("")) {
            AppUtil.toast("请输入退款金额");
            return false;
        }
        return true;
    }

    @OnClick(R.id.commit)
    public void onCommit() {
        if (!checkInput()) return;
        /*
        type*	string  类型(退款-REFUND、退款退货-RETURN_ALL)
        actualRefund*	number  实际退款金额
        refund*	number 退款金额
        goodsStatus*	string 商品状态(未收到商品-NOT_RECEIVED,已收到商品-RECEIVED,商家未发货-NOT_SEND)
        reasonId*	string 退款原因
        supplement*	string 退款说明
        picIdList	[string] description:凭证图片

        orderId*	string 订单id
        subOrderDetailId*	string 子订单详情id(如商品订单详情id,sub_order_goods_detail.id)
        * */
        String status = "";
        switch (goodsStatus.getText().toString()) {
            case "未收到商品":
                status = "NOT_RECEIVED";
                break;
            case "已收到商品":
                status = "RECEIVED";
                break;
            case "商家未发货":
                status = "NOT_SEND";
                break;
        }

        String reasonId=reasonMap.get(reason.getText().toString());
        double refundNum=Double.valueOf(moneyInput.getText().toString());

        HashMap<String,Object> map=new HashMap<>();
        map.put("type",onlyRefund?"REFUND":"RETURN_ALL");
        map.put("actualRefund",refundNum);
        map.put("refund",getPayPrice());
        map.put("goodsStatus",status);
        map.put("reasonId",reasonId);
        map.put("supplement",explain.getText().toString());
        map.put("orderId",goodsList.get(0).orderId);
        map.put("subOrderDetailId",getOderGoodsIds());
        map.put("picIdList",adapter.getUploadImgIds());
        OrderApi.aftersaleRefund(map).subscribe(new NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)){
                    RefundApplyFinishActivity.startRefundApplyFinishActivity();
                }
            }
        });
    }

    static class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.Holder> {
        private List<OrderGoods> list;
        private Activity activity;
        public GoodsAdapter(List<OrderGoods> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType,parent,false));
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.view_item_good;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            OrderGoods orderGoods=list.get(position);

            holder.goodsImg.id(orderGoods.picId);

            holder.goodsName.setText(orderGoods.title);
            holder.goodsSpec.setText(orderGoods.getSpec());
            holder.labels.setTexts(orderGoods.getLabels());

            holder.goodsPrice.setText(CommonUtil.formatPrice(orderGoods.realPrice));
            if (orderGoods.unitPrice > orderGoods.realPrice)
                holder.goodsPriceOrigin.setText(CommonUtil.formatOriginPrice(orderGoods.unitPrice));
            holder.goodsNum.setText("x" + orderGoods.number);
        }

        class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_img)
            ImgView goodsImg;
            @BindView(R.id.item_name)
            TextView goodsName;
            @BindView(R.id.item_spec)
            TextView goodsSpec;
            @BindView(R.id.item_labels)
            LabelLayout labels;
            @BindView(R.id.item_price)
            TextView goodsPrice;
            @BindView(R.id.item_price_origin)
            TextView goodsPriceOrigin;
            @BindView(R.id.item_num)
            TextView goodsNum;
            @BindView(R.id.item_tag)
            TextView goodsTag;
            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
