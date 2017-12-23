package cn.czyugang.tcg.client.entity;

import java.util.Date;

/**
 * Created by wuzihong on 2017/9/29.
 * 用户详细信息实体类
 */

public class UserDetail {
    private String id;
    private String userId;
    private String provinceId;
    private String cityId;
    private String areaId;
    private String fileId;
    private String identityCardId;
    private Date birthday;
    private String realnameAuth;
    private String setPassword;
    private String setPayPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getIdentityCardId() {
        return identityCardId;
    }

    public void setIdentityCardId(String identityCardId) {
        this.identityCardId = identityCardId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRealnameAuth() {
        return realnameAuth;
    }

    public void setRealnameAuth(String realnameAuth) {
        this.realnameAuth = realnameAuth;
    }

    public String getSetPassword() {
        return setPassword;
    }

    public void setSetPassword(String setPassword) {
        this.setPassword = setPassword;
    }

    public String getSetPayPassword() {
        return setPayPassword;
    }

    public boolean hadSetPayPassword(){
        return "YES".equals(setPayPassword);
    }

    public void setSetPayPassword(String setPayPassword) {
        this.setPayPassword = setPayPassword;
    }
}
