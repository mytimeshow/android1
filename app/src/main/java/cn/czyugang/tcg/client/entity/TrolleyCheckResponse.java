package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/14
 */

public class TrolleyCheckResponse extends Response<List<TrolleyCheckResponse.ErrorStore>> {

    public List<String> errorTrolleyGoods = new ArrayList<>();

    public void parse() {
        try {
            if (values == null) return;

            String errorTrolleyGoodsStr = values.optString("disableSettleShoppingCartIdList");
            if (errorTrolleyGoodsStr.equals(""))
                errorTrolleyGoods.addAll(JsonParse.fromJson(errorTrolleyGoodsStr, new JsonParse.Type(List.class, String.class)));


        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }

    public static class ErrorStore {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
}
