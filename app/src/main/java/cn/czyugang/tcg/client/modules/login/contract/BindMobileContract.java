package cn.czyugang.tcg.client.modules.login.contract;

/**
 * Created by wuzihong on 2017/9/23.
 * 绑定手机的Contract
 */

public interface BindMobileContract {
    interface View extends InputMobileContract.View, InputVerificationCodeContract.View {
        void showMobileBinding();

        void gotoInputMobile();

        void gotoInputVerificationCode();

        void finish();
    }

    interface Presenter extends InputMobileContract.Presenter,
            InputVerificationCodeContract.Presenter {
    }
}
