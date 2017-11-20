package cn.czyugang.tcg.client.utils.app;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by froger_mcs on 21/03/16.
 */
public class KeyboardWatcher {

    private Activity activity;
    private View rootView;
    private OnKeyboardToggleListener onKeyboardToggleListener;
    private ViewTreeObserver.OnGlobalLayoutListener viewTreeObserverListener;

    public KeyboardWatcher(Activity activity) {
        this.activity = activity;
        initialize();
    }

    public void setListener(OnKeyboardToggleListener onKeyboardToggleListener) {
        this.onKeyboardToggleListener = onKeyboardToggleListener;
    }

    public void destroy() {
        if (rootView != null)
            if (Build.VERSION.SDK_INT >= 16) {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(viewTreeObserverListener);
            } else {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(viewTreeObserverListener);
            }
        activity=null;
        rootView=null;
        onKeyboardToggleListener=null;
        viewTreeObserverListener=null;
    }

    private void initialize() {
        if (hasAdjustResizeInputMode()) {
            viewTreeObserverListener = new GlobalLayoutListener();
            rootView =activity.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(viewTreeObserverListener);
        } else {
            throw new IllegalArgumentException(String.format("Activity %s should have windowSoftInputMode=\"adjustResize\"" +
                    "to make KeyboardWatcher working. You can set it in AndroidManifest.xml", activity.getClass().getSimpleName()));
        }
    }

    private boolean hasAdjustResizeInputMode() {
        return (activity.getWindow().getAttributes().softInputMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) != 0;
    }

    private class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int initialValue;
        boolean hasSentInitialAction;
        boolean isKeyboardShown;

        @Override
        public void onGlobalLayout() {
            if (initialValue == 0) {
                initialValue = rootView.getHeight();
            } else {
                if (initialValue > rootView.getHeight()) {
                    if (onKeyboardToggleListener != null) {
                        if (!hasSentInitialAction || !isKeyboardShown) {
                            isKeyboardShown = true;
                            onKeyboardToggleListener.onKeyboardShown(initialValue - rootView.getHeight());
                        }
                    }
                } else {
                    if (!hasSentInitialAction || isKeyboardShown) {
                        isKeyboardShown = false;
                        rootView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (onKeyboardToggleListener != null) {
                                    onKeyboardToggleListener.onKeyboardClosed();
                                }
                            }
                        });
                    }
                }
                hasSentInitialAction = true;
            }
        }
    }

    public interface OnKeyboardToggleListener {
        void onKeyboardShown(int keyboardSize);

        void onKeyboardClosed();
    }
}
