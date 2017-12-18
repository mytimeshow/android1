package cn.czyugang.tcg.client.entity;

import java.util.List;

/**
 * Created by wuzihong on 2017/10/20.
 * 用户信息实体类
 */

public class UserInfo {
    private UserBase userBase;
    private UserDetail userDetail;
    private String totalBalance;
    private int totalScore;
    private int totalFetchCode;
    private String isBindWECHAT;
    private String isBindQQ;
    private String isBindWEIBO;
    private List<StaticDict> sexDict;


    /*
    *       内存保存
    * */
    private static UserInfo instance=null;

    public static void setInstance(UserInfo instance) {
        UserInfo.instance = instance;
    }

    public static String getUserPhotoId(){
       if (instance==null) return "";
       return instance.userBase.getNickname();
    }

    public static String getUserNickName(){
        if (instance==null) return "";
        return instance.userDetail.getFileId();
    }

    public UserBase getUserBase() {
        return userBase;
    }

    public void setUserBase(UserBase userBase) {
        this.userBase = userBase;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalFetchCode() {
        return totalFetchCode;
    }

    public void setTotalFetchCode(int totalFetchCode) {
        this.totalFetchCode = totalFetchCode;
    }

    public String getIsBindWECHAT() {
        return isBindWECHAT;
    }

    public void setIsBindWECHAT(String isBindWECHAT) {
        this.isBindWECHAT = isBindWECHAT;
    }

    public String getIsBindQQ() {
        return isBindQQ;
    }

    public void setIsBindQQ(String isBindQQ) {
        this.isBindQQ = isBindQQ;
    }

    public String getIsBindWEIBO() {
        return isBindWEIBO;
    }

    public void setIsBindWEIBO(String isBindWEIBO) {
        this.isBindWEIBO = isBindWEIBO;
    }

    public List<StaticDict> getSexDict() {
        return sexDict;
    }

    public void setSexDict(List<StaticDict> sexDict) {
        this.sexDict = sexDict;
    }
}
