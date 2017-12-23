package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/22.
 */

public class UserFansFollow {

    /**
     * fansCount : 0
     * isFollow : string
     * userFileId : string
     * userId : string
     * userName : string
     */

    @SerializedName("fansCount")
    public int fansCount;
    @SerializedName("isFollow")
    public String isFollow;
    @SerializedName("userFileId")
    public String userFileId;
    @SerializedName("userId")
    public String userId;
    @SerializedName("userName")
    public String userName;

}
