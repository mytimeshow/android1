package cn.czyugang.tcg.client.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by wuzihong on 2017/9/24.
 */

public class RxBus {
    private static Subject<Object> mSubject = PublishSubject.create().toSerialized();

    public static <T> Observable<T> getObservable(Class<T> classType) {
        return mSubject.ofType(classType)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void post(Object o) {
        mSubject.onNext(o);
    }
}
