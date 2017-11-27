package cn.czyugang.tcg.client.entity;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class Good {



    /*
    *   分类信息
    * */
    public boolean isCategory=false;
    public String categoryStr="";
    public int categoryP=-1;
    public int categoryRedDot=0;

    public static Good createCategory(String categoryStr,int categoryP){
        Good good=new Good();
        good.isCategory=true;
        good.categoryStr=categoryStr;
        good.categoryP=categoryP;
        return good;
    }

}
