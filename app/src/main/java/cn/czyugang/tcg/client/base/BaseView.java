package cn.czyugang.tcg.client.base;

/**
 * Created by wuzihong on 2017/8/30.
 * View基类
 */

public interface BaseView {
    void showLoadingDialog();

    void dismissLoadingDialog();

    void showToast(String msg);

    void showError(Throwable e);
}
