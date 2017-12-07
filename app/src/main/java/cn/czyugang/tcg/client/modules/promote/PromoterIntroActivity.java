package cn.czyugang.tcg.client.modules.promote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.BannerImgLoader;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;

/**
 * @author ruiaa
 * @date 2017/12/6
 *
 *  推广有礼
 */

public class PromoterIntroActivity extends BaseActivity {
    @BindView(R.id.promote_banner)
    Banner banner;
    @BindView(R.id.promote_invite_register)
    TextView inviteRegister;
    @BindView(R.id.promote_intro)
    ImgView introImg;

    public static void startPromoterIntroActivity() {
        Intent intent = new Intent(getTopActivity(), PromoterIntroActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promoter_intro);
        ButterKnife.bind(this);

        //banner.setImageLoader(new BannerImgLoader()).setImages(images).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppKeyStorage.isFirstOpenPromoterIntro()) {
            inviteRegister.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    inviteRegister.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    showIntroGuide();
                }
            });
        }
    }

    @OnClick(R.id.promote_invite_register)
    public void onInvite() {
        MyDialog.showAllShare(this, v -> {
            switch (v.getId()) {
                case R.id.view_share_wechat:{
                    break;
                }
                case R.id.view_share_wechat_circle:{
                    break;
                }
                case R.id.view_share_qq:{
                    break;
                }
                case R.id.view_share_qzone:{
                    break;
                }
                case R.id.view_share_sina_blog:{
                    break;
                }
                case R.id.view_share_scan:{
                    break;
                }
            }
        });
    }

    @OnClick(R.id.promote_become)
    public void onBecome() {
        BecomePromoterActivity.startBecomePromoterActivity();
    }

    @OnClick(R.id.promote_goods)
    public void onGoods() {
        PromoteGoodListActivity.startPromoteGoodListActivity();
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.title_right)
    public void onProfit() {
        PromoterProfitActivity.startPromoterProfitActivity();
    }

    private void showIntroGuide() {
        int[] location = new int[2];
        inviteRegister.getLocationInWindow(location);
        final int top = location[1] - AppUtil.getStatusBarHeight();
        LogRui.i("showIntroGuide####", top);
        MyDialog.Builder.newBuilder(this)
                .custom(R.layout.view_promote_intro_guide)
                .width(WindowManager.LayoutParams.MATCH_PARENT)
                .height(WindowManager.LayoutParams.MATCH_PARENT)
                .bgAlpha(0)
                .bindView(myDialog -> {
                    myDialog.onClick(R.id.view_promote_intro_know);
                    View anchor = myDialog.rootView.findViewById(R.id.view_anchor);
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) anchor.getLayoutParams();
                    lp.topMargin = top;
                    anchor.setLayoutParams(lp);
                })
                .build()
                .show();
        AppKeyStorage.saveHadOpenPromoterIntro();
    }
}
