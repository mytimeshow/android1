package cn.czyugang.tcg.client.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruiaa
 * @date 2017/12/9
 */

public class GoodsSpec {
    public String name="";
    public List<String> labels=new ArrayList<>();
    transient public String selectLabel="";

    public GoodsSpec() {
        name="kkk";
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");

        selectLabel=labels.get(0);
    }
}
