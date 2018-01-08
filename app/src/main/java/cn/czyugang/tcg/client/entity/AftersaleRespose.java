package cn.czyugang.tcg.client.entity;

import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class AftersaleRespose extends Response<List<Aftersale>> {

    public Map<String,String> statusDict;
    public Map<String,String> typeDict;
    public Map<String,String> orderStatusDict;
    public Map<String,String> refundStatusDict;

    public void parse(){
        if (values==null) return;
        try {
            //退货退款申请信息-data{type-售后类型,status-售后状态,handleResult-商家处理结果}

            //退货退款状态字典-statusDict
            statusDict= JsonParse.parseMap(values,"statusDict");

            //售后类型-typeDict    REFUND    RETURN_ALL
            typeDict=JsonParse.parseMap(values,"typeDict");

            //是否申请了平台介入字典-interventionFlagDict  ‘YES’-是,’NO’-否
            //商家处理结果字典4-handleResultDict        AGREE:同意,REFUSE-拒绝,UNTREATED:未处理


            //店铺名称-values.idToStoreNameDict#id={data.id} {name-店铺名称}
            Map<String,String> storeName=JsonParse.parseMap(values,"idToStoreNameDict");

            //商品名称-values.idToProductNameDict#id={data.id} {name-商品名称}
            Map<String,String> productName=JsonParse.parseMap(values,"idToProductNameDict");

            //商品原价-values.idToProductUnitPriceDict#id={data.id} {name-商品原价}
            Map<String,Double> productUnitPrice=JsonParse.parseMapDouble(values,"idToProductUnitPriceDict");

            //商品实价-values.idToProductRealPriceDict#id={data.id} {name-商品实价}
            Map<String,Double> productRealPrice=JsonParse.parseMapDouble(values,"idToProductRealPriceDict");

            //商品规格属性-values.idToProductAttributeDict#id={data.id} {name-商品规格属性}
            Map<String,String> productAttribute=JsonParse.parseMap(values,"idToProductAttributeDict");

            //商品数目-values.idToProductNumberDict#id={data.id} {name-商品数目}
            Map<String,Integer> productNumber=JsonParse.parseMapInt(values,"idToProductNumberDict");

            //商品订单状态-values.idToOrderStatusDict#id={data.id}
            Map<String,String> orderStatus=JsonParse.parseMap(values,"idToOrderStatusDict");

            //退款状态-values.refundStatusOf{data.id}

            //values.subOrderDetailIdToPicId#id={data.subOrderDetailId} {name-图片id}
            Map<String,String> productPic=JsonParse.parseMap(values,"subOrderDetailIdToPicId");

            for(Aftersale aftersale:data){
                aftersale.storeName=storeName.get(aftersale.id);
                aftersale.productName=productName.get(aftersale.id);
                aftersale.productUnitPrice=productUnitPrice.get(aftersale.id);
                aftersale.productRealPrice=productRealPrice.get(aftersale.id);
                aftersale.productAttribute=productAttribute.get(aftersale.id);
                aftersale.productNumber=productNumber.get(aftersale.id);
                aftersale.orderStatus=orderStatus.get(aftersale.id);
                aftersale.refundStatus=values.optString("refundStatusOf"+aftersale.id);
                aftersale.orderPic=productPic.get(aftersale.subOrderDetailId);
            }
        }catch (Exception e){
            LogRui.e("parse####",e);
        }
    }
}
