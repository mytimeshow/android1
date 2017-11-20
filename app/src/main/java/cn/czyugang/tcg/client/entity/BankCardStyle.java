package cn.czyugang.tcg.client.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wuzihong on 2017/10/25.
 * 银行卡样式
 */

public class BankCardStyle implements Parcelable {
    private String backIconId;
    private String bankName;
    private String color;
    private String iconId;

    protected BankCardStyle(Parcel in) {
        backIconId = in.readString();
        bankName = in.readString();
        color = in.readString();
        iconId = in.readString();
    }

    public static final Creator<BankCardStyle> CREATOR = new Creator<BankCardStyle>() {
        @Override
        public BankCardStyle createFromParcel(Parcel in) {
            return new BankCardStyle(in);
        }

        @Override
        public BankCardStyle[] newArray(int size) {
            return new BankCardStyle[size];
        }
    };

    public String getBackIconId() {
        return backIconId;
    }

    public void setBackIconId(String backIconId) {
        this.backIconId = backIconId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backIconId);
        dest.writeString(bankName);
        dest.writeString(color);
        dest.writeString(iconId);
    }
}
