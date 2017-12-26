package cn.czyugang.tcg.client.modules.groupon;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class GroupListBean{


    /**
     * currentPrice : 0
     * id : string
     * name : string
     * restTime : 0
     */

    public int currentPrice;
    public String id;
    public String name;
    public int restTime;

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }



}
