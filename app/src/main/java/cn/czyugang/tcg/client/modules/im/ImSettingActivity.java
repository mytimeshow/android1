package cn.czyugang.tcg.client.modules.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/12/21
 * <p>
 * 消息与提醒
 */

public class ImSettingActivity extends BaseActivity {
    @BindView(R.id.im_setting_receive_all)
    ImageView receiveAll;
    @BindView(R.id.im_setting_notice_new)
    SelectButton noticeNew;
    @BindView(R.id.im_setting_receive_praise)
    ImageView receivePraise;
    @BindView(R.id.im_setting_receive_tip)
    TextView receiveTip;
    @BindView(R.id.im_setting_voice)
    SelectButton voice;
    @BindView(R.id.im_setting_vibrate)
    SelectButton vibrate;
    @BindView(R.id.im_setting_headphone)
    SelectButton headphone;
    @BindView(R.id.im_setting_enter_send)
    SelectButton enterSend;
    @BindView(R.id.im_setting_delete_record)
    TextView deleteRecord;

    private boolean isReceiveAll=true;

    public static void startImSettingActivity() {
        Intent intent = new Intent(getTopActivity(), ImSettingActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.im_setting_notice_closeL)
    public void onCloseNotice() {
        ImSettingNoticeActivity.startImSettingNoticeActivity();
    }

    @OnClick(R.id.im_setting_receive_allL)
    public void onReceiveAll(){
        receiveAll.setVisibility(View.VISIBLE);
        receivePraise.setVisibility(View.GONE);
        receiveTip.setText("接受所有通知并进行提醒");
        isReceiveAll=true;
    }

    @OnClick(R.id.im_setting_receive_praiseL)
    public void onReceivePraise(){
        receiveAll.setVisibility(View.GONE);
        receivePraise.setVisibility(View.VISIBLE);
        receiveTip.setText("资讯类的点赞通知会出现在消息列表中，但你不会收到此类消息的消息提醒，其他通知正常接收和提醒");
        isReceiveAll=false;
    }
}
