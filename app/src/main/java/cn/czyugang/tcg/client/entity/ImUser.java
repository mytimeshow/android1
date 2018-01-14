package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2018/1/11
 */

public class ImUser {

    /**
     * account : string
     * baseId : string
     * faceUrl : string
     * nickname : string
     * tel : string
     * type : string
     */
    @SerializedName("account")
    public String account;
    @SerializedName("baseId")
    public String baseId;  //基础id userId、storeId、managerId
    @SerializedName("faceUrl")
    public String faceUrl;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("tel")
    public String tel;
    @SerializedName("type")
    public String type;
    @SerializedName("last")
    public String last;
}
