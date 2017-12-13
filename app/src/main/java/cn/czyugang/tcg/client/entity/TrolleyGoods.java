package cn.czyugang.tcg.client.entity;

/**
 * @author ruiaa
 * @date 2017/12/8
 */

public class TrolleyGoods {

    public Good good;
    public String spec = "";
    public double price=0;
    public double packagePrice=0;
    public String storeInventoryId="";

    public boolean isSelect = true;
    public int num = 0;

    public TrolleyGoods() {
    }

    public TrolleyGoods(Good good,int num) {
        this.good = good;
        this.num = num;
        price=good.showPrice;
        packagePrice=good.packagePrice;
        storeInventoryId=good.inventoryId;
    }



    void add(int addNum) {
        num += addNum;
    }

}
