package cn.czyugang.tcg.client.modules.groupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.ReduceProductApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.GroupDetail;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/12/7
 * <p>
 * 降价拍 详情
 */

public class GrouponDetailActivity extends BaseActivity {
    private static final String TAG = "GrouponDetailActivity";
    @BindView(R.id.groupon_detail_img)
    ImgView img;
    @BindView(R.id.groupon_detail_name)
    TextView name;
    @BindView(R.id.groupon_detail_name_sub)
    TextView nameSub;
    @BindView(R.id.groupon_detail_remain)
    TextView remain;
    @BindView(R.id.groupon_detail_sale)
    TextView sale;
    @BindView(R.id.groupon_detail_price)
    TextView price;
    @BindView(R.id.groupon_detail_price_down)
    TextView priceDown;
    @BindView(R.id.groupon_detail_price_min)
    TextView priceMin;
    @BindView(R.id.groupon_detail_time_limit)
    TextView timeLimit;
    @BindView(R.id.groupon_detail_header)
    ImgView headerImg;
    @BindView(R.id.groupon_detail_member)
    ImgView memberImg;
    @BindView(R.id.groupon_detail_member_num)
    TextView memberNum;
    @BindView(R.id.groupon_detail_time_remain)
    TextView timeRemain;
    @BindView(R.id.groupon_detail_member_open)
    TextView memberOpen;
    @BindView(R.id.groupon_detail_member_list)
    RecyclerView memberR;
    @BindView(R.id.groupon_detail_tab)
    TabLayout tabLayout;
    @BindView(R.id.groupon_detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.groupon_detail_buy)
    TextView buy;
    @BindView(R.id.groupon_detail_open_group)
    TextView openGroup;
    private GroupDetail mGroupDetail;
    private List<Member> memberList = new ArrayList<>();
    private MemberAdapter adapter;

    public static void startGrouponDetailActivity() {
        Intent intent = new Intent(getTopActivity(), GrouponDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupon_detail);
        ButterKnife.bind(this);

        getGroupDetail("940878770309607501");
        for (int i = 0; i < 10; i++) {
            memberList.add(new Member());
        }

        adapter = new MemberAdapter(memberList, this);
        memberR.setLayoutManager(new LinearLayoutManager(this));
        memberR.setAdapter(adapter);
        memberR.setNestedScrollingEnabled(false);
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.groupon_detail_member_open)
    public void onOpenMember() {
        memberR.setVisibility(memberR.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        memberOpen.setCompoundDrawablesWithIntrinsicBounds(0,0,
                memberR.getVisibility() == View.GONE ?R.drawable.icon_arrow_down:R.drawable.icon_arrow_up,0);
    }

    private static class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.Holder> {
        private List<Member> list;
        private Activity activity;

        public MemberAdapter(List<Member> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_groupon_member, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Member data = list.get(position);
            holder.itemView.setBackgroundResource(position == 0 ? R.drawable.bg_rect_red : R.drawable.bg_rect_light_red);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            public Holder(View itemView) {
                super(itemView);
            }
        }
    }

    public void getGroupDetail(String id){
        ReduceProductApi.getGroup(id).subscribe(new NetObserver<Response<GroupDetail>>() {
            @Override
            public void onNext(Response<GroupDetail> response) {
                super.onNext(response);
                showToast("done");
                Log.e(TAG, "onNext: " );
                if(ErrorHandler.judge200(response)){
                    mGroupDetail=response.getData();
                    img.id(mGroupDetail.productPicId);
                    name.setText(mGroupDetail.productTitle);
                    nameSub.setText(mGroupDetail.productSubTitle);
                    remain.setText("库存"+String.valueOf(mGroupDetail.inventory)+"件");
                    sale.setText("已售"+String.valueOf(mGroupDetail.sales)+"件");
                    price.setText("￥"+String.valueOf(mGroupDetail.productPrice));
                    priceDown.setText("每多一人参团\n拼团价降￥"+mGroupDetail.reducePrice);
                    priceMin.setText("拼团冰点价\n最低￥"+mGroupDetail.minPrice);
                    timeLimit.setText("拼团有效时间\n"+mGroupDetail.groupTime+"小时");
                    memberNum.setText(String.valueOf(mGroupDetail.memberCount));
                    timeRemain.setText("邀请更多好友参团，每人还能再省￥"+
                            mGroupDetail.restDiscount+"\n"+mGroupDetail.restTime/60+
                            "小时"+mGroupDetail.restTime%60+"分钟"+ "内成团");









                }
            }
        });

    }

    private static class Member {

    }
}
