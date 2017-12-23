package cn.czyugang.tcg.client.modules.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.widget.MultiImgView;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class ImgActivity extends BaseActivity {

    @BindView(R.id.multi_img)
    MultiImgView multiImgView;

    private int position = 0;
    private List<String> imgIds = new ArrayList<>();
    private ImgAdapter imgAdapter;

    public static void startImgActivity(String imgId) {
        Intent intent = new Intent(getTopActivity(), ImgActivity.class);
        intent.putExtra("imgId", imgId);
        getTopActivity().startActivity(intent);
    }

    public static void startImgActivity(ArrayList<String> imgIds, int position) {
        Intent intent = new Intent(getTopActivity(), ImgActivity.class);
        intent.putStringArrayListExtra("imgIds", imgIds);
        intent.putExtra("position", position);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        ButterKnife.bind(this);
        String id = getIntent().getStringExtra("imgId");
        if (id == null) {
            imgIds.addAll(getIntent().getStringArrayListExtra("imgIds"));
        } else {
            imgIds.add(id);
        }
        position = getIntent().getIntExtra("position", 0);

        multiImgView.setShowBigImg(true).setImgIds(imgIds, position);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }
}
