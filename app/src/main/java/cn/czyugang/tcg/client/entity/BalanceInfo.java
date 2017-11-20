package cn.czyugang.tcg.client.entity;

/**
 * Created by wuzihong on 2017/10/25.
 * 资产信息
 */

public class BalanceInfo {
    private Balance balance;
    private int bankCardCount;

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public int getBankCardCount() {
        return bankCardCount;
    }

    public void setBankCardCount(int bankCardCount) {
        this.bankCardCount = bankCardCount;
    }
}
