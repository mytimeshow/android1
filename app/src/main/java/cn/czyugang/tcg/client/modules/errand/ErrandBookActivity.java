package cn.czyugang.tcg.client.modules.errand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.img.UploadImg;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class ErrandBookActivity extends BaseActivity {
    public static final int ERRAND_TYPE_BUY = 1;
    public static final int ERRAND_TYPE_SEND = 2;
    public static final int ERRAND_TYPE_TAKE = 3;
    @BindView(R.id.title_text)
    TextView title;
    @BindView(R.id.errand_book_things_type)
    RecyclerView thingsTypeR;
    @BindView(R.id.errand_book_things_type_labels)
    LabelLayout thingsTypeLabels;
    @BindView(R.id.errand_book_input_goods)
    EditText namesInput;
    @BindView(R.id.errand_book_good_img)
    ImgView goodImg;
    @BindView(R.id.errand_book_good_cost)
    TextView goodCost;
    @BindView(R.id.errand_book_good_cost_input)
    EditText goodCostInput;
    @BindView(R.id.errand_book_things_weightL)
    LinearLayout thingsWeightL;
    @BindView(R.id.errand_book_things_volume)
    TextView thingsVolume;
    @BindView(R.id.errand_book_things_volumeL)
    LinearLayout thingsVolumeL;
    @BindView(R.id.errand_book_things_value)
    TextView thingsValue;
    @BindView(R.id.errand_book_things_valueL)
    LinearLayout thingsValueL;
    @BindView(R.id.errand_book_address_buy)
    TextView addressBuy;
    @BindView(R.id.errand_book_address_buyL)
    LinearLayout addressBuyL;
    @BindView(R.id.errand_book_address_buy_detail)
    EditText addressBuyDetail;
    @BindView(R.id.errand_book_address_get)
    TextView addressGet;
    @BindView(R.id.errand_book_address_getL)
    LinearLayout addressGetL;
    @BindView(R.id.errand_book_address_receipt)
    TextView addressReceipt;
    @BindView(R.id.errand_book_address_receiptL)
    LinearLayout addressReceiptL;
    @BindView(R.id.errand_book_send_time)
    TextView sendTime;
    @BindView(R.id.errand_book_send_timeL)
    LinearLayout sendTimeL;
    @BindView(R.id.errand_book_notice_input)
    EditText noticeInput;
    @BindView(R.id.errand_book_fee_delivery_ask)
    ImageView feeDeliveryAsk;
    @BindView(R.id.errand_book_fee_delivery)
    TextView feeDelivery;
    @BindView(R.id.errand_book_coupon)
    TextView coupon;
    @BindView(R.id.errand_book_couponL)
    LinearLayout couponL;
    @BindView(R.id.errand_book_fee_reward)
    TextView feeReward;
    @BindView(R.id.errand_book_fee_rewardL)
    LinearLayout feeRewardL;
    @BindView(R.id.errand_book_agreement_select)
    SelectButton agreementSelect;
    @BindView(R.id.errand_book_agreement)
    TextView agreement;
    @BindView(R.id.errand_book_pay_real)
    TextView payReal;
    @BindView(R.id.errand_book_commit)
    TextView commit;

    private int type;
    private UploadImg uploadImg = null;//"COMMENT"
    private ThingsTypeAdapter adapter;

    public static void startErrandBookActivity(int type) {
        Intent intent = new Intent(getTopActivity(), ErrandBookActivity.class);
        intent.putExtra("type", type);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", ERRAND_TYPE_BUY);

        setContentView(R.layout.activity_errand_book);
        ButterKnife.bind(this);

        switch (type) {
            case ERRAND_TYPE_BUY:
                initTypeBuy();
                break;
            case ERRAND_TYPE_SEND:
                initTypeSend();
                break;
            case ERRAND_TYPE_TAKE:
                initTypeTake();
                break;
        }

        goodImg.setVisibility(View.GONE);
        adapter = new ThingsTypeAdapter(new ArrayList<>(), this);
        thingsTypeR.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        thingsTypeR.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        parseSelectImg(requestCode, resultCode, data);
    }

    private void initTypeBuy() {
        title.setText("帮我买");
        thingsWeightL.setVisibility(View.GONE);
        thingsVolumeL.setVisibility(View.GONE);
        thingsValueL.setVisibility(View.GONE);
        addressGetL.setVisibility(View.GONE);
    }

    private void initTypeSend() {
        title.setText("帮我送");
        thingsTypeLabels.setVisibility(View.GONE);
        goodCost.setVisibility(View.GONE);
        goodCostInput.setVisibility(View.GONE);
        addressBuyL.setVisibility(View.GONE);
        addressBuyDetail.setVisibility(View.GONE);
    }

    private void initTypeTake() {
        title.setText("帮我取");
        thingsTypeLabels.setVisibility(View.GONE);
        goodCost.setVisibility(View.GONE);
        goodCostInput.setVisibility(View.GONE);
        addressBuyL.setVisibility(View.GONE);
        addressBuyDetail.setVisibility(View.GONE);
    }

    private boolean checkBuyInput() {
        return true;
    }

    private boolean checkSendInput() {
        return true;
    }

    private boolean checkTakeInput() {
        return true;
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick({R.id.errand_book_upload_img, R.id.errand_book_upload_img_text})
    public void onUploadImg() {
        if (uploadImg == null) uploadImg = new UploadImg();
        uploadImg.openSelector(this);
    }

    private void parseSelectImg(int requestCode, int resultCode, Intent data) {
        if (uploadImg != null) {
            uploadImg.parseResult(requestCode, resultCode, data);
            if (uploadImg.compressPath.equals("")) return;
            uploadImg.upload(uploadImg.compressPath, "COMMENT");
            goodImg.setVisibility(View.VISIBLE);
            goodImg.imgFile(uploadImg.compressPath);
        }
    }

    /*
    *   物品
    * */
    @OnClick(R.id.errand_book_things_volumeL)
    public void onThingsVolume() {

    }

    @OnClick(R.id.errand_book_things_valueL)
    public void onThingsValue() {

    }

    /*
    *   地址
    * */
    @OnClick(R.id.errand_book_address_buyL)
    public void onAddressBuy() {

    }

    @OnClick(R.id.errand_book_address_receiptL)
    public void onAddressReceipt() {

    }

    @OnClick(R.id.errand_book_address_getL)
    public void onAddressGet() {

    }

    @OnClick(R.id.errand_book_send_timeL)
    public void onSendTime() {

    }


    /*
    *   费用
    * */
    @OnClick(R.id.errand_book_fee_delivery_ask)
    public void onAskDeliveryFee() {
        MyDialog.Builder.newBuilder(this)
                .title("跑腿配送费用说明")
                .contentStr("1、3公里内起步配送费为XX元\n"
                        + "2、超出3公里的，续程费用为YY元/公里\n "
                        + "3、配送费用受商品重量、体积或恶劣天气等因素影响或有所浮动，实际费用以本结算页配送费为准")
                .oneButton("知道了")
                .onOneButton()
                .build()
                .show();
    }

    @OnClick(R.id.errand_book_couponL)
    public void onCoupon() {

    }

    private OptionsPickerView feeRewardPicker = null;
    private List<String> feeRewardStrList = new ArrayList<>();
    private List<Integer> feeRewardList = new ArrayList<>();
    private double reward = 0;
    //  ￥0.00  ￥3.00  ￥5.00  ￥10.00  ￥15.00  ￥20.00  ￥25.00  ￥30.00

    @OnClick(R.id.errand_book_fee_rewardL)
    public void onFeeReward() {
        if (feeRewardPicker == null) {
            feeRewardStrList.addAll(Arrays.asList("￥0.00", "￥3.00", "￥5.00", "￥10.00", "￥15.00", "￥20.00", "￥25.00", "￥30.00"));
            feeRewardList.addAll(Arrays.asList(0, 3, 5, 10, 15, 20, 25, 30));
            feeRewardPicker = new OptionsPickerView.Builder(this,
                    (options1, options2, options3, v) -> {
                        feeReward.setText(feeRewardStrList.get(options1));
                        reward = feeRewardList.get(options1);
                    })
                    .setTitleText("选择商品状态")
                    .setTitleBgColor(ResUtil.getColor(R.color.bg))
                    .setTitleColor(ResUtil.getColor(R.color.text_black))
                    .setTitleSize(17)
                    .setCancelColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .setSubmitColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .build();
            feeRewardPicker.setPicker(feeRewardStrList);
        }
        feeRewardPicker.show();
    }

    /*
    *   协议
    * */
    @OnClick(R.id.errand_book_agreement)
    public void onAgreement() {

    }

    private static class ThingsTypeAdapter extends RecyclerView.Adapter<ThingsTypeAdapter.Holder> {
        private List<Object> list;
        private Activity activity;

        public ThingsTypeAdapter(List<Object> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_errand_things_type, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            //Object data=list.get(position);
        }

        @Override
        public int getItemCount() {
            return 8;
            //return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
