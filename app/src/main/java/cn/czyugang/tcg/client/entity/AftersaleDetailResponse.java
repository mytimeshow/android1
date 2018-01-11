package cn.czyugang.tcg.client.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2018/1/8
 */

public class AftersaleDetailResponse extends Response<Aftersale> {
    private static final String TAG = "AftersaleDetailResponse";
    public String returnType="";
    public String buyerReason="";
    public String buyerExplian="";
    public String sellerReason1="";
    public String sellerExplian1="";
    public String sellerReason2="";
    public String sellerExplian2="";
    public List<String> buyerImgList=new ArrayList<>();
    public List<String> sellerImgList1=new ArrayList<>();
    public List<String> sellerImgList2=new ArrayList<>();
    public List<String> statusList=new ArrayList<>();
    public List<String> timeList=new ArrayList<>();
    public List<Integer> imgList=new ArrayList<>();
    public List<String> titleList=new ArrayList<>();

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
            if (reasonInfo!=null) buyerReason=reasonInfo.optString("reason");

            JSONObject reasonand=values.optJSONObject("returnSubOrderApplyDetail");
            if (reasonand!=null) {
                buyerExplian=reasonand.optString("supplement");
                sellerExplian1=reasonand.optString("businessSupplement");
                sellerExplian2=reasonand.optString("secondBusinessSupplement");
                sellerReason1=reasonand.optString("shopReason");
                sellerReason2=reasonand.optString("secondShopReason");
            }
            if(getData().type.equals("REFUND")){
                      returnType="退款";
            }else {
                returnType="退货退款";
            }
            initStatusList();
            initTimeList();
            initImgList();
            initstatusTitleList();
            initGoodsImg();
        }catch (Exception e){
            LogRui.e("parse####",e);
        }
    }

    private void initGoodsImg() {
        JSONArray array=values.optJSONArray("returnSubOrderApplyFileRelationList");
        if(array!=null && array.length()>0){
            for(int i=0,size=array.length();i<size;i++){
                String type=array.optJSONObject(i).optString("type");
                String imgId=array.optJSONObject(i).optString("fileId");
                int times=array.optJSONObject(i).optInt("time");
                if(type!=null && type.equals("BUYER")){
                    buyerImgList.add(imgId);

                }else if(type!=null && type.equals("BUSINESS") && times==1){
                    sellerImgList1.add(imgId);

                }else {
                    sellerImgList2.add(imgId);

                }
            }

        }
    }

    private void initstatusTitleList() {
        Log.e(TAG, "initstatusTitleList: 1" );
        JSONObject jsonObject=values.optJSONObject("returnGoodsInfo");
        Log.e(TAG, "initstatusTitleList:2" );
        String conpanyName="";
        String dillNum="";
        if(jsonObject!=null)
         conpanyName=jsonObject.optString("logisticsName");
        Log.e(TAG, "initstatusTitleList: 3" );
        if(jsonObject!=null)
         dillNum=jsonObject.optString("logisticsOrder");
        Log.e(TAG, "initstatusTitleList: 4" );
        if(returnType.equals("退款")){
            Log.e(TAG, "initstatusTitleList:5" );
            titleList.add("退款申请:商品   "+values.optString("productTitle")+"   X"+values.optString("productNumber"));
            Log.e(TAG, "initstatusTitleList:6" );
            titleList.add("商家拒绝了退款申请，您可联系客服介入");
            titleList.add("客服正在处理中，请耐心等待处理结果哦~|");
            titleList.add("经平台仲裁，此次退款申请予以通过");
            Log.e(TAG, "initstatusTitleList: 退款" );
        }else {
            titleList.add("退货退款申请:商品   "+values.optString("productTitle")+"   X"+values.optString("productNumber"));
            titleList.add("商家拒绝了退货退款申请，仍需退货退款可申请介入");
            titleList.add("客服正在处理中，请耐心等待处理结果哦~|");
            titleList.add("经平台仲裁同意了您的退货退款申请，请尽快将您商\n" +
                    "品寄回");
            titleList.add("物流快递："+conpanyName+"快递 \n"+"运单号："+dillNum);
            titleList.add("商家拒绝了退款申请，仍需退款可申请介入");
            titleList.add("客服正在处理中，请耐心等待处理结果哦~|");
            titleList.add("经平台仲裁，此次退款申请予以通过");
            Log.e(TAG, "initstatusTitleList: 退款退款  " );
        }


    }

    private void initImgList() {
        if(returnType.equals("退款")){
            imgList.add(R.drawable.icon_refound_grey);
            imgList.add(R.drawable.icon_refound_grey);
            imgList.add(R.drawable.icon_refound_grey);
            imgList.add(R.drawable.icon_refund);
        }else {
            imgList.add(R.drawable.icon_refund_return_grey);
            imgList.add(R.drawable.icon_refund_return_grey);
            imgList.add(R.drawable.icon_refund_return_grey);
            imgList.add(R.drawable.icon_refund_return_grey);
            imgList.add(R.drawable.icon_refound_grey);
            imgList.add(R.drawable.icon_refound_grey);
            imgList.add(R.drawable.icon_refound_grey);
            imgList.add(R.drawable.icon_refund_return);

        }
    }

    private void initTimeList() {
        JSONArray jsonArray=values.optJSONArray("returnOrderStatusRecordList");
        if(jsonArray!=null && jsonArray.length()>0){

            for(int i=0,size=jsonArray.length();i<size;i++){
                String object=jsonArray.optJSONObject(i).optString("status");
                String time=jsonArray.optJSONObject(i).optString("createTime");
                if(object.equals("POST_APPLICATION")) timeList.add(time);
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
            statusList.add("订单退款中，商家拒绝退款申请");
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
