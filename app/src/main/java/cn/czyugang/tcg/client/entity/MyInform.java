package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/8.
 */

/*    创建时间：  "createTime": "2017-12-14T02:16:11.370Z",
      id：  "id": "string",
      资讯id：  "infoId": "string",
      对象id：  "objectId": "string",
      类型：  "type": "string",
      用户id：  "userId": "string"
      */


public class MyInform {

    @SerializedName("type")
    public String type;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;

    //对象id
    @SerializedName("objectId")
    public String objectId;
    //用户id
    @SerializedName("userId")
    public String userId;
    //资讯id
    @SerializedName("infoId")
    public String infoId;


    public String commitNum;
    public String content;
    public String imgUrl;
    public String commitContent;
    public String commitHead;
    public String title="";
    public String replyName; // 回复者名
    public String commitName; // 评论者名
    public String replyContent; // 回复内容



    public void MyInform(){

    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return createTime;
    }

    public void setTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(String commitNum) {
        this.commitNum = commitNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCommitContent() {
        return commitContent;
    }

    public void setCommitContent(String commitContent) {
        this.commitContent = commitContent;
    }

    public String getCommitHead() {
        return commitHead;
    }

    public void setCommitHead(String commitHead) {
        this.commitHead = commitHead;
    }
}
