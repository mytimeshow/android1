package cn.czyugang.tcg.client.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wuzihong on 2017/10/30.
 */

public class Address implements Parcelable {
    private String address;
    private String area;
    private String building;
    private String city;
    private String coordinates;
    private String defaultAddress;
    private String id;
    private String linkman;
    private String phone;
    private String province;
    private String sex;
    private String street;
    private String tag;

    protected Address(Parcel in) {
        address = in.readString();
        area = in.readString();
        building = in.readString();
        city = in.readString();
        coordinates = in.readString();
        defaultAddress = in.readString();
        id = in.readString();
        linkman = in.readString();
        phone = in.readString();
        province = in.readString();
        sex = in.readString();
        street = in.readString();
        tag = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(area);
        dest.writeString(building);
        dest.writeString(city);
        dest.writeString(coordinates);
        dest.writeString(defaultAddress);
        dest.writeString(id);
        dest.writeString(linkman);
        dest.writeString(phone);
        dest.writeString(province);
        dest.writeString(sex);
        dest.writeString(street);
        dest.writeString(tag);
    }
}
