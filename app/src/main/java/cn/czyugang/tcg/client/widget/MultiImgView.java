package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.img.GlideApp;
import cn.czyugang.tcg.client.utils.img.ImageLoader;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class MultiImgView extends FrameLayout {
    public MultiImgView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MultiImgView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiImgView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private ViewPager viewPager;
    private TextView nums;
    private int position = 0;
    private List<String> imgIds = new ArrayList<>();
    private ImgAdapter imgAdapter;

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_multi_img, this, true);
        viewPager = findViewById(R.id.view_multi_img_pager);
        nums = findViewById(R.id.view_multi_img_nums);

        imgAdapter = new ImgAdapter();
        viewPager.setAdapter(imgAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                nums.setText((position + 1) + "/" + imgIds.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public int getImgNums(){
        return imgIds.size();
    }

    public void setImgIds(List<String> ids) {
        imgIds.clear();
        imgIds.addAll(ids);
        imgAdapter.notifyDataSetChanged();
        nums.setText((position + 1) + "/" + imgIds.size());
    }

    public void setImgIds(List<String> ids, int p) {
        imgIds.clear();
        imgIds.addAll(ids);
        imgAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(p);
        position = p;
        nums.setText((position + 1) + "/" + imgIds.size());
    }

    private class ImgAdapter extends PagerAdapter {

        HashMap<Integer, View> viewHashMap = new HashMap<>();

        private void createView(int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_img, null);
            View imgLoading = view.findViewById(R.id.img_loading);
            ImageView imgImg = view.findViewById(R.id.img_img);
            if (getWidth()!=0&&getHeight()!=0){
                GlideApp.with(getContext())
                        .load(ImageLoader.getImageUrl(imgIds.get(position), getWidth(), getHeight()))
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
            }else {
                getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        GlideApp.with(getContext())
                                .load(ImageLoader.getImageUrl(imgIds.get(position), getWidth(), getHeight()))
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
                    }
                });
            }

            viewHashMap.put(position, view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (!viewHashMap.containsKey(position)) createView(position);
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
            return view == object;
        }
    }
}
