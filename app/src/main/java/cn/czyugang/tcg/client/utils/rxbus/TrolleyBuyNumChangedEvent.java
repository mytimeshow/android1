package cn.czyugang.tcg.client.utils.rxbus;

import cn.czyugang.tcg.client.entity.Good;

/**
 * @author ruiaa
 * @date 2017/12/9
 */

public class TrolleyBuyNumChangedEvent {
    public Good good=null;

    public TrolleyBuyNumChangedEvent() {
    }

    public TrolleyBuyNumChangedEvent(Good good) {
        this.good = good;
    }
}
