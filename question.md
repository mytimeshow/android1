+   12/15 11:05:24      --15:41:15

        /api/auth/v1/product/shopping/shoppingCart/preSettle 结算购物车预加载接口
        哪部分信息是与（addressId 地址id）+（deliveryWays 配送方式）关联的？
        是不是用户每更换地址、配送方式都要调用一次？

        很多信息会重新计算
        每次修改调用一次


+   12/15 11:09:53      --15:44:27

        data
            deliverysEnable=SL,PL,E,S
            isStoreLogistics=YES
        value.storeIdToDeliveryTypeDict
            name=PLATFORM,FETCH
        商家配送不可用是因为时间或者位置不在范围内吗？


        按value.storeIdToDeliveryTypeDict的
        data里面的可能有问题

+   12/15 11:15:37      --14:55:36

        配送时间是平台配送和商家配送通用的吗？

        是的

+   12/15 15:16:52      --15:29:39

        尽快送达

        只要 deliveryTimeOfId 第一个时间是今天的就显示尽快送达选项

+   12/15 15:49:18      --15:49:18

        默认配送方式和地址

        地址是基于用户的默认地址，如果没有默认地址就是用户地址列表的第一条数据。这个地址与初次进入结算界面那个地址是一致的 。
        配送方式默认为可用配送方式的第一个。

+   12/15 15:56:22      --15:56:22

        配送方式（storeId-deliveryType-deliveryTime组合列表），假如两个店铺，是这样子吗？
        918003175762620416-PLATFORM-2017-12-15 10:25:55,918003175762620416-PLATFORM-2017-12-15 10:25:55

        是的

+   12/15 16:59:22      --16:59:22

        /api/auth/v1/product/shopping/shoppingCart/preSettle
        规格
        values.storeInventoryIdToAttributeStringDict
        values.idToCustomTagValueDict
        标签
        values.tagOfShoppingCart{cartOf.id}

        是的