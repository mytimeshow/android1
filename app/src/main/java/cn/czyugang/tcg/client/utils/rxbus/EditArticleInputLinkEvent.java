package cn.czyugang.tcg.client.utils.rxbus;

/**
 * @author ruiaa
 * @date 2018/1/14
 */

public class EditArticleInputLinkEvent {

    public String title;
    public String link;

    public EditArticleInputLinkEvent(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
