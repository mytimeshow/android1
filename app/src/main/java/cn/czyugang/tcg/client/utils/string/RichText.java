package cn.czyugang.tcg.client.utils.string;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;

import java.util.regex.Pattern;

import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.utils.app.ResUtil;

import static android.text.style.DynamicDrawableSpan.ALIGN_BASELINE;

/**
 * Created by ruiaa on 2017/10/26.
 */
public class RichText {

    public SpannableStringBuilder builder;

    public RichText() {
        builder = new SpannableStringBuilder();
    }

    public RichText(CharSequence text) {
        builder = new SpannableStringBuilder(text);
    }

    public static RichText newRichText() {
        return new RichText();
    }

    public static RichText newRichText(CharSequence text) {
        return new RichText(text);
    }

    public boolean contain(CharSequence text) {
        if (text == null) return true;
        String source = builder.toString();
        return source.contains(text);
    }

    public boolean containOrAppend(CharSequence text) {
        if (text == null) return true;
        String source = builder.toString();
        if (!source.contains(text)) {
            builder.append(text);
            return false;
        }
        return true;
    }

    public RichText append(@StringRes int id) {
        builder.append(ResUtil.getString(id));
        return this;
    }

    public RichText append(CharSequence text) {
        if (text == null) return this;
        builder.append(text);
        return this;
    }

    public RichText appendSp(CharSequence text, float sizeSp) {
        if (text == null) return this;
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new AbsoluteSizeSpan(ResUtil.sp2px(sizeSp)), 0, text.length(), 0);
        builder.append(spannableString);
        return this;
    }

    public RichText appendSpRes(CharSequence text, @DimenRes int size) {
        if (text == null) return this;
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new AbsoluteSizeSpan(ResUtil.getDimenInPx(size)), 0, text.length(), 0);
        builder.append(spannableString);
        return this;
    }


    public RichText appendColor(CharSequence text, int color) {
        if (text == null) return this;
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
        builder.append(spannableString);
        return this;
    }

    public RichText appendColorRes(CharSequence text, @ColorRes int colorId) {
        return appendColor(text, ResUtil.getColor(colorId));
    }

    public RichText appendSpColor(CharSequence text, float sizeSp, int color) {
        if (text == null) return this;
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new AbsoluteSizeSpan(ResUtil.sp2px(sizeSp)), 0, text.length(), 0);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
        builder.append(spannableString);
        return this;
    }

    public RichText appendSpColorRes(CharSequence text, @DimenRes int size, @DimenRes int color) {
        if (text == null) return this;
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new AbsoluteSizeSpan(ResUtil.getDimenInPx(size)), 0, text.length(), 0);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
        builder.append(spannableString);
        return this;
    }


    public RichText setSizeInPx(CharSequence text, int sizePx) {
        if (text == null) return this;
        containOrAppend(text);
        String source = builder.toString();
        int startIndex = source.indexOf(text.toString());
        int endIndex = startIndex + text.length();
        builder.setSpan(new AbsoluteSizeSpan(sizePx), startIndex, endIndex, 0);
        return this;
    }

    public RichText setSizeInSp(Object text, float sizeSp) {
        if (text == null) return this;
        return setSizeInPx(text.toString(), ResUtil.sp2px(sizeSp));
    }

    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_BOLD_ITALIC = 3;
    public final static int STYLE_UNDERLINE = 12;
    public final static int STYLE_STRIKETHROUGH = 13;

    public RichText setStyle( int style) {
        String source = builder.toString();
        int startIndex = 0;
        int endIndex = source.length();
        Object span;
        if (style == STYLE_UNDERLINE) {
            span = new UnderlineSpan();
        } else if (style == STYLE_STRIKETHROUGH) {
            span = new StrikethroughSpan();
        } else {
            span = new StyleSpan(style);
        }
        builder.setSpan(span, startIndex, endIndex, 0);
        return this;
    }

    public RichText setStyle(CharSequence text, int style) {
        if (text == null) return this;
        containOrAppend(text);
        String source = builder.toString();
        int startIndex = source.indexOf(text.toString());
        int endIndex = startIndex + text.length();
        Object span;
        if (style == STYLE_UNDERLINE) {
            span = new UnderlineSpan();
        } else if (style == STYLE_STRIKETHROUGH) {
            span = new StrikethroughSpan();
        } else {
            span = new StyleSpan(style);
        }
        builder.setSpan(span, startIndex, endIndex, 0);
        return this;
    }

    public RichText setStyleRes(CharSequence text, @StringRes int style) {
        if (text == null) return this;
        containOrAppend(text);
        String source = builder.toString();
        int startIndex = source.indexOf(text.toString());
        int endIndex = startIndex + text.length();
        builder.setSpan(new TextAppearanceSpan(MyApplication.getContext(), style), startIndex, endIndex, 0);
        return this;
    }

    public RichText addimg(int start, int end, @DrawableRes int img) {
        if (start < 0 || start >= end) return this;
        if (builder.toString().length() < end) return this;
        ImageSpan imageSpan=new ImageSpan(MyApplication.getContext(),img,ALIGN_BASELINE);
        builder.setSpan(imageSpan,start,end,0);
        return this;
    }

    public RichText addimg(int start, int end, @DrawableRes int img, int width, int height) {
        if (start < 0 || start >= end) return this;
        if (builder.toString().length() < end) return this;
        Drawable drawable=ResUtil.getDrawable(img);
        drawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
        ImageSpan imageSpan=new ImageSpan(drawable,ALIGN_BASELINE);
        builder.setSpan(imageSpan,start,end,0);
        return this;
    }

    public RichText addimgRes(int start, int end, @DrawableRes int img, @DimenRes int size) {
        size=(int)(ResUtil.getDimenInPx(size));
        return addimg(start, end, img,size,size);
    }

    public CharSequence build() {
        return builder.subSequence(0, builder.length());
    }


    public static CharSequence setFormatSizeSp(CharSequence text, float sizeSp) {
        return setFormatSizePx(text, ResUtil.sp2px(sizeSp));
    }

    public static CharSequence setFormatSizePx(CharSequence text, float sizePx) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new AbsoluteSizeSpan((int) sizePx), 0, text.length(), 0);
        return spannableString.subSequence(0, spannableString.length());
    }

    public static CharSequence setColor(CharSequence text, @ColorRes int colorRes) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(ResUtil.getColor(colorRes)), 0, text.length(), 0);
        return spannableString.subSequence(0, spannableString.length());
    }

    public static String clearWhite(String s) {
        if (s == null || s.isEmpty()) return "";
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        return p.matcher(s).replaceAll("");
    }

    public static SpannableString format(String text, @StyleRes int style) {
        if (text == null) {
            return null;
        }
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(MyApplication.getContext(), style), 0, text.length(), 0);
        return spannableString;
    }


}
