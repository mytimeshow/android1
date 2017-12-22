package cn.czyugang.tcg.client.modules.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/12/11
 * <p>
 * im主页 通知 + 聊天 列表
 */

public class ImListActivity extends BaseActivity {
    @BindView(R.id.im_list_title_notice)
    TextView titleNotice;
    @BindView(R.id.im_list_title_chat)
    TextView titleChat;
    @BindView(R.id.im_list_viewpager)
    ViewPager viewpager;

    private NoticeFragment noticeFragment;
    private ImMsgFragment msgFragment;

    public static void startImListActivity() {
        Intent intent = new Intent(getTopActivity(), ImListActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_list);
        ButterKnife.bind(this);

        noticeFragment = NoticeFragment.newInstance();
        msgFragment = ImMsgFragment.newInstance();
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(noticeFragment);
        fragments.add(msgFragment);
        viewpager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), fragments));
        viewpager.setOffscreenPageLimit(2);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                noticeFragment.hideTypes();
                switchUI(position == 0);
                LogRui.i("onPageSelected####");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick(R.id.im_list_title_notice)
    public void onShowNotice() {
        if (viewpager.getCurrentItem() == 0) {
            if (noticeFragment.typesIsShowing()) {
                titleNotice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tri_down_red, 0);
                noticeFragment.hideTypes();
            } else {
                titleNotice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tri_up_red, 0);
                noticeFragment.showTypes();
            }
        } else {
            viewpager.setCurrentItem(0);
            switchUI(true);
        }
    }

    @OnClick(R.id.im_list_title_chat)
    public void onShowChatMsg() {
        viewpager.setCurrentItem(1);
        switchUI(false);
    }

    private void switchUI(boolean isNotice) {
        if (isNotice) {
            titleNotice.setTextColor(ResUtil.getColor(R.color.main_red));
            titleNotice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tri_down_red, 0);
            titleChat.setTextColor(ResUtil.getColor(R.color.text_black));
        } else {
            titleNotice.setTextColor(ResUtil.getColor(R.color.text_black));
            titleNotice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tri_down_gray, 0);
            titleChat.setTextColor(ResUtil.getColor(R.color.main_red));
        }
    }

    @OnClick(R.id.title_right)
    public void onSetting() {
        MyDialog.imSettingDialog(this)
                .bindView(myDialog -> {
                    myDialog.onClick(R.id.im_setting,v -> {
                        ImSettingActivity.startImSettingActivity();
                        myDialog.dismiss();
                    });
                    myDialog.onClick(R.id.im_all_read,v -> {
                       myDialog.dismiss();
                    });
                })
                .build()
                .show();
    }

}
