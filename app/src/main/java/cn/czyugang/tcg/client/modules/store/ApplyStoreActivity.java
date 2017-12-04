package cn.czyugang.tcg.client.modules.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.address.activity.SelectLocationActivity;

/**
 * @author ruiaa
 * @date 2017/11/30
 * <p>
 * 入驻申请
 */

public class ApplyStoreActivity extends BaseActivity {
    private static final int REQUEST_SELECT_LOCATION = 10;
    @BindView(R.id.apply_store_merchant_name)
    EditText merchantName;
    @BindView(R.id.apply_store_user_name)
    EditText userName;
    @BindView(R.id.apply_store_user_phone)
    EditText userPhone;
    @BindView(R.id.apply_store_store_name)
    EditText storeName;
    @BindView(R.id.apply_store_store_location_text)
    TextView locationText;
    @BindView(R.id.apply_store_store_detail_location)
    EditText detailLocation;
    @BindView(R.id.apply_store_store_management)
    EditText storeManagement;
    @BindView(R.id.apply_store_merchant_type_personal)
    RadioButton typePersonal;
    @BindView(R.id.apply_store_merchant_type_enterprise)
    RadioButton typeEnterprise;

    private PoiItem poiItem = null;

    public static void startApplyStoreActivity() {
        Intent intent = new Intent(getTopActivity(), ApplyStoreActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.apply_store_store_location)
    public void onSelectLocation() {
        SelectLocationActivity.startSelectLocationActivity(this, REQUEST_SELECT_LOCATION,
                "入驻申请", "请输入店铺位置/小区/大厦/学校");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == RESULT_OK && data != null) {
            poiItem = SelectLocationActivity.getLocation(data);
            if (poiItem == null) return;
            locationText.setText(poiItem.getTitle());
            String mProvince = poiItem.getProvinceName();
            String mCity = poiItem.getCityName();
            String mAd = poiItem.getAdName();
            String area;
            if (mProvince != null && mProvince.equals(mCity)) {
                area = mProvince;
            } else {
                area = mProvince + mCity + mAd;
            }
            detailLocation.setText(area + poiItem.getSnippet());
        }
    }


    private boolean checkInput(){
        if (merchantName.getText().length()==0){
            showToast("请输入商户名称");
            return false;
        }
        if(!typeEnterprise.isChecked()&&!typePersonal.isChecked()){
            showToast("请选择商户类型");
            return false;
        }
        if (userName.getText().length()==0){
            showToast("请输入联系人");
            return false;
        }
        if (userPhone.getText().length()==0){
            showToast("请输入联系电话");
            return false;
        }
        if (storeName.getText().length()==0){
            showToast("请输入店铺名称");
            return false;
        }
        if (poiItem==null){
            showToast("请选择店铺地址");
            return false;
        }
        if (detailLocation.getText().length()==0){
            showToast("请输入店铺详细地址");
            return false;
        }
        if (storeManagement.getText().length()==0){
            showToast("简单描述店铺经营内容");
            return false;
        }
        return true;
    }

    @OnClick(R.id.apply_store_commit)
    public void onCommit() {
        //if (!checkInput()) return;
        Toast.makeText(context, "提交成功", Toast.LENGTH_LONG).show();
        storeManagement.postDelayed(() -> {
            ApplyStoreStatusActivity.startApplyStoreStatusActivity();
            finish();
        }, 3000L);
    }
}
