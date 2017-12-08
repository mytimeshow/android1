package cn.czyugang.tcg.client.entity;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MyInform {

    String type;
    String time;
    String commitNum;
    String content;
    String imgUrl;
    String commitContent;
    String commitHead;

    public void MyInform(){

    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
