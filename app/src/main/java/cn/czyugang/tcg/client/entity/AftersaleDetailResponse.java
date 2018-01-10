package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2018/1/8
 */

public class AftersaleDetailResponse extends Response<Aftersale> {
    public String returnType="";
    public String reason="";
    public List<String> buyerImgList=new ArrayList<>();
    public List<String> sellerImgList=new ArrayList<>();
    public List<String> statusList=new ArrayList<>();
    public List<String> timeList=new ArrayList<>();


    public void parse(){
        if (values==null) return;
        try{

            //售后申请信息-data{orderNo-售后编号,createTime-申请时间.overtime-超时时间,type-售后类型}
            //售后状态字典-statusDict
            //售后状态说明字典-statusSupplementDict
            //是否申请了平台介入字典-interventionFlagDict
            //售后类型字典(退货退款、退款)-typeDict
            //商家处理结果字典-handleResultDict
            //售后详情-values.returnSubOrderApplyDetail{goodsStatus-发货状态，refund-退款金额,actualRefund-时间退款金额,supplement-用户补充说明,shopReason-商家拒绝理由,businessSupplement-商家拒绝说明}
            //售后商品状态字典-values.returnSubOrderApplyDetail_goodsStatusDict
            //退货原因-values.returnRefundReason{reason-退款原因}
            //用户退货快递信息-values.returnGoodsInfo{logisticsMode-物流方式,logisticsOrder-物流单号,logisticsName-快递公司名}
            //退货物流模式字典-values.returnGoodsInfo_logisticsModeDict
            //节点信息-values.returnOrderStatusRecordList{status-节点状态(撤销时间在这找),createTime-创建节点时间}
            //节点状态字典-values.returnOrderStatusRecordList_statusDict
            //图片凭证-values.returnSubOrderApplyFileRelationList{fileId-凭证图片id}
            //退货退款凭证类型字典(商家、买家)-values.returnSubOrderApplyFileRelationList_typeDict
            //店铺信息-values.storeInfoBase{name-店铺名称}
            JSONObject reasonInfo=values.optJSONObject("returnRefundReason");
            if (reasonInfo!=null) reason=reasonInfo.optString("reason");
            if(getData().type.equals("REFUND")){
                      returnType="退款";
            }else {
                returnType="退货退款";
            }
            initStatusList();
            initTimeList();




        }catch (Exception e){
            LogRui.e("parse####",e);
        }
    }

    private void initTimeList() {
        JSONArray jsonArray=values.optJSONArray("returnOrderStatusRecordList");
        if(jsonArray!=null && jsonArray.length()>0){
            for(int i=0,size=jsonArray.length();i<size;i++){
                String object=jsonArray.optJSONObject(i).optString("status");
                String time=jsonArray.optJSONObject(i).optString("createTime");
                if(object.equals("POST_APPLICATION")) timeList.add("time");
                if(object.equals("BUSINESS_REFUSE_APPLICATION")) timeList.add("time");
                if(object.equals("PLATFORM_INTERVENING")) timeList.add("time");
                if(object.equals("PLATFORM_AGREE_REFUND_APPLICATION")) timeList.add("time");
                if(object.equals("BUYER_RETURN_GOODS")) timeList.add("time");
                if(object.equals("BUSINESS_REFUSE_APPLICATION")) timeList.add("time");
                if(object.equals("PLATFORM_INTERVENING")) timeList.add("time");
                if(object.equals("FINISH")) timeList.add("time");

            }
        }

    }

    private void initStatusList() {
        if(returnType.equals("退款")){
            statusList.add("订单退款中，用户申请退款");
            statusList.add("订单退款中，订单退款中，商家拒绝退款申请");
            statusList.add("订单退款中，用户申请平台介入");
            statusList.add("订单退款成功");
        }else {
            statusList.add("订单退款中，用户申请退货退款");
            statusList.add("订单退款中，商家拒绝退货退款申请");
            statusList.add("订单退款中，用户申请平台介入");
            statusList.add("订单退款中，等待用户退货");
            statusList.add("订单退款中，商家待确认收货");
            statusList.add("订单退款中，商家拒绝退款");
            statusList.add("订单退款中，用户申请平台介入");
            statusList.add("订单退款中，订单退款关闭");

        }

    }
}
