package cn.czyugang.tcg.client.modules.address.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Address;
import cn.czyugang.tcg.client.modules.address.contract.AddAddressContract;
import cn.czyugang.tcg.client.modules.address.presenter.AddAddressPresenter;
import cn.czyugang.tcg.client.modules.common.dialog.MessageDialog;

/**
 * Created by wuzihong on 2017/10/13.
 * 添加/编辑地址
 */

public class AddAddressActivity extends BaseActivity implements AddAddressContract.View,
        RadioGroup.OnCheckedChangeListener, MessageDialog.OnClickListener {
    public static final String KEY_TYPE = "type";
    public static final String KEY_ADDRESS = "address";
    public static final int TYPE_ADD_ADDRESS = 0;//添加地址
    public static final int TYPE_EDIT_ADDRESS = 1;//编辑地址
    private final int REQUEST_SELECT_LOCATION = 0;
    private final int REQUEST_SELECT_CONTACTS = 1;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.group_sex)
    RadioGroup group_sex;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_area)
    EditText et_area;
    @BindView(R.id.et_street)
    EditText et_street;
    @BindView(R.id.et_address_details)
    EditText et_address_details;
    @BindView(R.id.group_tag)
    RadioGroup group_tag;
    @BindView(R.id.iv_add_tag)
    ImageView iv_add_tag;
    @BindView(R.id.tv_custom_tag)
    TextView tv_custom_tag;
    @BindView(R.id.tv_edit_tag)
    TextView tv_edit_tag;
    @BindView(R.id.et_custom_tag)
    EditText et_custom_tag;
    @BindView(R.id.tv_confirm_tag)
    TextView tv_confirm_tag;

    private AddAddressContract.Presenter mPresenter;

    private int mType;
    private Address mAddress;
    private String mProvince;
    private String mCity;
    private String mAd;
    private double mLat;
    private double mLon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mType = intent.getIntExtra(KEY_TYPE, 0);
        mAddress = intent.getParcelableExtra(KEY_ADDRESS);
        mPresenter = new AddAddressPresenter(this, mAddress == null ? null : mAddress.getId());
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_LOCATION:
                if (resultCode == RESULT_OK && data != null) {
                    PoiItem poiItem = data.getParcelableExtra(SelectLocationActivity.KEY_LOCATION);
                    if (poiItem != null) {
                        et_address.setText(poiItem.getTitle());
                        mProvince = poiItem.getProvinceName();
                        mCity = poiItem.getCityName();
                        mAd = poiItem.getAdName();
                        String area;
                        if (mProvince != null && mProvince.equals(mCity)) {
                            area = mProvince;
                        } else {
                            area = mProvince + mCity + mAd;
                        }
                        et_area.setText(area);
                        et_address_details.setText(area + poiItem.getSnippet());
                        LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                        if (latLonPoint != null) {
                            mLat = latLonPoint.getLatitude();
                            mLon = latLonPoint.getLongitude();
                        }
                    }
                    String street = data.getStringExtra(SelectLocationActivity.KEY_STREET);
                    et_street.setText(street);
                }
                break;
            case REQUEST_SELECT_CONTACTS:
                if (resultCode == RESULT_OK && data != null) {
                    ContentResolver contentResolver = getContentResolver();
                    Cursor idCursor = contentResolver.query(data.getData(), null, null, null, null);
                    if (idCursor != null) {
                        idCursor.moveToFirst();
                        String contactId = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        et_name.setText(idCursor.getString(idCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                        idCursor.close();
                        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                        if (phoneCursor != null) {
                            while (phoneCursor.moveToNext()) {
                                if (phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) == 2) {
                                    et_mobile.setText(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", ""));
                                    phoneCursor.close();
                                    return;
                                }
                            }
                            phoneCursor.moveToFirst();
                            et_mobile.setText(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", ""));
                            phoneCursor.close();
                        }
                    }
                }
                break;
        }
    }

    private void initView() {
        switch (mType) {
            case TYPE_ADD_ADDRESS:
                initAddAddress();
                break;
            case TYPE_EDIT_ADDRESS:
                initEditAddress();
                break;
        }
        //让地址编辑框不能编辑
        et_address.setKeyListener(null);
        //让地区编辑框不能编辑
        et_area.setKeyListener(null);
        //让街道编辑框不能编辑
        et_street.setKeyListener(null);
        group_tag.setOnCheckedChangeListener(this);
    }

    private void initAddAddress() {
        tv_title.setText("新增收货地址");
        tv_delete.setVisibility(View.GONE);
    }

    private void initEditAddress() {
        tv_title.setText("编辑收货地址");
        tv_delete.setVisibility(View.VISIBLE);
        if (mAddress != null) {
            et_name.setText(mAddress.getLinkman());
            et_mobile.setText(mAddress.getPhone());
            group_sex.check(UserApi.MAN.equals(mAddress.getSex()) ? R.id.rbtn_man : R.id.rbtn_woman);
            et_address.setText(mAddress.getBuilding());
            mProvince = mAddress.getProvince();
            mCity = mAddress.getCity();
            mAd = mAddress.getArea();
            if (mProvince != null && mProvince.equals(mCity)) {
                et_area.setText(mProvince);
            } else {
                et_area.setText(mProvince + mCity + mAd);
            }
            et_street.setText(mAddress.getStreet());
            et_address_details.setText(mAddress.getAddress());
            String tag = mAddress.getTag();
            switch (tag) {
                case "公司":
                    group_tag.check(R.id.rbtn_company);
                    break;
                case "家":
                    group_tag.check(R.id.rbtn_home);
                    break;
                case "学校":
                    group_tag.check(R.id.rbtn_school);
                    break;
                default:
                    //隐藏加号图标
                    iv_add_tag.setVisibility(View.INVISIBLE);
                    //显示自定义标签
                    tv_custom_tag.setVisibility(View.VISIBLE);
                    tv_edit_tag.setVisibility(View.VISIBLE);
                    tv_custom_tag.setText(tag);
                    customTagChecked(true);
                    break;
            }
            String location = mAddress.getCoordinates();
            if (!TextUtils.isEmpty(location)) {
                String[] latLon = location.split(",");
                if (latLon.length > 1) {
                    mLat = Double.valueOf(latLon[0]);
                    mLon = Double.valueOf(latLon[1]);
                }
            }
        }
    }

    /**
     * 修改自定义标签选中状态
     *
     * @param checked
     */
    private void customTagChecked(boolean checked) {
        if (checked) {
            tv_custom_tag.setTextColor(getResources().getColor(R.color.main_red));
            tv_custom_tag.setBackgroundResource(R.drawable.bg_address_edit_tag_checked);
            //清除已选标签
            group_tag.setOnCheckedChangeListener(null);
            group_tag.clearCheck();
            group_tag.setOnCheckedChangeListener(this);
        } else {
            tv_custom_tag.setTextColor(getResources().getColor(R.color.text_gray));
            tv_custom_tag.setBackgroundResource(R.drawable.bg_address_edit_tag_normal);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_delete, R.id.fl_select_contacts, R.id.et_address,
            R.id.iv_add_tag, R.id.tv_confirm_tag, R.id.tv_custom_tag, R.id.tv_edit_tag,
            R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_delete:
                MessageDialog.newInstance()
                        .setMessage("确定删除地址吗？")
                        .setNegativeButton("取消")
                        .setPositiveButton("确定")
                        .setOnClickListener(this)
                        .show(getSupportFragmentManager(), "MessageDialog");
                break;
            case R.id.fl_select_contacts:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_SELECT_CONTACTS);
                break;
            case R.id.et_address:
                startActivityForResult(new Intent(context, SelectLocationActivity.class), REQUEST_SELECT_LOCATION);
                break;
            case R.id.iv_add_tag:
                //隐藏加号图标
                iv_add_tag.setVisibility(View.INVISIBLE);
                //显示编辑框
                et_custom_tag.setVisibility(View.VISIBLE);
                tv_confirm_tag.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_confirm_tag:
                //隐藏编辑框
                et_custom_tag.setVisibility(View.INVISIBLE);
                tv_confirm_tag.setVisibility(View.INVISIBLE);
                if (TextUtils.isEmpty(et_custom_tag.getText())) {
                    //编辑框为空时
                    //显示加号图标，清空自定义标签
                    iv_add_tag.setVisibility(View.VISIBLE);
                    tv_custom_tag.setText(null);
                    if (group_tag.getCheckedRadioButtonId() == -1) {
                        //没选中标签，默认选中公司
                        group_tag.check(R.id.rbtn_company);
                    }
                } else {
                    //编辑框不为空
                    //显示自定义标签
                    tv_custom_tag.setVisibility(View.VISIBLE);
                    tv_edit_tag.setVisibility(View.VISIBLE);
                    tv_custom_tag.setText(et_custom_tag.getText());
                }
                break;
            case R.id.tv_custom_tag:
                customTagChecked(true);
                break;
            case R.id.tv_edit_tag:
                //隐藏自定义标签
                tv_custom_tag.setVisibility(View.INVISIBLE);
                tv_edit_tag.setVisibility(View.INVISIBLE);
                //显示编辑框
                et_custom_tag.setVisibility(View.VISIBLE);
                tv_confirm_tag.setVisibility(View.VISIBLE);
                et_custom_tag.setText(tv_custom_tag.getText());
                break;
            case R.id.tv_save:
                String tag;
                switch (group_tag.getCheckedRadioButtonId()) {
                    case R.id.rbtn_company:
                        tag = "公司";
                        break;
                    case R.id.rbtn_home:
                        tag = "家";
                        break;
                    case R.id.rbtn_school:
                        tag = "学校";
                        break;
                    default:
                        tag = tv_custom_tag.getText().toString();
                        break;
                }
                mPresenter.saveAddress(et_name.getText().toString(), et_mobile.getText().toString(),
                        group_sex.getCheckedRadioButtonId() == R.id.rbtn_man ? UserApi.MAN : UserApi.WOMAN,
                        et_address.getText().toString(), mProvince, mCity, mAd, et_street.getText().toString(),
                        et_address_details.getText().toString(), tag, mLat, mLon);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        customTagChecked(false);
    }

    @Override
    public void onClick(MessageDialog dialog, int what) {
        switch (what) {
            case Dialog.BUTTON_POSITIVE:
                mPresenter.deleteAddress();
                break;
        }
    }
}
