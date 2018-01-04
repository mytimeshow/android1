package cn.czyugang.tcg.client.modules.person;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.RecordApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Record;
import cn.czyugang.tcg.client.entity.RecordResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.FiveStarView;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/12/23
 */
public class FootmarkFragment extends BaseFragment {

    @BindView(R.id.fragment_collection_bottom)
    View bottomV;
    @BindView(R.id.view_select_all)
    SelectButton selectB;
    @BindView(R.id.view_delete)
    TextView deleteT;
    @BindView(R.id.fragment_collection_list)
    RecyclerView listR;
    Unbinder unbinder;

    private int type = -1;
    private FootmarkAdapter adapter;
    private List<Record> recordList = new ArrayList<>();
    private RecordResponse recordResponse = null;
    private RefreshLoadHelper refreshLoadHelper;

    public static FootmarkFragment newInstance(int type) {
        FootmarkFragment fragment = new FootmarkFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type < 0 && getArguments() != null) type = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        adapter = new FootmarkAdapter(recordList, getActivity());
        listR.setLayoutManager(new LinearLayoutManager(getActivity()));
        listR.setAdapter(adapter);

        refreshLoadHelper=new RefreshLoadHelper(getActivity()).build(listR);
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(()->getFootmark(true));
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(()->getFootmark(false));

        getFootmark(false);

        return rootView;
    }

    private String getTypeStr() {
        //店铺(STORE)/商品(PRODUCT)/资讯(INFO)/任务(TASK)
        String typeStr = "";
        switch (type) {
            case CollectionActivity.COLLECTION_TYPE_SHOP:
                typeStr = "STORE";
                break;
            case CollectionActivity.COLLECTION_TYPE_GOODS:
                typeStr = "PRODUCT";
                break;
            default:
                typeStr = "INFO";
                break;
        }
        return typeStr;
    }

    private void getFootmark(boolean loadmore) {
        String accessTime=null;
        int page=1;
        if (loadmore&&recordResponse!=null){
            accessTime=recordResponse.accessTime;
            page=recordResponse.currentPage+1;
        }
        RecordApi.getFootMark(getTypeStr(),page,accessTime).subscribe(new BaseActivity.NetObserver<RecordResponse>() {
            @Override
            public void onNext(RecordResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    if (response.data == null || response.data.isEmpty()) {
                        if (loadmore) AppUtil.toast("没有更多了");
                        return;
                    }
                    switch (type) {
                        case CollectionActivity.COLLECTION_TYPE_SHOP:
                            response.parseStoreFootmark();
                            break;
                        case CollectionActivity.COLLECTION_TYPE_GOODS:
                            response.parseGoodFootmark();
                            break;
                        default:
                            response.parseInformFootmark();
                            break;
                    }

                    if (!loadmore) {
                        recordList.clear();
                    }
                    recordList.addAll(response.data);
                    adapter.notifyDataSetChanged();
                    recordResponse = response;
                }
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    private void onItemLongClick(Record record, View view) {

    }

    public void showBottom(boolean show) {
        bottomV.setVisibility(show ? View.VISIBLE : View.GONE);
        adapter.showSelectButton = show;
        adapter.notifyDataSetChanged();
    }

    public void onSelectAll(View view) {
        boolean s = ((CompoundButton) view).isChecked();
        for (Record record : recordList) {
            record.selected = s;
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.view_cancel)
    public void onCancelSelect() {
        showBottom(false);
        ((FootmarkActivity)getActivity()).cancelEdit();
    }

    @OnClick(R.id.view_delete)
    public void onDeleteSelect() {
        ArrayList<String> ids = new ArrayList<>();
        Iterator<Record> iterator = recordList.iterator();
        while (iterator.hasNext()) {
            Record record = iterator.next();
            if (record.selected) {
                ids.add(record.objectId);
                iterator.remove();
            }
        }
        adapter.notifyDataSetChanged();
        if (ids.isEmpty()) return;
        RecordApi.deleteFootMark(getTypeStr(), ids).subscribe(new BaseActivity.NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    AppUtil.toast("成功删除");
                }
            }
        });
    }

    @Override
    public String getLabel() {
        if (type < 0 && getArguments() != null) type = getArguments().getInt("type");
        switch (type) {
            case FootmarkActivity.ROOTMARK_TYPE_SHOP:
                return "店铺";
            case FootmarkActivity.ROOTMARK_TYPE_GOODS:
                return "商品";
            default:
                return "资讯";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private class FootmarkAdapter extends RecyclerView.Adapter<FootmarkAdapter.Holder> {
        private List<Record> list;
        private Activity activity;
        private boolean showSelectButton = false;

        public FootmarkAdapter(List<Record> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FootmarkAdapter.Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }


        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Record data = list.get(position);

            holder.itemView.setOnClickListener(v -> {
                onItemLongClick(data, v);
            });

            holder.selectButton.setVisibility(showSelectButton ? View.VISIBLE : View.GONE);
            holder.selectButton.setChecked(data.selected);
            holder.selectButton.setOnClickListener(v -> {
                data.selected = ((SelectButton) v).isChecked();
            });

            holder.time.setText("今天");

            switch (type) {
                case CollectionActivity.COLLECTION_TYPE_SHOP:
                    showStore(holder, data);
                    break;
                case CollectionActivity.COLLECTION_TYPE_GOODS:
                    showGood(holder, data);
                    break;
                default:
                    showInform(holder, data);
                    break;
            }
        }

        private void showStore(Holder holder, Record data) {

        }

        private void showGood(Holder holder, Record data) {

        }

        private void showInform(Holder holder, Record data) {

        }

        @Override
        public int getItemViewType(int position) {
            switch (type) {
                case CollectionActivity.COLLECTION_TYPE_SHOP:
                    return R.layout.item_collection_store;
                case CollectionActivity.COLLECTION_TYPE_GOODS:
                    return R.layout.item_collection_good;
                default:
                    return R.layout.item_collection_inform;
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView time;
            SelectButton selectButton;
            ImgView imgView;
            TextView name;
            FiveStarView starView;
            TextView activity;
            TextView distance;
            TextView price;
            TextView from;

            public Holder(View itemView) {
                super(itemView);
                selectButton = itemView.findViewById(R.id.item_select);
                imgView = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                starView = itemView.findViewById(R.id.item_star);
                activity = itemView.findViewById(R.id.item_activity);
                distance = itemView.findViewById(R.id.item_distance);
                price = itemView.findViewById(R.id.item_price);
                from = itemView.findViewById(R.id.item_from);
                time=itemView.findViewById(R.id.item_time);
                time.setVisibility(View.VISIBLE);
            }
        }
    }
}
