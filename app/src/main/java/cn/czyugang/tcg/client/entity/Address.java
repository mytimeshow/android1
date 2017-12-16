package cn.czyugang.tcg.client.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzihong on 2017/10/30.
 */

public class Address implements Parcelable {
    public String address;
    public String area;
    public String building;
    public String city;
    public String coordinates;
    public String defaultAddress;
    public String id;
    public String linkman;
    public String phone;
    public String province;
    public String sex;
    public String street;
    public String tag;
    public String type;//收货地址RECEIVED、跑腿取货地址/买货地址-OBTAIN

    public boolean isDefaultAddress(){
        return defaultAddress.equals("YES");
    }

    public boolean isReceivedAddress(){
        return "RECEIVED".equals(type);
    }

    public String getLinkmanAndPhone(){
        return linkman+"   "+phone;
    }


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

    public static void clearNotReceivedAddress(List<Address> list){
        List<Address> newList=new ArrayList<>();
        for(Address address:list){
            if (address.isReceivedAddress()) newList.add(address);
        }
        list.clear();
        list.addAll(newList);
    }

    public static Address findDefaultAddress(List<Address> list){
        for (Address address:list){
            if (address.isDefaultAddress()) return address;
        }
        return null;
    }
}
