package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/18
 */

public class OrderDelivery {

    /**
     * baseOrderId : string
     * baseUserId : string
     * createTime : 2017-12-18T04:58:21.471Z
     * deleteFlag : string
     * deliveryCost : 0
     * deliveryCostBonus : 0
     * distance : string
     * exUserId : string
     * expectDeliveryTime : string
     * fetchCode : string
     * fetchCodeStatus : string
     * goodsType : string
     * id : string
     * isAssigned : string
     * prepareCountdown : 2017-12-18T04:58:21.471Z
     * qrCodeUrl : string
     * receiveAddressId : string
     * status : string
     * storeId : string
     * type : string
     * updateTime : 2017-12-18T04:58:21.471Z
     */

    @SerializedName("baseOrderId")
    public String baseOrderId;      //基础订单id
    @SerializedName("baseUserId")
    public String baseUserId;    //配送员用户id/店铺配送员id
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;       //是否删除(DELETE：删除,NORMAL:正常)
    @SerializedName("deliveryCost")
    public double deliveryCost;     //跑腿总金额
    @SerializedName("deliveryCostBonus")
    public double deliveryCostBonus;        //跑腿优惠金额
    @SerializedName("distance")
    public String distance;     //路程
    @SerializedName("exUserId")
    public String exUserId;     //上任配送员id/店铺配送员id
    @SerializedName("expectDeliveryTime")
    public String expectDeliveryTime;       //期望送达时间
    @SerializedName("fetchCode")
    public String fetchCode;        //自提码
    @SerializedName("fetchCodeStatus")
    public String fetchCodeStatus;  //自提码状态（USED-已使用；UNUSED-未使用）
    @SerializedName("goodsType")
    public String goodsType;        //商品类型（商超-MARKET、外卖-TAKEOUT）
    @SerializedName("id")
    public String id;       //配送订单表id
    @SerializedName("isAssigned")
    public String isAssigned;       //是否为指派订单（YES-是；NO-否）
    @SerializedName("prepareCountdown")
    public String prepareCountdown;     //备货倒计时（备货结束时间点）
    @SerializedName("qrCodeUrl")
    public String qrCodeUrl;
    @SerializedName("receiveAddressId")
    public String receiveAddressId;     //收货地址(收货地、收货联系人、收货联系电话)
    @SerializedName("status")
    public String status;       //配送订单状态（未接单-WAITING；已接单-DOING；已抵达-ARRIVED；配送中-DELIVERING；已完成-DONE；取消中-CANCELING；已取消-CANCEL）
    @SerializedName("storeId")
    public String storeId;      //发货地址(包含发货地、发货地联系人、发货联系电话)
    @SerializedName("type")
    public String type;     //配送方式 PLATFORM-平台配送，STORE-商家配送，FETCH-自提，RUN-跑腿配送
    @SerializedName("updateTime")
    public String updateTime;

    //




    public String getTypeStr() {
        switch (type) {
            case "PLATFORM":
                return "平台配送";
            case "STORE":
                return "商家配送";
            case "FETCH":
                return "自提";
            case "RUN":
                return "跑腿配送";
        }
        return "";
    }

}
