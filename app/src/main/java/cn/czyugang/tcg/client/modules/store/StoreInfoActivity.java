package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.ActivityTextView;
import cn.czyugang.tcg.client.widget.FiveStarView;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class StoreInfoActivity extends BaseActivity {

    @BindView(R.id.store_info_title_bg)
    View titleBg;
    @BindView(R.id.store_info_img)
    ImgView img;
    @BindView(R.id.store_info_name)
    TextView name;
    @BindView(R.id.store_info_star)
    FiveStarView star;
    @BindView(R.id.store_info_delivered)
    TextView delivered;
    @BindView(R.id.store_info_activity)
    LinearLayout activities;
    @BindView(R.id.store_info_notice)
    TextView notice;

    private Store store;

    public static void startStoreInfoActivity(Activity activity, Store store) {
        Intent intent = new Intent(activity, StoreInfoActivity.class);
        MyApplication.getInstance().activityTransferData = store;
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        ButterKnife.bind(this);
        store = (Store) MyApplication.getInstance().activityTransferData;
        MyApplication.getInstance().activityTransferData = null;

        titleBg.setBackgroundResource(store.isFoodStore() ? R.color.bg_store_food : R.color.bg_store_good);
        img.id(store.avatarId);
        name.setText(store.name);
        star.setScore((float) (store.score));
        delivered.setText("??? | ???");
        ActivityTextView.create(activities, "?", "??? |   ???",R.dimen.dp_10);
        notice.setText(store.notices);
    }


}
