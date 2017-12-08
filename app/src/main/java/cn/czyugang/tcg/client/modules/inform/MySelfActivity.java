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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.MyInform;
import cn.czyugang.tcg.client.modules.store.SearchActivity;
import cn.czyugang.tcg.client.modules.store.SearchResultActivity;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MySelfActivity extends BaseActivity {

    @BindView(R.id.inform_for_myself_list)
    RecyclerView informForMyselfList;
   
    public static void startMySelfActivity( ){
        Intent intent=new Intent(getTopActivity(),MySelfActivity.class);
        getTopActivity().startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        ButterKnife.bind(this);

        List<MyInform> myInforms=new ArrayList<MyInform>();
        MyInform myInform=new MyInform();
        myInform.setCommitNum("46121");
        myInform.setType("发表了文章");
        myInform.setContent("哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或");
        MyInform myInform2=new MyInform();
        myInform2.setCommitNum("7685446");
        myInform2.setType("赞同了评论");
        myInform2.setContent("内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");

        myInforms.add(myInform);
        myInforms.add(myInform2);
        myInforms.add(myInform);
        myInforms.add(myInform);
        myInforms.add(myInform);
        myInforms.add(myInform2);
        myInforms.add(myInform2);
        myInforms.add(myInform);
        myInforms.add(myInform);


        MyInformAdapter myInformAdapter=new MyInformAdapter(myInforms,this);
        informForMyselfList.setLayoutManager(new LinearLayoutManager(this));
        informForMyselfList.setAdapter(myInformAdapter);
    }



    @OnClick(R.id.img_search)
    public void onSearch(){
        SearchActivity.startSearchActivity();
    }

    static class MyInformAdapter extends RecyclerView.Adapter<MyInformAdapter.Holder> {
        private List<MyInform> list;
        private Activity activity;

        public MyInformAdapter(List<MyInform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_for_myself ,parent,false));
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            MyInform data=list.get(position);
            holder.informContent.setText(data.getContent());
            holder.informCommitNum.setText(data.getCommitNum());
            holder.informType.setText(data.getType());
            if(data.getType().equals("赞同了评论")){
                holder.commit.setVisibility(View.VISIBLE);
            }else{
                holder.commit.setVisibility(View.GONE);
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
            ImgView informImg;
            TextView informType;
            TextView informTime;
            TextView informContent;
            TextView informCommitNum;

            //评论详情部分
            RelativeLayout commit;
            ImgView commitHead;
            TextView commitContent;

            public Holder(View itemView) {
                super(itemView);
                informImg=itemView.findViewById(R.id.inform_for_myself_img);
                informType=itemView.findViewById(R.id.inform_for_myself_type);
                informTime=itemView.findViewById(R.id.inform_for_myself_time);
                informContent=itemView.findViewById(R.id.inform_for_myself_content);
                informCommitNum=itemView.findViewById(R.id.inform_for_myself_commit_content_num);
                //评论详情部分
                commit=itemView.findViewById(R.id.inform_for_myself_commit_content);
                commitHead=itemView.findViewById(R.id.inform_for_myself_commit_head);
                commitContent=itemView.findViewById(R.id.inform_for_myself_commit_contentmsg);


            }
        }
    }

}
