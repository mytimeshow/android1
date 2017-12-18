package cn.czyugang.tcg.client.utils.rxbus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruiaa
 * @date 2017/12/18
 */

public class DeleteOrderEvent {
    public List<String> orderIds = null;

    public DeleteOrderEvent(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    public DeleteOrderEvent(String orderId) {
        this.orderIds = new ArrayList<>();
        this.orderIds.add(orderId);
    }
}
