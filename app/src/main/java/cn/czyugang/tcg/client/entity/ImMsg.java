package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2018/1/11
 */

public class ImMsg {

    /**
     * ext : {"comment":"string","content":"string","discPrice":"string","fileUrl":"string","good":"string","messageType":"string","objectId":"string","price":"string","subTitle":"string","title":"string"}
     * from : string
     * fromNickname : string
     * msg : {"msg":"string","type":"string"}
     * read : string
     * target : string
     * targetNickname : string
     * target_type : string
     */

    @SerializedName("ext")
    public ExtData ext;
    @SerializedName("from")
    public String from;
    @SerializedName("fromNickname")
    public String fromNickname;
    @SerializedName("msg")
    public MsgData msg;
    @SerializedName("read")
    public String read;
    @SerializedName("target")
    public String target;
    @SerializedName("targetNickname")
    public String targetNickname;
    @SerializedName("target_type")
    public String targetType;

    public static class ExtData {
        /**
         * comment : string
         * content : string
         * discPrice : string
         * fileUrl : string
         * good : string
         * messageType : string
         * objectId : string
         * price : string
         * subTitle : string
         * title : string
         */

        @SerializedName("comment")
        public String comment;
        @SerializedName("content")
        public String content;
        @SerializedName("discPrice")
        public String discPrice;
        @SerializedName("fileUrl")
        public String fileUrl;
        @SerializedName("good")
        public String good;
        @SerializedName("messageType")
        public String messageType;
        @SerializedName("objectId")
        public String objectId;
        @SerializedName("price")
        public String price;
        @SerializedName("subTitle")
        public String subTitle;
        @SerializedName("title")
        public String title;
    }

    public static class MsgData {
        /**
         * msg : string
         * type : string
         */

        @SerializedName("msg")
        public String msg;
        @SerializedName("type")
        public String type;
    }
}
