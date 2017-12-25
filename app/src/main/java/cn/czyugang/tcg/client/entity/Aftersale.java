package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/11/30
 */

public class Aftersale {

    /**
     * overtime	string($date-time)
     * 超时时间(超时自动退款、超时自动撤回等)
     * <p>
     * interventionFlag	string
     * 是否申请过平台介入标志(‘YES’-是,’NO’-否)
     * <p>
     * handleResult	string
     * 商家处理结果(AGREE:同意,REFUSE-拒绝,UNTREATED:未处理)
     * <p>
     * orderId	string
     * 基订单id
     * <p>
     * createTime	string($date-time)
     * 创建时间
     * <p>
     * orderNo	string
     * 退款退货单号
     * <p>
     * status	string
     * 状态(POST_APPLICATION:买家提交申请,REVOKE_APPLICATION:买家撤销申请,BUSINESS_AGREE_APPLICATION:商家同意申请,WAITING_BUYER_RETURN_GOODS:待买家发货、用户录入快递单号,BUYER_RETURN_GOODS:用户已发出退回商品,BUSINESS_REFUSE_APPLICATION:商家拒绝申请,WAITING_BUYER_APPLY_INTERVENTION:待买家申请平台介入,PLATFORM_INTERVENING:平台介入中,TIME_OUT_AUTO_AGREE:超时系统自动同意申请,TIME_OUT_AUTO_REVOKE:超时系统自动撤销申请,PLATFORM_AGREE_APPLICATION:平台同意申请,PLATFORM_REFUSE_APPLICATION:平台拒绝申请,FINISH:申请退货退款成功)
     * <p>
     * updateTime	string($date-time)
     * 更新时间
     * <p>
     * deleteFlag	string
     * 删除标志量(NORMAL-未删除,DELETED-已删除)
     * <p>
     * subOrderDetailId	string
     * 子订单详情id
     * <p>
     * type	string
     * 类型(退款-REFUND、退款退货-RETURN_ALL)
     * <p>
     * businessId	string
     * 商家id
     * <p>
     * id	string
     * 主键
     * <p>
     * reasonId	string
     * 退款原因id
     */

    @SerializedName("businessId")
    public String businessId;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("handleResult")
    public String handleResult;
    @SerializedName("id")
    public String id="";
    @SerializedName("interventionFlag")
    public String interventionFlag;
    @SerializedName("orderId")
    public String orderId;
    @SerializedName("orderNo")
    public String orderNo;
    @SerializedName("overtime")
    public String overtime;
    @SerializedName("reasonId")
    public String reasonId;
    @SerializedName("status")
    public String status="";
    /*
POST_APPLICATION:买家提交申请,
REVOKE_APPLICATION:买家撤销申请,
BUSINESS_AGREE_APPLICATION:商家同意申请,
WAITING_BUYER_RETURN_GOODS:待买家发货、用户录入快递单号,
BUYER_RETURN_GOODS:用户已发出退回商品,
BUSINESS_REFUSE_APPLICATION:商家拒绝申请,
WAITING_BUYER_APPLY_INTERVENTION:待买家申请平台介入,
PLATFORM_INTERVENING:平台介入中,
TIME_OUT_AUTO_AGREE:超时系统自动同意申请,
TIME_OUT_AUTO_REVOKE:超时系统自动撤销申请,
PLATFORM_AGREE_APPLICATION:平台同意申请,
PLATFORM_REFUSE_APPLICATION:平台拒绝申请,
FINISH:申请退货退款成功
    * */
    @SerializedName("subOrderDetailId")
    public String subOrderDetailId;
    @SerializedName("type")
    public String type;     //类型(退款-REFUND、退款退货-RETURN_ALL)
    @SerializedName("updateTime")
    public String updateTime;

    /*
    *   售后列表
    * */
    public String storeName = "";
    public String productName = "";
    public double productUnitPrice = 0;
    public double productRealPrice = 0;
    public String productAttribute = "";
    public double productNumber = 0;
    public String orderStatus = "";
    public String refundStatus = "";

    public String getStatusStr() {
        switch (status) {
            case "POST_APPLICATION":
                return "买家提交申请";
            case "REVOKE_APPLICATION":
                return "买家撤销申请";
            case "BUSINESS_AGREE_APPLICATION":
                return "商家同意申请";
            case "WAITING_BUYER_RETURN_GOODS":
                return "待买家发货、用户录入快递单号";
            case "BUYER_RETURN_GOODS":
                return "用户已发出退回商品";
            case "BUSINESS_REFUSE_APPLICATION":
                return "商家拒绝申请";
            case "WAITING_BUYER_APPLY_INTERVENTION":
                return "待买家申请平台介入";
            case "PLATFORM_INTERVENING":
                return "平台介入中";
            case "TIME_OUT_AUTO_AGREE":
                return "超时系统自动同意申请";
            case "TIME_OUT_AUTO_REVOKE":
                return "超时系统自动撤销申请";
            case "PLATFORM_AGREE_APPLICATION":
                return "平台同意申请";
            case "PLATFORM_REFUSE_APPLICATION":
                return "平台拒绝申请";
            case "FINISH":
                return "申请退货退款成功";
        }
        return "";
    }

    public boolean platformAgreeApplication(){
        return status.equals("PLATFORM_AGREE_APPLICATION");
    }

    public boolean isOnlyRefund(){
        return  type.equals("REFUND");
    }

    public String getTypeStr(){
        if (isOnlyRefund()) return "退款";
        return "退款退货";
    }

    public String getRefundStatusStr(){
        switch (refundStatus){
            case "REFUNDING":return "退款中";
            case "REVOKE":return "退款撤销";
            case "FINISH":return "已完成";
            case "SETTLEMENT":return "已结算";
            case "CLOSE":return "已关闭";
        }
        return "";
    }
}
