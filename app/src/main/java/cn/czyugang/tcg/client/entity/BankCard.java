package cn.czyugang.tcg.client.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wuzihong on 2017/10/25.
 */

public class BankCard implements Parcelable {
    @SerializedName(value = "id", alternate = "bankId")
    private String id;
    @SerializedName(value = "ownedBank", alternate = "bankName")
    private String ownedBank;
    private String cardType;
    @SerializedName(value = "cardNum", alternate = "bankNo")
    private String cardNum;

    protected BankCard(Parcel in) {
        id = in.readString();
        ownedBank = in.readString();
        cardType = in.readString();
        cardNum = in.readString();
    }

    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel in) {
            return new BankCard(in);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getOwnedBank() {
        return ownedBank;
    }

    public void setOwnedBank(String ownedBank) {
        this.ownedBank = ownedBank;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ownedBank);
        dest.writeString(cardType);
        dest.writeString(cardNum);
    }
}
