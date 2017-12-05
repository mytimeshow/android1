package cn.czyugang.tcg.client.entity;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FollowCotent {
    //资讯-->关注列表-->内容

    String headUrl;
    String name;
    String content;
    String imgUrl;
    String commentNum;
    String thumbNum;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getThumbNum() {
        return thumbNum;
    }

    public void setThumbNum(String thumbNum) {
        this.thumbNum = thumbNum;
    }

    public void FollowCotent(){


    }
    public void FollowCotent(String headUrl,String name,String imgUrl,String commentNum,String thumbNum){
        this.headUrl=headUrl;
        this.name=name;
        this.imgUrl=imgUrl;
        this.commentNum=commentNum;
        this.thumbNum=thumbNum;

    }
}
