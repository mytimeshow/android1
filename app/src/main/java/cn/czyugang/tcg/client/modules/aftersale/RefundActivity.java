package cn.czyugang.tcg.client.modules.aftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.modules.common.UploadImgAdapter;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 退款申请
 */

public class RefundActivity extends BaseActivity {
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

    private boolean onlyRefund=true;
    private UploadImgAdapter adapter;

    public static void startRefundActivity(boolean onlyRefund) {
        Intent intent = new Intent(getTopActivity(), RefundActivity.class);
        intent.putExtra("onlyRefund",onlyRefund);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersale_refund);
        ButterKnife.bind(this);

        onlyRefund=getIntent().getBooleanExtra("onlyRefund",true);

        adapter = new UploadImgAdapter(this);
        uploadImg.setLayoutManager(new GridLayoutManager(this, 3));
        uploadImg.setAdapter(adapter);
        uploadImg.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,
                ResUtil.getDimenInPx(R.dimen.dp_6), ResUtil.getColor(R.color.white)));

        if (onlyRefund){
            //仅退款
            findViewById(R.id.refund_goods_status_arrow).setVisibility(View.GONE);
            goodsStatus.setText("商家未发货");
            goodsStatus.setClickable(false);
            title.setText("退款申请");
        }
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
            reasonList.add("未收到商品");
            reasonList.add("不喜欢");

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
        return true;
    }

    @OnClick(R.id.commit)
    public void onCommit() {
        //if (!checkInput()) return;
        RefundApplyFinishActivity.startRefundApplyFinishActivity();
    }
}
