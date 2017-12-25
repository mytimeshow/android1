店铺
商超
#餐饮
#商品详情
店内购物车
预下单
下单结算
#支付

订单列表
订单详情
#订单物流追踪，地图

商家入驻

售后申请
#售后列表       --  后台下单接口有问题 等待
#售后详情


购物搜索
#筛选信息没数据返回

#扫码 二维码编解码规范没出

#支付 已出支付宝和工行        -- 后台下单接口有问题 等待

商品详情
#评价 介绍 规格

收藏 足迹




未处理接口：
-------------------------------------------------------------------------------------------------------------
全站购物车
店铺-餐饮模板
支付
订单物流追踪，地图
售后详情，售后追踪

三期：
推广员：收入报表，商品清单       芝吟28号
营销工具：所有活动、折扣、优惠券
降价拍（团购降价）               章文30号
二维码优惠（扫码购）

四期：
跑腿
界面与广告
消息中心
版本控制
IM系统

五期：
营销工具（邀请有奖）
积分系统
评价管理



            扫码购订单控制器
            /api/auth/v3/order/qrcode/activity/product 查看活动商品详情
            /api/auth/v3/order/qrcode/activity/products 查询扫码购优惠活动商品信息列表
            /api/auth/v3/order/qrcode/activity/pre/confirm  确认订单数据加载
            /api/auth/v3/order/qrcode/activity/place/by/delivery 下单(走配送)
            /api/auth/v3/order/qrcode/activity/place/in/store 下单(到店)"


            "营销工具服务模块（外部接口）
            降价拍控制器ReducePriceAuthController
            GET/api/auth/v1/marketing/reduce/detailInfo[DOC-v3]降价拍活动商品详情
            GET/api/auth/v1/marketing/reduce/group/detail[DOC-v3]获取降价拍团详情
            GET/api/auth/v2/order/reduce/price/preSettle[DOC-v3]结算预加载
            POST/api/auth/v2/order/reduce/price/settle[DOC-v3]降价拍订单结算
            商品服务模块（外部接口）
            shopping-controller-impl购物控制器
            GET/api/auth/v1/product/shopping/getAttributes［可接入］根据商品店铺库存id获取规格、标签属性我的订单、订单详情属于二期新增字段部分
            订单服务模块（外部接口）
            商品订单控制器
            GET/api/auth/v2/order/product/myOrder[可接入-v2]我的订单
            GET/api/auth/v2/order/product/orderDetail[可接入-v2]订单详情


            "营销内部模块-推广员控制器
            /api/auth/v3/marketing/promoter/pre 预加载
            /api/auth/v3/marketing/promoter/share/register 邀请注册分享
            /api/auth/v3/marketing/promoter/share/product 商品推广分享
            /api/auth/v3/marketing/promoter/submit 提交申请成为推广员
            /api/auth/v3/marketing/promoter/get/report 获取报表
            /api/auth/v3/marketing/promoter/get/product 查看推广商品信息
            /api/auth/v3/marketing/promoter/page/product 分页查询推广商品
            /api/auth/v3/marketing/promoter/page/order 报表订单分页查询
            /api/auth/v3/marketing/promoter/get/code[可接入-v3]获取推广员用户推广码
            推广员申请流程：
            外部推广员
            /api/auth/v3/marketing/promoter/submit [可接入-v3]提交申请成为推广员
            后台推广员管理
            /api/manage/v3/marketing/promoter/page/application[可接入-v3]分页查询推广员审核申请
            /api/manage/v3/marketing/promoter/get/application[可接入-v3]查看推广员申请信息
            /api/manage/v3/marketing/promoter/update/application[可接入-v3]审核推广员申请
            /api/manage/v3/marketing/promoter/page/promoter[可接入-v3]分页查询推广员
            /api/manage/v3/marketing/promoter/get/promoter[可接入-v3]查看推广员信息
            /api/manage/v3/marketing/promoter/update/status[可接入-v3]修改推广员状态"




            /api/auth/v1/marketing/user/coupon/pay/platform/coupon/list[Doc-v3]选择平台优惠券（类型：可用，不可用）分页
            /api/auth/v1/marketing/user/coupon/pay/store/coupon/list[Doc-v3]选取商家优惠券（类型：可用，不可用）分页
            /api/auth/v1/marketing/user/coupon/pay/user/coupon/list[Doc-v3]用户可使用/不可用优惠券列表(付款时候)分页
            /api/auth/v1/marketing/user/coupon/platform/activity/list[Doc-v3]选择平台活动
            /api/auth/v1/marketing/user/coupon/store/activity/list[Doc-v3]选取店铺活动
            /api/auth/v1/marketing/user/coupon/store/coupon/list[Doc-v3]店铺领券列表分页
            /api/auth/v1/marketing/user/coupon/user/coupon/list[Doc-v3]我的优惠券（类型：未使用，已使用，已过期）分页
            /api/auth/v1/marketing/user/coupon/voucher/center[Doc-v3]领券中心列表分页"




