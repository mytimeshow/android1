package cn.czyugang.tcg.client.modules.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.utils.img.GlideApp;
import cn.czyugang.tcg.client.utils.img.ImageLoader;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class ImgActivity extends BaseActivity {

    @BindView(R.id.img_pager)
    ViewPager pager;
    private int position=0;
    private List<String> imgIds=new ArrayList<>();
    private ImgAdapter imgAdapter;

    public static void startImgActivity(String imgId) {
        Intent intent = new Intent(MyApplication.getContext(), ImgActivity.class);
        intent.putExtra("imgId", imgId);
        MyApplication.getContext().startActivity(intent);
    }

    public static void startImgActivity(ArrayList<String> imgIds, int position) {
        Intent intent = new Intent(MyApplication.getContext(), ImgActivity.class);
        intent.putStringArrayListExtra("imgIds", imgIds);
        intent.putExtra("position", position);
        MyApplication.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        ButterKnife.bind(this);
        String id=getIntent().getStringExtra("imgId");
        if(id==null) {
            imgIds.addAll(getIntent().getStringArrayListExtra("imgIds"));
        }else {
            imgIds.add(id);
        }
        position=getIntent().getIntExtra("position",0);

        imgAdapter=new ImgAdapter();
        pager.setAdapter(imgAdapter);
        pager.setOffscreenPageLimit(3);
        pager.setCurrentItem(position);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    private class ImgAdapter extends PagerAdapter{

        HashMap<Integer,View> viewHashMap=new HashMap<>();

        private void createView(int position){
            View view= LayoutInflater.from(ImgActivity.this).inflate(R.layout.fragment_img,null);
            View imgLoading=view.findViewById(R.id.img_loading);
            ImageView imgImg=view.findViewById(R.id.img_img);
            GlideApp.with(ImgActivity.this)
                    .load(ImageLoader.getImageUrl(imgIds.get(position)))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imgLoading.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imgLoading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgImg);
            viewHashMap.put(position,view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(!viewHashMap.containsKey(position)) createView(position);
            container.addView(viewHashMap.get(position));
            return viewHashMap.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewHashMap.get(position));
        }

        @Override
        public int getCount() {
            return imgIds.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}
