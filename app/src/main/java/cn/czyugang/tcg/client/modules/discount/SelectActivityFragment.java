package cn.czyugang.tcg.client.modules.discount;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.DiscountApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Discount;
import cn.czyugang.tcg.client.entity.DiscountsResponse;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
import io.reactivex.Observable;

/**
 * @author ruiaa
 * @date 2018/1/5
 */

public class SelectActivityFragment extends BaseFragment {

    @BindView(R.id.discount_select_activity_list)
    RecyclerView activitysR;
    Unbinder unbinder;

    private SelectDiscountActivity selectDiscountActivity;
    private boolean isPlatform = false;

    private List<Discount> activityList = new ArrayList<>();
    private ActivityAdapter adapter;
    private RefreshLoadHelper refreshLoadHelper;
    private DiscountsResponse discountsResponse = null;

    public static SelectActivityFragment newInstance(boolean isPlatform) {
        SelectActivityFragment fragment = new SelectActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPlatform", isPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPlatform = getArguments().getBoolean("isPlatform");
        selectDiscountActivity=(SelectDiscountActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discount_select_activity, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        adapter=new ActivityAdapter(activityList,selectDiscountActivity);
        activitysR.setLayoutManager(new LinearLayoutManager(selectDiscountActivity));
        activitysR.setAdapter(adapter);
        refreshLoadHelper=new RefreshLoadHelper(selectDiscountActivity).build(activitysR);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(()->getData(false));
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(()->getData(true));


        return rootView;
    }

    private void getData(boolean loadmore) {
        int page = 1;
        String time = null;
        if (loadmore && discountsResponse != null) {
            page = discountsResponse.currentPage + 1;
            time = discountsResponse.accessTime;
        }
        Observable<DiscountsResponse> observable = null;
        if (isPlatform) {
            observable = DiscountApi.getPlatformActivity(page, time);
        } else {
            observable = DiscountApi.getStoreActivity(page, time);
        }
        observable.subscribe(new BaseActivity.NetObserver<DiscountsResponse>() {
            @Override
            public void onNext(DiscountsResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (loadmore && ErrorHandler.isRepeat(discountsResponse, response)) return;
                response.parse();
                if (loadmore) activityList.clear();
                activityList.addAll(response.data);
                adapter.notifyDataSetChanged();
                discountsResponse = response;
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    @Override
    public String getLabel() {
        isPlatform = getArguments().getBoolean("isPlatform");
        return "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.Holder> {
        private List<Discount> list;
        private Activity activity;

        public ActivityAdapter(List<Discount> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.item_discount_activity;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Discount data = list.get(position);
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
