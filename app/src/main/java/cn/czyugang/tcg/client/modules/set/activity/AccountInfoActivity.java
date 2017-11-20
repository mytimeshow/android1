package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.address.activity.AddressManageActivity;
import cn.czyugang.tcg.client.modules.common.dialog.UploadPhotoDialog;
import cn.czyugang.tcg.client.modules.set.contract.AccountInfoContract;
import cn.czyugang.tcg.client.modules.set.dialog.SelectDateDialog;
import cn.czyugang.tcg.client.modules.set.dialog.SelectSexDialog;
import cn.czyugang.tcg.client.modules.set.presenter.AccountInfoPresenter;
import cn.czyugang.tcg.client.utils.string.DateFormat;
import cn.czyugang.tcg.client.utils.DictUtil;
import cn.czyugang.tcg.client.utils.img.ImageLoader;

/**
 * Created by wuzihong on 2017/9/30.
 * 账号信息
 */

public class AccountInfoActivity extends BaseActivity implements AccountInfoContract.View,
        SelectSexDialog.OnSelectSexListener, SelectDateDialog.OnSelectDateListener,
        UploadPhotoDialog.OnUploadPhotoListener {
    @BindView(R.id.fresco_avatar)
    SimpleDraweeView fresco_avatar;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;

    private AccountInfoContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        ButterKnife.bind(this);
        mPresenter = new AccountInfoPresenter(this);
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            UserBase userBase = userInfo.getUserBase();
            if (userBase != null) {
                //昵称
                tv_name.setText(userBase.getNickname());
                //性别
                tv_sex.setText(DictUtil.lookupStaticDict(userInfo.getSexDict(), userBase.getSex()));
            }
            UserDetail userDetail = userInfo.getUserDetail();
            if (userDetail != null) {
                //头像
                ImageLoader.loadImageToView(userDetail.getFileId(), fresco_avatar);
                //生日
                tv_birthday.setText(DateFormat.formatDate(userDetail.getBirthday()));
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.cl_modify_avatar, R.id.fl_name, R.id.fl_sex, R.id.fl_birthday,
            R.id.tv_delivery_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cl_modify_avatar:
                UploadPhotoDialog.newInstance()
                        .setCropSize(500, 500)
                        .setOnUploadPhotoListener(this)
                        .show(getSupportFragmentManager(), "UploadPhotoDialog");
                break;
            case R.id.fl_name: {
                Intent intent = new Intent(context, ModifyNicknameActivity.class);
                UserInfo userInfo = mPresenter.getUserInfo();
                if (userInfo != null) {
                    UserBase userBase = userInfo.getUserBase();
                    if (userBase != null) {
                        intent.putExtra(ModifyNicknameActivity.KEY_NICKNAME, userBase.getNickname());
                    }
                }
                startActivity(intent);
                break;
            }
            case R.id.fl_sex:
                SelectSexDialog.newInstance()
                        .setOnSelectSexListener(this)
                        .show(getSupportFragmentManager(), "SelectSexDialog");
                break;
            case R.id.fl_birthday:
                SelectDateDialog.newInstance()
                        .setOnSelectDateListener(this)
                        .show(getSupportFragmentManager(), "SelectDateDialog");
                break;
            case R.id.tv_delivery_address:
                startActivity(new Intent(context, AddressManageActivity.class));
                break;
        }
    }

    @Override
    public void onSelectSex(SelectSexDialog dialog, int sex) {
        switch (sex) {
            case SelectSexDialog.MAN:
                mPresenter.modifySexToMan();
                break;
            case SelectSexDialog.WOMAN:
                mPresenter.modifySexToWoman();
                break;
        }
    }

    @Override
    public void onSelectDate(SelectDateDialog dialog, Date date) {
        mPresenter.modifyBirthday(date);
    }

    @Override
    public void onUploadPhoto(Uri uri) {
        mPresenter.uploadAvatar(new File(uri.getPath()));
    }
}
