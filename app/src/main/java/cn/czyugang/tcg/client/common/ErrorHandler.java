package cn.czyugang.tcg.client.common;

import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;

/**
 * @author ruiaa
 * @date 2017/11/27
 */

public class ErrorHandler {

    public static boolean judge200(Object response) {
        if (response == null) {
            LogRui.e("judge200####  服务器异常？");
            return false;
        } else {
            if (response instanceof Response){
                if (((Response) response).getCode() != 200) {
                    AppUtil.toast(((Response) response).getMessage());
                    return false;
                }
            }else if (response instanceof String){
                String code=JsonParse.getStringField((String)response,"code");
                if (!"200".equals(code)){
                    AppUtil.toast(JsonParse.getStringField((String)response,"message"));
                    return false;
                }
            }
        }
        return true;
    }

}
