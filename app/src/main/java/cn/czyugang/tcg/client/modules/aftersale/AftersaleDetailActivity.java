package cn.czyugang.tcg.client.modules.aftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.czyugang.tcg.client.api.OrderApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Aftersale;
import cn.czyugang.tcg.client.entity.AftersaleDetailResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 退款/售后详情
 */

public class AftersaleDetailActivity extends BaseActivity {
    @BindView(R.id.aftersale_statusL)
    View statusL;
    @BindView(R.id.aftersale_status)
    TextView status;
    @BindView(R.id.aftersale_status_tip)
    TextView statusTip;

    @BindView(R.id.aftersale_store_name)
    TextView storeName;
    @BindView(R.id.aftersale_service_id)
    TextView serviceId;
    @BindView(R.id.aftersale_type)
    TextView type;
    @BindView(R.id.aftersale_money)
    TextView money;
    @BindView(R.id.aftersale_money_flow)
    View moneyFlow;
    @BindView(R.id.aftersale_reason)
    TextView reason;
    @BindView(R.id.aftersale_explain)
    TextView explain;
    @BindView(R.id.aftersale_time_apply)
    TextView timeApply;
    @BindView(R.id.aftersale_time_refund)
    TextView timeRefund;
    @BindView(R.id.aftersale_time_refundL)
    View timeRefundL;
    @BindView(R.id.aftersale_time_close)
    TextView timeClose;
    @BindView(R.id.aftersale_time_closeL)
    View timeCloseL;
    @BindView(R.id.aftersale_revoke)
    TextView revoke;
    @BindView(R.id.aftersale_platform_interpose)
    TextView platformInterpose;
    @BindView(R.id.aftersale_return_goods)
    TextView returnGoods;

    private String id = "";
    private AftersaleDetailResponse detailResponse;
    private Aftersale aftersale;

    public static void startAftersaleDetailActivity(String id) {
        Intent intent = new Intent(getTopActivity(), AftersaleDetailActivity.class);
        intent.putExtra("id", id);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        setContentView(R.layout.activity_aftersale_detail);
        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        OrderApi.aftersaleOrder(id).subscribe(new NetObserver<AftersaleDetailResponse>() {
            @Override
            public void onNext(AftersaleDetailResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                detailResponse = response;
                aftersale = detailResponse.data;

                showBaseInfo();
                showStatus();
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    private void showBaseInfo() {
        storeName.setText("");
        serviceId.setText(aftersale.orderNo);
        type.setText(aftersale.getTypeStr());
        money.setText(CommonUtil.formatPrice(0));
        moneyFlow.setVisibility(View.GONE);
        reason.setText(detailResponse.reason);
        explain.setText("");

        timeApply.setText("");
        timeRefund.setText("");
        timeRefundL.setVisibility(View.GONE);
        timeClose.setText("");
        timeCloseL.setVisibility(View.GONE);
    }

    private void showStatus() {
        //订单退款中，用户申请退款>    商家需在XX:XX:XX内处理，超时未处理将自动退款           撤销申请
        //订单退款成功，商家同意退款申请  任何意见和吐槽，欢迎随时联系我们
        //订单退款中，商家拒绝退款申请    请在XX:XX:XX内处理，超时未处理将自动撤销申请      撤销申请 平台介入

        //订单退款中，用户申请退货退款       商家需在XX:XX:XX内处理，超时未处理将自动同意退货申请     撤销申请
        //订单退款中，商家同意退货退款申请   请在XX:XX:XX内提交退货，超时未处理将自动撤销申请     撤销申请  退货
        //订单退款中，商家待确认收货    商家需在XX:XX:XX内处理，超时未处理将自动退款               撤销申请
        //订单退款中，商家拒绝退货申请      请在XX:XX:XX内处理，超时未处理将自动撤销申请            撤销申请 平台介入
        //订单退款中，商家拒绝退款申请      请在XX:XX:XX内处理，超时未处理将自动撤销申请(用户申请退货退款，商家确认收货阶段拒绝退款)     撤销申请 平台介入


        //订单退款中，平台介入中          客服正在处理中，请耐心等待处理结果哦~              撤销申请
        //订单退款成功                    经平台仲裁，此次退款申请予以通过
        //订单退款已关闭                  经平台仲裁，此次退款申请不予通过

    /*
POST_APPLICATION:买家提交申请,
BUSINESS_AGREE_APPLICATION:商家同意申请,
WAITING_BUYER_RETURN_GOODS:待买家发货、用户录入快递单号,
BUYER_RETURN_GOODS:用户已发出退回商品,
FINISH:申请退货退款成功

BUSINESS_REFUSE_APPLICATION:商家拒绝申请,

WAITING_BUYER_APPLY_INTERVENTION:待买家申请平台介入,
PLATFORM_INTERVENING:平台介入中,
PLATFORM_AGREE_APPLICATION:平台同意申请,
PLATFORM_REFUSE_APPLICATION:平台拒绝申请,

REVOKE_APPLICATION:买家撤销申请,
TIME_OUT_AUTO_AGREE:超时系统自动同意申请,
TIME_OUT_AUTO_REVOKE:超时系统自动撤销申请,




    * */
        //类型(退款-REFUND、退款退货-RETURN_ALL)

        switch (aftersale.status) {
            //POST_APPLICATION:买家提交申请,
            //BUSINESS_AGREE_APPLICATION:商家同意申请,
            //WAITING_BUYER_RETURN_GOODS:待买家发货、用户录入快递单号,
            //BUYER_RETURN_GOODS:用户已发出退回商品,
            //FINISH:申请退货退款成功
            case "POST_APPLICATION": {
                //POST_APPLICATION:买家提交申请,
                statusL.setBackgroundResource(R.color.main_light_blue);
                if (aftersale.isOnlyRefund()) {
                    status.setText("订单退款中，用户申请退款>");
                    statusTip.setText("商家需在XX:XX:XX内处理，超时未处理将自动退款");
                } else {
                    status.setText("订单退款中，用户申请退货退款>");
                    statusTip.setText("商家需在XX:XX:XX内处理，超时未处理将自动同意退货申请");
                }
                revoke.setVisibility(View.VISIBLE);
                break;
            }
            case "BUSINESS_AGREE_APPLICATION": {
                //BUSINESS_AGREE_APPLICATION:商家同意申请,
                if (aftersale.isOnlyRefund()) {
                    statusL.setBackgroundResource(R.color.main_light_blue);
                    status.setText("订单退款成功，商家同意退款申请>");
                    statusTip.setText("任何意见和吐槽，欢迎随时联系我们");

                    moneyFlow.setVisibility(View.VISIBLE);
                } else {
                    statusL.setBackgroundResource(R.color.main_light_red);
                    status.setText("订单退款中，商家同意退货退款申请>");
                    statusTip.setText("请在XX:XX:XX内提交退货，超时未处理将自动撤销申请");
                    revoke.setVisibility(View.VISIBLE);
                    returnGoods.setVisibility(View.VISIBLE);
                }
                break;
            }
            case "WAITING_BUYER_RETURN_GOODS": {
                //WAITING_BUYER_RETURN_GOODS:待买家发货、用户录入快递单号,
                statusL.setBackgroundResource(R.color.main_light_red);
                status.setText("订单退款中，商家同意退货退款申请>");
                statusTip.setText("请在XX:XX:XX内提交退货，超时未处理将自动撤销申请");

                revoke.setVisibility(View.VISIBLE);
                returnGoods.setVisibility(View.VISIBLE);
                break;
            }
            case "BUYER_RETURN_GOODS": {
                //BUYER_RETURN_GOODS:用户已发出退回商品,
                statusL.setBackgroundResource(R.color.main_light_blue);
                status.setText("订单退款中，商家待确认收货>");
                statusTip.setText("商家需在XX:XX:XX内处理，超时未处理将自动退款");

                revoke.setVisibility(View.VISIBLE);
                break;
            }
            case "FINISH": {
                //FINISH:申请退货退款成功
                statusL.setBackgroundResource(R.color.main_light_blue);
                status.setText("订单退款成功，商家同意退款申请>");
                statusTip.setText("任何意见和吐槽，欢迎随时联系我们");

                moneyFlow.setVisibility(View.VISIBLE);
                break;
            }
            //BUSINESS_REFUSE_APPLICATION:商家拒绝申请,
            case "BUSINESS_REFUSE_APPLICATION": {
                statusL.setBackgroundResource(R.color.main_light_red);
                if (aftersale.isOnlyRefund()) {
                    status.setText("订单退款中，商家拒绝退款申请>");
                    statusTip.setText("请在XX:XX:XX内处理，超时未处理将自动撤销申请");
                } else {
                    status.setText("订单退款中，商家拒绝退货申请>");
                    statusTip.setText("请在XX:XX:XX内处理，超时未处理将自动撤销申请");
                }
                revoke.setVisibility(View.VISIBLE);
                platformInterpose.setVisibility(View.VISIBLE);
                break;
            }
            //WAITING_BUYER_APPLY_INTERVENTION:待买家申请平台介入,
            //PLATFORM_INTERVENING:平台介入中,
            //PLATFORM_AGREE_APPLICATION:平台同意申请,
            //PLATFORM_REFUSE_APPLICATION:平台拒绝申请,
            case "WAITING_BUYER_APPLY_INTERVENTION": {
                ////WAITING_BUYER_APPLY_INTERVENTION:待买家申请平台介入,
                statusL.setBackgroundResource(R.color.main_light_red);
                status.setText("订单退款中，商家拒绝退款申请>");
                statusTip.setText("请在XX:XX:XX内处理，超时未处理将自动撤销申请");
                revoke.setVisibility(View.VISIBLE);
                platformInterpose.setVisibility(View.VISIBLE);
                break;
            }
            case "PLATFORM_INTERVENING": {
                //PLATFORM_INTERVENING:平台介入中,
                statusL.setBackgroundResource(R.color.main_light_blue);
                status.setText("订单退款中，平台介入中>");
                statusTip.setText("客服正在处理中，请耐心等待处理结果哦~");

                revoke.setVisibility(View.VISIBLE);
                break;
            }
            case "PLATFORM_AGREE_APPLICATION": {
                //PLATFORM_AGREE_APPLICATION:平台同意申请,
                statusL.setBackgroundResource(R.color.main_light_blue);
                status.setText("订单退款成功>");
                statusTip.setText("经平台仲裁，此次退款申请予以通过");

                moneyFlow.setVisibility(View.VISIBLE);
                break;
            }
            case "PLATFORM_REFUSE_APPLICATION": {
                //PLATFORM_REFUSE_APPLICATION:平台拒绝申请,
                statusL.setBackgroundResource(R.color.grey_500);
                status.setText("订单退款已关闭>");
                statusTip.setText("经平台仲裁，此次退款申请不予通过");
                break;
            }
            //REVOKE_APPLICATION:买家撤销申请,
            //TIME_OUT_AUTO_AGREE:超时系统自动同意申请,
            //TIME_OUT_AUTO_REVOKE:超时系统自动撤销申请,
            case "REVOKE_APPLICATION": {
                //REVOKE_APPLICATION:买家撤销申请,
                statusL.setBackgroundResource(R.color.grey_500);
                status.setText("订单退款已关闭>");
                statusTip.setText("您已撤销申请");
                break;
            }
            case "TIME_OUT_AUTO_AGREE": {
                statusL.setBackgroundResource(R.color.main_light_blue);
                status.setText("订单退款成功>");
                statusTip.setText("等候超时，系统已自动同意申请");

                moneyFlow.setVisibility(View.VISIBLE);
                break;
            }
            case "TIME_OUT_AUTO_REVOKE": {
                statusL.setBackgroundResource(R.color.grey_500);
                status.setText("订单退款已关闭>");
                statusTip.setText("等候超时，系统已自动撤销申请");
                break;
            }
        }
    }

    @OnClick(R.id.aftersale_statusL)
    public void onOpenTrack() {
        AftersaleTrackActivity.startAftersaleTrackActivity(detailResponse);
    }

    @OnClick(R.id.aftersale_revoke)
    public void onRevoke() {
        MyDialog.Builder.newBuilder(this)
                .contentStr("确定撤销退款申请么？")
                .onNegativeButton()
                .onPositiveButton(myDialog -> {
                    myDialog.dismiss();
                    OrderApi.aftersaleRevoke(aftersale.id).subscribe(new NetObserver<Response<Object>>() {
                        @Override
                        public void onNext(Response<Object> response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            AppUtil.toast("成功撤销退款申请");
                            getData();
                        }
                    });
                })
                .build()
                .show();

    }

    @OnClick(R.id.aftersale_platform_interpose)
    public void onPlatformInterpose() {
        MyDialog.Builder.newBuilder(this)
                .title("确定申请平台介入么？")
                .contentStr("温馨提示：介入后将由平台客服进行仲裁处理喔~")
                .onNegativeButton()
                .onPositiveButton(myDialog -> {
                    myDialog.dismiss();
                    OrderApi.aftersaleIntervention(aftersale.id).subscribe(new NetObserver<Response<Object>>() {
                        @Override
                        public void onNext(Response<Object> response) {
                            super.onNext(response);
                            if (!ErrorHandler.judge200(response)) return;
                            AppUtil.toast("成功申请平台介入");
                            getData();
                        }
                    });
                })
                .build()
                .show();
    }

    private TextView logisticsMode = null;
    private TextView logisticsName = null;
    private EditText logisticsOrder = null;

    @OnClick(R.id.aftersale_return_goods)
    public void onReturnGoods() {
        MyDialog.Builder.newBuilder(this)
                .custom(R.layout.dialog_aftersale_return_goods)
                .bindView(myDialog -> {
                    logisticsMode = myDialog.rootView.findViewById(R.id.dialog_aftersale_logistics_mode);
                    logisticsMode.setOnClickListener(v -> onSelectLogisticsMode());
                    logisticsName = myDialog.rootView.findViewById(R.id.dialog_aftersale_logistics_name);
                    logisticsName.setOnClickListener(v -> onSelectLogisticsName());
                    logisticsOrder = myDialog.rootView.findViewById(R.id.dialog_aftersale_logistics_order);
                    myDialog.onClick(R.id.tv_negative);
                    myDialog.onClick(R.id.tv_positive, v -> {
                        postReturnGoodsLogistics();
                        myDialog.dismiss();
                    });
                })
                .build()
                .show();
    }

    private List<String> logisticsModeList = new ArrayList<>();
    private OptionsPickerView logisticsModePicker = null;

    private void onSelectLogisticsMode() {
        if (logisticsModePicker == null) {
            logisticsModeList.add("快递");//快递-DELIVERY,其它-OTHER
            logisticsModeList.add("其它");

            logisticsModePicker = new OptionsPickerView.Builder(this,
                    (options1, options2, options3, v) -> {
                        String s = logisticsModeList.get(options1);
                        logisticsMode.setText(s);
                        if (s.equals("其它")){
                            logisticsName.setText("");
                            logisticsName.setClickable(false);
                        }else {
                            logisticsName.setClickable(true);
                        }
                    })
                    .setTitleText("选择退货方式")
                    .setTitleBgColor(ResUtil.getColor(R.color.bg))
                    .setTitleColor(ResUtil.getColor(R.color.text_black))
                    .setTitleSize(17)
                    .setCancelColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .setSubmitColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .isDialog(true)
                    .setOutSideCancelable(true)
                    .build();
            logisticsModePicker.setPicker(logisticsModeList);
        }

        logisticsModePicker.show();
    }

    private List<String> logisticsNameList = new ArrayList<>();
    private OptionsPickerView logisticsNamePicker = null;

    private void onSelectLogisticsName() {
        if (logisticsNamePicker == null) {
            logisticsNameList.add("哪里获取");
            logisticsNameList.add("所有");
            logisticsNameList.add("物流公司");

            logisticsNamePicker = new OptionsPickerView.Builder(this,
                    (options1, options2, options3, v) -> logisticsName.setText(logisticsNameList.get(options1)))
                    .setTitleText("选择物流公司")
                    .setTitleBgColor(ResUtil.getColor(R.color.bg))
                    .setTitleColor(ResUtil.getColor(R.color.text_black))
                    .setTitleSize(17)
                    .setCancelColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .setSubmitColor(ResUtil.getColor(R.color.text_black))
                    .setSubCalSize(16)
                    .isDialog(true)
                    .setOutSideCancelable(true)
                    .build();
            logisticsNamePicker.setPicker(logisticsNameList);
        }

        logisticsNamePicker.show();
    }

    private void postReturnGoodsLogistics() {
        String mode = "";
        String name = "";
        String order = "";
        if (logisticsMode != null) mode = logisticsMode.getText().toString();
        if (logisticsName != null) name = logisticsName.getText().toString();
        if (logisticsOrder != null) order = logisticsOrder.getText().toString();
        if (mode.isEmpty()) {
            AppUtil.toast("请选择物流方式");
            return;
        }
        OrderApi.aftersaleLogistics(mode, name, order, aftersale.id).subscribe(new NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                AppUtil.toast("成功退货");
                getData();
            }
        });
    }

    @OnClick(R.id.aftersale_money_flow)
    public void onMoneyFlow() {
        RefundStatusActivity.startRefundStatusActivity();
    }
}
