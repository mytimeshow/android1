package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class GoodCategory {

    /**
     * createTime : 2017-11-21T10:21:22.129Z
     * deleteFlag : string
     * id : string
     * level : string
     * name : string
     * order : 0
     * parentId : string
     * picId : string
     * remark : string
     * storeId : string
     * updateTime : 2017-11-21T10:21:22.129Z
     */

    @SerializedName("createTime")
    public String createTime;
    @SerializedName("updateTime")
    public String updateTime;

    @SerializedName("deleteFlag")
    public String deleteFlag;

    @SerializedName("storeId")
    public String storeId;
    @SerializedName("id")
    public String id;
    @SerializedName("parentId")
    public String parentId;     //父分类id
    @SerializedName("name")
    public String name;

    @SerializedName("order")
    public int order;       //排序序号

    @SerializedName("level")
    public String level;
    @SerializedName("picId")
    public String picId;
    @SerializedName("remark")
    public String remark;

    public List<GoodCategory> secondCategoryList=null;

    public String toString(){
        return name+"#"+remark+"  "+(secondCategoryList==null?"":"  子"+secondCategoryList.toString());
    }

    public List<String> getSeconds(){
        if (secondCategoryList==null) return null;
        List<String> seconds=new ArrayList<>();
        for(GoodCategory category:secondCategoryList){
            seconds.add(category.name);
        }
        return seconds;
    }

    public String getSecondCategoryId(String name){
        if (secondCategoryList==null) return null;
        for(GoodCategory category:secondCategoryList){
            if (category.name.equals(name)) return category.id;
        }
        return null;
    }
}
