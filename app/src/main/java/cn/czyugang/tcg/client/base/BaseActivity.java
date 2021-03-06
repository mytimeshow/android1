package cn.czyugang.tcg.client.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.modules.common.dialog.LoadingDialog;
import cn.czyugang.tcg.client.modules.entry.activity.MainActivity;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.im.UmengUtil;
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

    private volatile int requestLoadingDialogTimes = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);
        context = getApplicationContext();
        UmengUtil.doOnActivityCreate(this);
    }

    public void onBack(){
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.newInstance();
            LogRui.d("showLoadingDialog####  create");
        }
        if (requestLoadingDialogTimes == 0) {
            mLoadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
            LogRui.d("showLoadingDialog####  show");
        }
        requestLoadingDialogTimes++;
    }

    @Override
    public void dismissLoadingDialog() {
        requestLoadingDialogTimes--;
        if (requestLoadingDialogTimes <= 0 && mLoadingDialog != null&&mLoadingDialog.getFragmentManager()!=null) {
            requestLoadingDialogTimes = 0;
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
            LogRui.d("dismissLoadingDialog####");
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

    public static MainActivity clearAllActivityExceptMainAndTop() {
        MainActivity mainActivity = null;
        for (int i = 0, size = activityList.size() - 1; i < size; i++) {
            BaseActivity baseActivity = activityList.get(i);
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

    public static BaseActivity getTopActivity() {
        return activityList.get(activityList.size() - 1);
    }

    public static abstract class NetObserver<T> implements Observer<T> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            LogRui.d("****onSubscribe####");
            getTopActivity().mCompositeDisposable.add(d);
            if (showLoading()) getTopActivity().showLoadingDialog();
        }

        @Override
        public void onNext(T response) {
            LogRui.d("****onNext####");
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogRui.e("onError####",e);
            getTopActivity().showError(e);
            if (showLoading()) getTopActivity().dismissLoadingDialog();
            if (getSwipeToLoadLayout() != null) {
                getSwipeToLoadLayout().setLoadingMore(false);
                getSwipeToLoadLayout().setRefreshing(false);
            }
        }

        @Override
        public void onComplete() {
            LogRui.d("****onComplete####");
            if (showLoading()) getTopActivity().dismissLoadingDialog();
            if (getSwipeToLoadLayout() != null) {
                getSwipeToLoadLayout().setLoadingMore(false);
                getSwipeToLoadLayout().setRefreshing(false);
            }
        }

        public boolean showLoading() {
            return true;
        }

        public SwipeToLoadLayout getSwipeToLoadLayout() {
            return null;
        }
    }
}
