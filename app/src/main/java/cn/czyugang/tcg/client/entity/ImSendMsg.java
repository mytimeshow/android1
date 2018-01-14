package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * @author ruiaa
 * @date 2018/1/11
 */

public class ImSendMsg {

    /**
     * objectId : string
     * sendAccount : string
     * targets : ["string"]
     * type : string
     */

    @SerializedName("objectId")
    public String objectId;
    @SerializedName("type")
    public String type;     //发送的类型 PRODUCT - 商品、INFO - 资讯、 ACTIVITY - 活动

    @SerializedName("msgContent")
    public String msgContent;

    @SerializedName("send")
    public String sendAccount;
    @SerializedName("targets")
    public List<String> targetsAccount;

    public ImSendMsg(String msgContent, String sendAccount, List<String> targetsAccount) {
        this.msgContent = msgContent;
        this.sendAccount = sendAccount;
        this.targetsAccount = targetsAccount;
    }

    public ImSendMsg(String objectId, String type, String sendAccount, List<String> targetsAccount) {
        this.objectId = objectId;
        this.type = type;
        this.sendAccount = sendAccount;
        this.targetsAccount = targetsAccount;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map=new HashMap<>();
        if (objectId!=null) map.put("objectId",objectId);
        if (type!=null) map.put("type",type);

        if (msgContent!=null) map.put("msgContent",msgContent);

        if (sendAccount!=null) map.put("send",sendAccount);
        if (targetsAccount!=null) map.put("targets",targetsAccount);

        return map;
    }
}
