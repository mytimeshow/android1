package cn.czyugang.tcg.client.entity;

/**
 * @author ruiaa
 * @date 2017/12/8
 */

public class TrolleyGoods {

    public Good good;
    public boolean isSelect = true;
    public int num = 0;
    public String spec = "";

    public TrolleyGoods() {
    }

    public TrolleyGoods(Good good, String spec, int num) {
        this.good = good;
        this.num = num;
        this.spec = spec;
    }

    public void add(int addNum) {
        num += addNum;
    }

}
