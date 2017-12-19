package cn.czyugang.tcg.client.entity;

import org.json.JSONObject;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by wuzihong on 2017/9/11.
 * 下载上传进度
 */
public class Progress {
    private long progress;
    private long total;
    private String bodyStr;

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getBodyStr() {
        return bodyStr;
    }

    public void setBodyStr(String bodyStr) {
        this.bodyStr = bodyStr;
    }

    public String getFileId(){
        if (bodyStr==null) return "";
        try{
            JSONObject jsonObject=new JSONObject(bodyStr);
            jsonObject=jsonObject.getJSONObject("data");
            return jsonObject.getString("id");
        }catch (Exception e){
            LogRui.e("getFileId####",e);
        }
        return "";
    }
}
