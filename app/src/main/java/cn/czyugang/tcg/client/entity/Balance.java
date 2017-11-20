package cn.czyugang.tcg.client.entity;

/**
 * Created by wuzihong on 2017/10/25.
 * 余额
 */

public class Balance {
    private double cashPrice;
    private double freezeCashPrice;
    private double uncashPrice;
    private double freezeUncashPrice;

    public double getCashPrice() {
        return cashPrice;
    }

    public void setCashPrice(double cashPrice) {
        this.cashPrice = cashPrice;
    }

    public double getFreezeCashPrice() {
        return freezeCashPrice;
    }

    public void setFreezeCashPrice(double freezeCashPrice) {
        this.freezeCashPrice = freezeCashPrice;
    }

    public double getUncashPrice() {
        return uncashPrice;
    }

    public void setUncashPrice(double uncashPrice) {
        this.uncashPrice = uncashPrice;
    }

    public double getFreezeUncashPrice() {
        return freezeUncashPrice;
    }

    public void setFreezeUncashPrice(double freezeUncashPrice) {
        this.freezeUncashPrice = freezeUncashPrice;
    }
}
