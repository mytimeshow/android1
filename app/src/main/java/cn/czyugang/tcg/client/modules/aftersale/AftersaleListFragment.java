package cn.czyugang.tcg.client.modules.aftersale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.OrderApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Aftersale;
import cn.czyugang.tcg.client.entity.AftersaleRespose;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

/**
 * @author ruiaa
 * @date 2017/11/30
 */

public class AftersaleListFragment extends BaseFragment {

    public static final int AFTER_SALE_TYPE_ALL = 1;
    public static final int AFTER_SALE_TYPE_DEAL = 2;
    public static final int AFTER_SALE_TYPE_FINISH = 3;

    @BindView(R.id.aftersale_list)
    RecyclerView aftersaleR;
    Unbinder unbinder;

    private int type = AFTER_SALE_TYPE_ALL;
    private List<Aftersale> aftersaleList = new ArrayList<>();
    private AftersaleAdapter adapter;
    private AftersaleRespose aftersaleRespose;
    private RefreshLoadHelper refreshLoadHelper;

    public static AftersaleListFragment newInstance(int aftersaleType) {
        AftersaleListFragment fragment = new AftersaleListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", aftersaleType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) type = getArguments().getInt("type", AFTER_SALE_TYPE_ALL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_aftersale_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        adapter = new AftersaleAdapter(aftersaleList, getActivity());
        aftersaleR.setLayoutManager(new LinearLayoutManager(getActivity()));
        aftersaleR.setAdapter(adapter);
        aftersaleR.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
                ResUtil.getDimenInPx(R.dimen.dp_10), ResUtil.getColor(R.color.bg)).setDrawBottom(true));

        refreshLoadHelper=new RefreshLoadHelper(getActivity()).build(aftersaleR);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(()->getOrderList(false));
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(()->getOrderList(true));

        getOrderList(false);

        return rootView;
    }

    private void getOrderList(boolean loadmore) {
        //status  售后订单状态(空-查询全部,FINISH-已完结,PROCESSING-处理中
        String status = null;
        switch (type) {
            case AFTER_SALE_TYPE_DEAL:
                status = "PROCESSING";
                break;
            case AFTER_SALE_TYPE_FINISH:
                status = "FINISH";
                break;
        }
        String accessTime = null;
        int page = 1;
        if (loadmore && aftersaleRespose != null) {
            accessTime = aftersaleRespose.accessTime;
            page = aftersaleRespose.currentPage + 1;
        }
        OrderApi.aftersaleOrders(status, accessTime, page).subscribe(new BaseActivity.NetObserver<AftersaleRespose>() {
            @Override
            public void onNext(AftersaleRespose response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (loadmore&&ErrorHandler.isRepeat(aftersaleRespose,response)) return;
                if (!loadmore) aftersaleList.clear();
                response.parse();
                aftersaleList.addAll(response.data);
                adapter.notifyDataSetChanged();
                aftersaleRespose = response;
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    @Override
    public String getLabel() {
        if (getArguments() != null) type = getArguments().getInt("type", AFTER_SALE_TYPE_ALL);
        switch (type) {
            case AFTER_SALE_TYPE_ALL:
                return "全部";
            case AFTER_SALE_TYPE_DEAL:
                return "处理中";
            default:
                return "已完结";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class AftersaleAdapter extends RecyclerView.Adapter<AftersaleAdapter.Holder> {
        private List<Aftersale> list;
        private Activity activity;

        public AftersaleAdapter(List<Aftersale> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_aftersale, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Aftersale data = list.get(position);
            holder.itemView.setOnClickListener(v -> AftersaleDetailActivity.startAftersaleDetailActivity());

            holder.storeName.setText(data.storeName);
            holder.status.setText(data.getStatusStr());

            holder.goodName.setText(data.productName);
            holder.goodPrice.setText(CommonUtil.formatPrice(data.productRealPrice));
            //holder.goodOriginPrice.setText(CommonUtil.formatOriginPrice(data.productUnitPrice));
            holder.goodSpec.setText(data.productAttribute);
            holder.goodNum.setText("x"+data.productNumber);

            holder.typeReturnAll.setVisibility(data.isOnlyRefund()?View.GONE:View.VISIBLE);
            holder.typeRefund.setVisibility(data.isOnlyRefund()?View.VISIBLE:View.GONE);
            holder.type.setText(data.getTypeStr());
            holder.refundStatus.setText(data.getRefundStatusStr());

            holder.statusTag.setVisibility(data.platformAgreeApplication()?View.VISIBLE:View.GONE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView storeName;
            TextView status;

            View goodOne;
            ImgView goodImg;
            TextView goodName;
            TextView goodPrice;
            TextView goodOriginPrice;
            TextView goodSpec;
            TextView goodNum;

            View typeRefund;
            View typeReturnAll;
            TextView type;
            TextView refundStatus;
            TextView refundMoney;

            View statusTag;

            public Holder(View itemView) {
                super(itemView);
                storeName=itemView.findViewById(R.id.item_store_name);
                status=itemView.findViewById(R.id.item_status);

                goodOne=itemView.findViewById(R.id.item_list_one);
                goodImg=itemView.findViewById(R.id.item_img);
                goodName=itemView.findViewById(R.id.item_name);
                goodPrice=itemView.findViewById(R.id.item_price);
                goodOriginPrice=itemView.findViewById(R.id.item_price_origin);
                goodSpec=itemView.findViewById(R.id.item_spec);
                goodNum=itemView.findViewById(R.id.item_num);

                typeRefund=itemView.findViewById(R.id.item_img_refund);
                typeReturnAll=itemView.findViewById(R.id.item_img_return_goods);
                type=itemView.findViewById(R.id.item_type);
                refundStatus=itemView.findViewById(R.id.item_refund_status);
                refundMoney=itemView.findViewById(R.id.item_money);

                statusTag=itemView.findViewById(R.id.item_status_tag);
            }
        }
    }
}
