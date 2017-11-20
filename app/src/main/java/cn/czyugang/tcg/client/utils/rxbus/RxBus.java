package cn.czyugang.tcg.client.utils.rxbus;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by ruiaa on 2017/7/18.
 */

public class RxBus {
    private static volatile RxBus mInstance;
    private final Subject<Object> bus;
    private HashMap<Object, CompositeDisposable> mSubscriptionMap;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送事件
     *
     * @param o
     */
    public static void post(Object o) {
        getInstance().bus.onNext(o);
    }

    public static  void postDelay(Object o, long delayTime) {
        Observable.just(true)
                .delay(delayTime, TimeUnit.MILLISECONDS)
                .subscribe(aBoolean -> {
                    getInstance().bus.onNext(o);
                });
    }

    /**
     * 返回指定类型的Observable实例
     *
     * @param type
     * @param
     * @return
     */
    public static <T> Observable<T> toObservable(Class<T> type) {
        return getInstance().bus.ofType(type);
    }

    public static <T> Observable<T> getObservable(Class<T> type) {
        return getInstance().bus.ofType(type);
    }

    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public static boolean hasObservers() {
        return getInstance().bus.hasObservers();
    }

/*    *//**
     * 一个默认的订阅方法
     *
     * @param type
     * @param next
     * @param error
     * @param
     * @return
     *//*
    public Subscription doSubscribe(Class type, Action1 next, Action1 error) {
        return_arrow_white tObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);
    }*/

    /**
     * 保存订阅后的subscription
     * @param type
     * @param disposable
     */
    public <T> void addDisposable(Class<T> type, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = type.getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            CompositeDisposable compositeDisposable=new CompositeDisposable();
            compositeDisposable.add(disposable);
            mSubscriptionMap.put(key, compositeDisposable);
        }
    }

    /**
     * 取消订阅
     * @param type
     */
    public <T> void unDispose(Class<T> type) {
        if (mSubscriptionMap == null) {
            return;
        }

        String key = type.getName();
        if (!mSubscriptionMap.containsKey(key)){
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }

        mSubscriptionMap.remove(key);
    }
}
