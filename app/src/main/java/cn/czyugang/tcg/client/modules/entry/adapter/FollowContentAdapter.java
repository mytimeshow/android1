package cn.czyugang.tcg.client.modules.entry.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BasePageAdapter;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.entity.FollowCotent;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FollowContentAdapter extends BaseRecyclerAdapter {


    public FollowContentAdapter(Context context){
        super(context);
    }

   /* @Override
    protected BasePageAdapter.ViewHolder onCreate(ViewGroup parent) {
        return new BasePageAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_follow_list, parent, false));
    }*/

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int position) {

    }
     class ViewHolder extends BaseRecyclerAdapter.ViewHolder{
        @BindView(R.id.follow_list_head)
        ImgView followHead;
        @BindView(R.id.follow_list_name)
        TextView followName;
        @BindView(R.id.follw_list_commit_num)
        TextView followCommitNum;
        @BindView(R.id.follw_list_thumb_num)
        TextView followThumbNum;
        @BindView(R.id.follow_list_content)
        TextView followContent;
        @BindView(R.id.follow_list_img)
        ImgView followImg;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, itemView);
         }

    }




}

