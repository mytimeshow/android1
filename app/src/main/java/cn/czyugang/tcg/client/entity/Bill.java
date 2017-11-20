package cn.czyugang.tcg.client.entity;

import java.util.Date;

/**
 * Created by wuzihong on 2017/10/27.
 * 账单
 */

public class Bill {
    private String id;
    private String actionType;
    private String actionTypeStr;
    private String steamType;
    private String statusStr;
    private double tradePrice;
    private String tradeNo;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionTypeStr() {
        return actionTypeStr;
    }

    public void setActionTypeStr(String actionTypeStr) {
        this.actionTypeStr = actionTypeStr;
    }

    public String getSteamType() {
        return steamType;
    }

    public void setSteamType(String steamType) {
        this.steamType = steamType;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
