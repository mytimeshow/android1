package cn.czyugang.tcg.client.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wuzihong on 2017/10/28.
 */

public class BankCardInfo implements Parcelable {
    private BankCard bankCard;
    private BankCardStyle bankCardStyle;

    public BankCardInfo() {

    }

    protected BankCardInfo(Parcel in) {
        bankCard = in.readParcelable(BankCard.class.getClassLoader());
        bankCardStyle = in.readParcelable(BankCardStyle.class.getClassLoader());
    }

    public static final Creator<BankCardInfo> CREATOR = new Creator<BankCardInfo>() {
        @Override
        public BankCardInfo createFromParcel(Parcel in) {
            return new BankCardInfo(in);
        }

        @Override
        public BankCardInfo[] newArray(int size) {
            return new BankCardInfo[size];
        }
    };

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public BankCardStyle getBankCardStyle() {
        return bankCardStyle;
    }

    public void setBankCardStyle(BankCardStyle bankCardStyle) {
        this.bankCardStyle = bankCardStyle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bankCard, flags);
        dest.writeParcelable(bankCardStyle, flags);
    }
}
