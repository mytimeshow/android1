package cn.czyugang.tcg.client.api;

/**
 * @author ruiaa
 * @date 2017/12/23
 */

public class DiscountApi {

    /*
    *   降价拍
    * */
    //api/auth/v1/marketing/reduce/detail [可接入-v3]商家降价拍活动商品详情  //activityProductId  活动商品id(列表data.id)

    //api/auth/v1/marketing/reduce/detailInfo [可接入-v3]降价拍活动商品详情  //activityProductId  活动商品id

    //api/auth/v1/marketing/reduce/group/detail [可接入-v3]获取降价拍团详情  //activityGroupId  活动团id

    //api/auth/v1/marketing/reduce/product [可接入-v3]获取降价拍商品      //分页    status 当前推广CURRENT,历史推广HISTORY      name 商品名称


    /*
    *   扫码购
    * */
    // api/auth/v3/marketing/store/qrcode/activity/get［可接入-v3］查看活动详情

    // api/auth/v3/marketing/store/qrcode/activity/list［可接入-v3］查询扫码购优惠活动

    // api/auth/v3/marketing/store/qrcode/activity/related/delivery [可接入-v3] 关联订单(物流方式)

    // api/auth/v3/marketing/store/qrcode/activity/related/direct [可接入-v3] 关联订单(直接购买)


    /*
    *   优惠券
    * */
    // api/auth/v1/marketing/user/coupon/pay/platform/coupon/list [Doc-v3]选择平台优惠券（类型：可用，不可用）分页

    // api/auth/v1/marketing/user/coupon/pay/store/coupon/list  [Doc-v3]选取商家优惠券（类型：可用，不可用）分页

    // api/auth/v1/marketing/user/coupon/pay/user/coupon/list      [Doc-v3]用户可使用/不可用优惠券列表(付款时候)分页

    // api/auth/v1/marketing/user/coupon/platform/activity/list      [Doc-v3]选择平台活动

    // api/auth/v1/marketing/user/coupon/store/activity/list      [Doc-v3]选取店铺活动

    // api/auth/v1/marketing/user/coupon/store/coupon/list      [Doc-v3]店铺领券列表分页

    // api/auth/v1/marketing/user/coupon/user/coupon/list      [Doc-v3]我的优惠券（类型：未使用，已使用，已过期）分页

    // api/auth/v1/marketing/user/coupon/voucher/center      [Doc-v3]领券中心列表分页
}
