package cn.czyugang.tcg.client.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.modules.common.dialog.LoadingDialog;
import cn.czyugang.tcg.client.modules.entry.activity.MainActivity;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/8/29.
 * Activity基类
 */

public class BaseActivity extends AppCompatActivity implements BaseView {
    private static List<BaseActivity> activityList = new ArrayList<>();
    protected Context context;
    private LoadingDialog mLoadingDialog;
    private Toast mToast;
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
        mCompositeDisposable.dispose();
    }

    public static MainActivity clearAllActivityExceptMain() {
        MainActivity mainActivity = null;
        for (BaseActivity baseActivity : activityList) {
            if (baseActivity instanceof MainActivity) {
                if (mainActivity != null) mainActivity.finish();
                mainActivity = (MainActivity) baseActivity;
            } else {
                baseActivity.finish();
            }
        }
        activityList.clear();
        if (mainActivity != null) activityList.add(mainActivity);
        return mainActivity;
    }

    protected abstract class NetObserver<T> implements Observer<T> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mCompositeDisposable.add(d);
            showLoadingDialog();
        }

        @Override
        public void onNext(T response) {

        }

        @Override
        public void onError(@NonNull Throwable e) {
            showError(e);
            dismissLoadingDialog();
        }

        @Override
        public void onComplete() {
            dismissLoadingDialog();
        }
    }
}
