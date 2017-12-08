package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.os.Bundle;
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
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/24
 */



public class InformColumnFragment extends BaseFragment {

    @BindView(R.id.inform_column_list)
    RecyclerView informColumnList;

    public static InformColumnFragment newInstance() {
        InformColumnFragment fragment = new InformColumnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inform_column, container, false);
        ButterKnife.bind(this, rootView);

        List<Inform> informColumns=new ArrayList<Inform>();
        Inform informColumn=new Inform();
        informColumn.setName("宇宙无敌大帅比");
        informColumn.setContent("啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦");
        informColumn.setFollow(false);
        informColumn.setFollowNum("442455");
        informColumns.add(informColumn);
        Inform informColumn2=new Inform();
        informColumn2.setName("Amshine");
        informColumn2.setContent("哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈 ");
        informColumn2.setFollow(true);
        informColumn2.setFollowNum("6746341");
        informColumns.add(informColumn2);
        informColumns.add(informColumn2);
        informColumns.add(informColumn);
        informColumns.add(informColumn);
        informColumns.add(informColumn2);
        InformColumnAdapter informColumnAdapter=new InformColumnAdapter(informColumns,getActivity());
        informColumnList.setLayoutManager(new LinearLayoutManager(getActivity()));
        informColumnList.setAdapter(informColumnAdapter);





        return rootView;
    }

    @Override
    public String getLabel() {
        return "资讯栏目";
    }

    static class InformColumnAdapter extends RecyclerView.Adapter<InformColumnAdapter.Holder> {
        private List<Inform> list;
        private Activity activity;

        public InformColumnAdapter(List<Inform> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_column ,parent,false));
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Inform data=list.get(position);
            holder.columnName.setText(data.getName());
            holder.columnContent.setText(data.getContent());
            holder.columnFollowNum.setText(data.getFollowNum());
            if(data.isFollow()){
                holder.columnIsFollow.setText("已关注");
                holder.columnIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
            }else{
                holder.columnIsFollow.setText("+关注");
                holder.columnIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);
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
            ImgView columnImg;
            TextView columnName;
            TextView columnContent;
            TextView columnFollowNum;
            TextView columnIsFollow;

            public Holder(View itemView) {
                super(itemView);
                columnImg=itemView.findViewById(R.id.inform_column_img);
                columnName=itemView.findViewById(R.id.inform_column_name);
                columnContent=itemView.findViewById(R.id.inform_column_content);
                columnFollowNum=itemView.findViewById(R.id.inform_column_follownum);
                columnIsFollow=itemView.findViewById(R.id.inform_column_isfollow);

            }
        }
    }
}
