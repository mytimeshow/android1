package cn.czyugang.tcg.client.modules.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/12/1
 */

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.Holder> {
    private List<String> list;
    private Activity activity;
    private int width = 100;
    private int height = 100;
    private int rightMargin = 20;
    private int srcType = 1;//1:后台图片id，2:本地文件

    public ImgAdapter(List<String> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        setSizeRes(R.dimen.dp_60);
        setRightMargin(R.dimen.dp_10);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(activity).inflate(
                R.layout.view_img, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String data = list.get(position);
        holder.imgView.id(data);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ImgAdapter setWidth(int width) {
        this.width = width;
        return this;
    }

    public ImgAdapter setHeight(int height) {
        this.height = height;
        return this;
    }

    public ImgAdapter setSrcType(int srcType) {
        this.srcType = srcType;
        return this;
    }

    public ImgAdapter setSize(int size) {
        width = height = size;
        return this;
    }

    public ImgAdapter setSizeRes(@DimenRes int sizeRes) {
        width = height = ResUtil.getDimenInPx(sizeRes);
        return this;
    }

    public ImgAdapter setRightMargin(@DimenRes int rightMargin) {
        this.rightMargin = ResUtil.getDimenInPx(rightMargin);
        return this;
    }

    public static void bind(Context context, RecyclerView recyclerView, ImgAdapter adapter, int spanCount) {
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount));
        }
        recyclerView.setAdapter(adapter);
    }

    class Holder extends RecyclerView.ViewHolder {
        ImgView imgView;

        public Holder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.view_img);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
            lp.width = width;
            lp.height = height;
            lp.rightMargin = rightMargin;
            imgView.setLayoutParams(lp);
        }
    }
}
