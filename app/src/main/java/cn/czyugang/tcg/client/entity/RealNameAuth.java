package cn.czyugang.tcg.client.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wuzihong on 2017/10/26.
 * 实名认证
 */

public class RealNameAuth implements Parcelable {
    private String name;
    private String licenceNo;

    protected RealNameAuth(Parcel in) {
        name = in.readString();
        licenceNo = in.readString();
    }

    public static final Creator<RealNameAuth> CREATOR = new Creator<RealNameAuth>() {
        @Override
        public RealNameAuth createFromParcel(Parcel in) {
            return new RealNameAuth(in);
        }

        @Override
        public RealNameAuth[] newArray(int size) {
            return new RealNameAuth[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(licenceNo);
    }
}
