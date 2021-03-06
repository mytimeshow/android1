package cn.czyugang.tcg.client.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.modules.common.dialog.LoadingDialog;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by wuzihong on 2017/8/30.
 * Fragment基类
 */

public class BaseFragment extends Fragment implements BaseView {
    protected Context context;
    protected BaseActivity activity;
    protected View rootView;
    private LoadingDialog mLoadingDialog;
    private Toast mToast;
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (BaseActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.newInstance();
        }
        if (!mLoadingDialog.isAdded()) {
            mLoadingDialog.show(getChildFragmentManager(), "LoadingDialog");
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

    public String getLabel(){
        return getClass().getName();
    }
    public void setText(String text){

    }
    public void getCommetNum(int count){

    }
}
