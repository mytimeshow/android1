package cn.czyugang.tcg.client.modules.scan;

import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.common.WebActivity;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;

/**
 * @author ruiaa
 * @date 2017/12/23
 */

public class ScanResultHandler {

    public static boolean deal(String code, BaseActivity activity){
        isScanDiscountGoods(code);
        return false;
    }

    //普通商品二维码
    private static void isCommon(String code){
        ScanResultActivity.startScanResultActivity(code);
    }

    //外部链接
    private static void isOutsideUrl(final String url, BaseActivity activity) {
        MyDialog.Builder.newBuilder(activity)
                .contentStr("检测到此链接为外部链接，\n打开外部链接可能存在安全隐患，\n请注意您的个人隐私的保护哦~\n" + url)
                .onNegativeButton()
                .positiveButton("打开链接")
                .onPositiveButton(myDialog -> {
                    myDialog.dismiss();
                    BaseActivity.clearAllActivityExceptMain();
                    WebActivity.startWebActivity(activity, url);
                })
                .build()
                .show();
    }

    //折扣 抢购商品
    private static void isScanDiscountGoods(String result){
        ScanGoodsDetailActivity.startScanGoodsDetailActivity(result);
    }

    //到店
    private static void isScanStore(String result){
        ScanStoreActivity.startScanStoreActivity();
    }
}
