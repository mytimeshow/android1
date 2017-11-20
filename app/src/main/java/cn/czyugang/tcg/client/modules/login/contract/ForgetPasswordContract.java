package cn.czyugang.tcg.client.modules.login.contract;

/**
 * Created by wuzihong on 2017/9/23.
 * 找回密码的Contract
 */

public interface ForgetPasswordContract {
    interface View extends InputMobileContract.View, InputVerificationCodeContract.View,
            SetPasswordSucceedContract.View {
        void showMobileUnregistered();

        void gotoInputMobile();

        void gotoInputVerificationCode();

        void gotoSetPassword();

        void gotoSetPasswordSucceed();

        void finish();
    }

    interface Presenter extends InputMobileContract.Presenter,
            InputVerificationCodeContract.Presenter, SetPasswordContract.Presenter {
    }
}
