package cn.czyugang.tcg.client.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.modules.common.dialog.LoadingDialog;

/**
 * Created by wuzihong on 2017/8/29.
 * Activity基类
 */

public class BaseActivity extends AppCompatActivity implements BaseView {
    protected Context context;
    private LoadingDialog mLoadingDialog;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.newInstance();
        }
        if (!mLoadingDialog.isAdded()) {
            mLoadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissAllowingStateLoss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    @Override
    public void showError(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            showToast(getString(R.string.network_exception));
        } else if (e instanceof UserOAuth.LoginExpiredException) {
            showToast(getString(R.string.login_expired_exception));
        } else {
            showToast(getString(R.string.server_exception));
        }
        e.printStackTrace();
    }
}
