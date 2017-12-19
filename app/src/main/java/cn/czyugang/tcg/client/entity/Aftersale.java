package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/11/30
 */

public class Aftersale {

    /**
     overtime	string($date-time)
     超时时间(超时自动退款、超时自动撤回等)

     interventionFlag	string
     是否申请过平台介入标志(‘YES’-是,’NO’-否)

     handleResult	string
     商家处理结果(AGREE:同意,REFUSE-拒绝,UNTREATED:未处理)

     orderId	string
     基订单id

     createTime	string($date-time)
     创建时间

     orderNo	string
     退款退货单号

     status	string
     状态(POST_APPLICATION:买家提交申请,REVOKE_APPLICATION:买家撤销申请,BUSINESS_AGREE_APPLICATION:商家同意申请,WAITING_BUYER_RETURN_GOODS:待买家发货、用户录入快递单号,BUYER_RETURN_GOODS:用户已发出退回商品,BUSINESS_REFUSE_APPLICATION:商家拒绝申请,WAITING_BUYER_APPLY_INTERVENTION:待买家申请平台介入,PLATFORM_INTERVENING:平台介入中,TIME_OUT_AUTO_AGREE:超时系统自动同意申请,TIME_OUT_AUTO_REVOKE:超时系统自动撤销申请,PLATFORM_AGREE_APPLICATION:平台同意申请,PLATFORM_REFUSE_APPLICATION:平台拒绝申请,FINISH:申请退货退款成功)

     updateTime	string($date-time)
     更新时间

     deleteFlag	string
     删除标志量(NORMAL-未删除,DELETED-已删除)

     subOrderDetailId	string
     子订单详情id

     type	string
     类型(退款-REFUND、退款退货-RETURN_ALL)

     businessId	string
     商家id

     id	string
     主键

     reasonId	string
     退款原因id
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
    public String id;
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
    public String status;
    @SerializedName("subOrderDetailId")
    public String subOrderDetailId;
    @SerializedName("type")
    public String type;
    @SerializedName("updateTime")
    public String updateTime;
}
