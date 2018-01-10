package cn.czyugang.tcg.client.utils.im;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author ruiaa
 * @date 2018/1/10
 */
public class MqUserInfo {

    /**
     * clientId : string
     * password : string
     * topic : ["string"]
     * username : string
     */

    @SerializedName("clientId")
    public String clientId = null;
    @SerializedName("password")
    public String password = null;
    @SerializedName("username")
    public String username = null;
    @SerializedName("topic")
    public List<String> topic = null;
}
