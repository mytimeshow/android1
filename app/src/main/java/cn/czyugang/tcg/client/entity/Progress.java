package cn.czyugang.tcg.client.entity;

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
}
