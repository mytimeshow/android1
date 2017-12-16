package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Inform;;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * Created by Administrator on 2017/12/11.
 */

public class InformSelfFollowActivity extends BaseActivity {

    @BindView(R.id.inform_self_follow_fans_list)
    RecyclerView selfFollwList;
    @BindView(R.id.inform_self_follow_fans_title_name)
    TextView tvTitleName;

    static String titleName;

    public static void startInformSelfFollowActivity(String title){
        Intent intent=new Intent(getTopActivity(),InformSelfFollowActivity.class);
        getTopActivity().startActivity(intent);
        titleName=title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_self_follow_fans);
        ButterKnife.bind(this);

        tvTitleName.setText(titleName);
        List<Inform> informColumns=new ArrayList<Inform>();
        Inform informColumn=new Inform();
        informColumn.name=("宇宙无敌大帅比");
        informColumn.isFollow=(false);
        informColumn.followNum=("442455");
        informColumns.add(informColumn);
        Inform informColumn2=new Inform();
        informColumn2.name=("Amshine");
        informColumn2.isFollow=(true);
        informColumn2.followNum=("6746341");
        informColumns.add(informColumn2);
        informColumns.add(informColumn2);
        informColumns.add(informColumn);
        informColumns.add(informColumn);
        informColumns.add(informColumn2);
        SelfFollowFansListAdapter SelfFollowFansListAdapter=new SelfFollowFansListAdapter(informColumns,this);
        selfFollwList.setLayoutManager(new LinearLayoutManager(this));
        selfFollwList.setAdapter(SelfFollowFansListAdapter);
    }

    public static class SelfFollowFansListAdapter extends RecyclerView.Adapter<SelfFollowFansListAdapter.Holder> {
        private List<Inform> list;
        private Activity activity;

        public SelfFollowFansListAdapter(List<Inform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_self_follow_fans ,parent,false));
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Inform data=list.get(position);
            holder.follwFansName.setText(data.name);
            holder.follwFansFollwNum.setText(data.followNum);
            if(data.isFollow){
                holder.isFollow.setText("已关注");
                holder.isFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
            }else{
                holder.isFollow.setText("+关注");
                holder.isFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);
            }


        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class Holder extends RecyclerView.ViewHolder {
            ImgView follwFansHead;
            TextView follwFansName;
            TextView follwFansFollwNum;
            TextView isFollow;

            public Holder(View itemView) {
                super(itemView);
;
                follwFansHead=itemView.findViewById(R.id.inform_self_follow_fans_img);
                follwFansName=itemView.findViewById(R.id.inform_self_follow_fans_name);
                follwFansFollwNum=itemView.findViewById(R.id.inform_self_follow_fans_follownum);
                isFollow=itemView.findViewById(R.id.inform_self_follow_fans_isfollow);



            }
        }
    }

}
