package cn.czyugang.tcg.client.entity;

/**
 * Created by wuzihong on 2017/10/27.
 */

public class BillStatus {
    private String fundSteamId;
    private String statusText;

    public String getFundSteamId() {
        return fundSteamId;
    }

    public void setFundSteamId(String fundSteamId) {
        this.fundSteamId = fundSteamId;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
