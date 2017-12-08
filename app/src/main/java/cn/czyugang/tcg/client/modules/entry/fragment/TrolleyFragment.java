package cn.czyugang.tcg.client.modules.entry.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.widget.ActivityTextView;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

public class TrolleyFragment extends BaseFragment {
    @BindView(R.id.title_right)
    TextView edit;
    @BindView(R.id.trolley_store_list)
    RecyclerView storeR;
    @BindView(R.id.trolley_money)
    TextView money;
    @BindView(R.id.trolley_buy)
    TextView toBuy;
    @BindView(R.id.trolley_bottom_edit)
    LinearLayout bottomEditL;
    private Unbinder unbinder;

    private List<TrolleyStore> trolleyList=new ArrayList<>();
    private TrolleyAdapter adapter;

    public static TrolleyFragment newInstance() {
        TrolleyFragment fragment = new TrolleyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trolley, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        for (int i=0;i<10;i++){
            trolleyList.add(new TrolleyStore());
        }


        adapter=new TrolleyAdapter(trolleyList,getActivity());
        storeR.setLayoutManager(new LinearLayoutManager(getActivity()));
        storeR.setAdapter(adapter);
        new RefreshLoadHelper(getActivity()).build(storeR);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.title_right)
    public void onEdit(){
        if (bottomEditL.getVisibility()==View.GONE){
            bottomEditL.setVisibility(View.VISIBLE);
            edit.setText("完成");
        }else {
            bottomEditL.setVisibility(View.GONE);
            edit.setText("编辑");
        }
    }

    private static class TrolleyAdapter extends RecyclerView.Adapter<TrolleyAdapter.Holder> {
        private List<TrolleyStore> list;
        private Activity activity;
        public TrolleyAdapter(List<TrolleyStore> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_trolley_store,parent,false));
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            TrolleyStore data=list.get(position);

            holder.packFeeAsk.setOnClickListener(v -> {
                MyDialog.bubbleToast(activity,v,"啊啊ajdk哎哎哎");
            });
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        class Holder extends RecyclerView.ViewHolder {
            SelectButton selectButton;
            TextView name;
            View moreActivityL;
            ActivityTextView moreActivity;
            TextView moreActivityAction;
            RecyclerView goodsR;
            View useActivityL;
            ActivityTextView useActivity;
            TextView fixActivity;
            View packFeeL;
            TextView packFee;
            View packFeeAsk;

            public Holder(View itemView) {
                super(itemView);
                selectButton=itemView.findViewById(R.id.item_select);
                name=itemView.findViewById(R.id.item_name);
                moreActivityL=itemView.findViewById(R.id.item_more_activityL);
                moreActivity=itemView.findViewById(R.id.item_more_activity);
                moreActivityAction=itemView.findViewById(R.id.item_more_activity_add);
                goodsR=itemView.findViewById(R.id.item_goods);
                useActivityL=itemView.findViewById(R.id.item_activity_useL);
                useActivity=itemView.findViewById(R.id.item_activity_use);
                fixActivity=itemView.findViewById(R.id.item_activity_fix);
                packFeeL=itemView.findViewById(R.id.item_pack_feeL);
                packFee=itemView.findViewById(R.id.item_pack_fee);
                packFeeAsk=itemView.findViewById(R.id.item_pack_fee_ask);

            }
        }
    }
}
