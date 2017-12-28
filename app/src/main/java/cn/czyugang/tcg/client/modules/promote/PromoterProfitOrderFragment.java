package cn.czyugang.tcg.client.modules.promote;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.PromoterApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.PromoteOrder;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoterProfitOrderFragment extends BaseFragment {
    @BindView(R.id.promote_profit_order_list)
    RecyclerView orderR;
    Unbinder unbinder;

    private String type=null;
    private List<PromoteOrder> orderListAll=new ArrayList<>();
    private List<PromoteOrder> orderListRegister = new ArrayList<>();
    private List<PromoteOrder> orderListProduct = new ArrayList<>();
    private List<PromoteOrder> orderListInvalid = new ArrayList<>();

    private Response<List<PromoteOrder>> orderResponseAll;
    private Response<List<PromoteOrder>> orderResponseRegister;
    private Response<List<PromoteOrder>> orderResponseProduct ;
    private Response<List<PromoteOrder>> orderResponseInvalid ;
    private RefreshLoadHelper refreshLoadHelper;
    private ProfitOrderAdapter adapter;

    //所有订单 邀请注册  推广商品  失效订单
    public static PromoterProfitOrderFragment newInstance(String type) {
        PromoterProfitOrderFragment fragment = new PromoterProfitOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type==null) getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_promoter_profit_order, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        adapter=new ProfitOrderAdapter(orderListAll,getActivity());
        orderR.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderR.setAdapter(adapter);
        refreshLoadHelper = new RefreshLoadHelper(getActivity()).build(orderR);
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> {
            String nowType;
            switch (type) {
                case "邀请注册":
                    nowType = "REGISTER";
                    break;
                case "推广商品":
                    nowType = "PRODUCT";
                    break;
                case "失效订单":
                    nowType = "INVALID";
                    break;
                default:
                    nowType = "";
                    break;

            }
            refreshList(true, nowType);
        });
        orderR.postDelayed(() -> {
            refreshList(true, "");
            refreshList(true, "REGISTER");
            refreshList(true, "PRODUCT");
            refreshList(true, "INVALID");
        }, 2000);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getLabel() {
        if (type==null) type=getArguments().getString("type");
        return type;
    }

    void refreshList(boolean loadmore, String type) {
        int pagerIndex = 1;
        String accessTime = "";
        switch (type) {
            case "":
                if (loadmore && orderResponseAll != null) {
                    accessTime = orderResponseAll.accessTime;
                    pagerIndex = orderResponseAll.currentPage + 1;
                }
                break;
            case "REGISTER":
                if (loadmore && orderResponseRegister != null) {
                    accessTime = orderResponseRegister.accessTime;
                    pagerIndex = orderResponseRegister.currentPage + 1;
                }
                break;
            case "PRODUCT":
                if (loadmore && orderResponseProduct != null) {
                    accessTime = orderResponseProduct.accessTime;
                    pagerIndex = orderResponseProduct.currentPage + 1;
                }
                break;
            case "INVALID":
                if (loadmore && orderResponseInvalid != null) {
                    accessTime = orderResponseInvalid.accessTime;
                    pagerIndex = orderResponseInvalid.currentPage + 1;
                }
                break;
            default:
                break;
        }

        PromoterApi.getOrderList(String.valueOf(pagerIndex), accessTime, type).subscribe(new BaseActivity.NetObserver<Response<List<PromoteOrder>>>() {
            @Override
            public void onNext(Response<List<PromoteOrder>> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                switch (type) {
                    case "":
                        if (ErrorHandler.isRepeat(orderResponseAll, response)) return;
                        if (!loadmore) {
                            orderListAll.clear();
                        }
                        orderListAll.addAll(response.data);
                        orderResponseAll = response;
                        break;
                    case "REGISTER":
                        if (ErrorHandler.isRepeat(orderResponseRegister, response)) return;
                        if (!loadmore) {
                            orderListRegister.clear();
                        }
                        orderListRegister.addAll(response.data);
                        orderResponseRegister = response;
                        break;
                    case "PRODUCT":
                        if (ErrorHandler.isRepeat(orderResponseProduct, response)) return;
                        if (!loadmore) {
                            orderListProduct.clear();
                        }
                        orderListProduct.addAll(response.data);
                        orderResponseProduct = response;
                        break;
                    case "INVALID":
                        if (ErrorHandler.isRepeat(orderResponseInvalid, response)) return;
                        if (!loadmore) {
                            orderListInvalid.clear();
                        }
                        orderListInvalid.addAll(response.data);
                        orderResponseInvalid = response;
                        break;
                    default:
                        break;
                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });

    }

    private static class ProfitOrderAdapter extends RecyclerView.Adapter<ProfitOrderAdapter.Holder> {
        private List<PromoteOrder> list;
        private Activity activity;
        public ProfitOrderAdapter(List<PromoteOrder> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_promote_profit_order,parent,false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            PromoteOrder data=list.get(position);
            holder.img.id(data.picId);
            holder.name.setText(data.title);
            holder.subName.setText("所属店铺："+data.storeName);
            holder.pay.setText("¥"+data.payment);
            holder.profit.setText("¥"+data.commission);
            holder.percentage.setText(data.rate+"%");
            holder.time.setText(data.account+"于"+data.createTime+"创建");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        class Holder extends RecyclerView.ViewHolder {
            ImgView img;
            TextView name;
            TextView subName;
            ImageView type;
            TextView status;
            TextView pay;
            TextView profit;
            TextView percentage;
            TextView time;
            public Holder(View itemView) {
                super(itemView);
                img= itemView.findViewById(R.id.item_img);
                name= itemView.findViewById(R.id.item_name);
                subName= itemView.findViewById(R.id.item_name_sub);
                type= itemView.findViewById(R.id.item_type);
                status= itemView.findViewById(R.id.item_status);
                pay= itemView.findViewById(R.id.item_pay);
                profit= itemView.findViewById(R.id.item_profit);
                percentage= itemView.findViewById(R.id.item_percentage);
                time= itemView.findViewById(R.id.item_time);
            }
        }
    }
}
