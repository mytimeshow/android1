package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
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
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.GoodCategory;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.common.dialog.GoodsSpecDialog;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.GoodsPlusMinusView;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;

/**
 * @author ruiaa
 * @date 2017/11/22
 *
 *  店铺商品列表 外卖餐饮类
 */

public class FoodListFragment extends BaseFragment {


    @BindView(R.id.foodlist_foods)
    RecyclerView foodR;
    @BindView(R.id.foodlist_category)
    RecyclerView categoryR;
    Unbinder unbinder;
    private StoreActivity storeActivity;
    private FoodAdapter foodAdapter;
    private LinearLayoutManager foodLayoutManager;
    private CategoryAdapter categoryAdapter;
    private List<Good> foodList = new ArrayList<>();
    private List<Good> categoryList = new ArrayList<>();

    public static FoodListFragment newInstance() {
        FoodListFragment fragment = new FoodListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeActivity = (StoreActivity) getActivity();

        StoreApi.getFoods(storeActivity.store.id).subscribe(new NetObserver<Response<List<GoodCategory>>>() {
            @Override
            public void onNext(Response<List<GoodCategory>> response) {
                super.onNext(response);

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_foodlist, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                foodList.add(Good.createCategory("分类1", categoryList.size()));
                categoryList.add(Good.createCategory("分类1", foodList.size() - 1));
            }
            if (i == 5) {
                foodList.add(Good.createCategory("分类2", categoryList.size()));
                categoryList.add(Good.createCategory("分类2", foodList.size() - 1));
            }
            if (i == 8) {
                foodList.add(Good.createCategory("分类3", categoryList.size()));
                categoryList.add(Good.createCategory("分类3", foodList.size() - 1));
            }
            if (i == 11) {
                foodList.add(Good.createCategory("分类4", categoryList.size()));
                categoryList.add(Good.createCategory("分类4", foodList.size() - 1));
            }
            foodList.add(new Good());
        }

        foodAdapter = new FoodAdapter(foodList, getActivity());
        foodLayoutManager = new LinearLayoutManager(getActivity());
        foodR.setLayoutManager(foodLayoutManager);
        foodR.setAdapter(foodAdapter);

        categoryAdapter = new CategoryAdapter(categoryList, getActivity());
        categoryR.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryR.setAdapter(categoryAdapter);
        categoryR.addItemDecoration(new RecycleViewDivider(getActivity()));

        foodR.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int nowState = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                nowState = newState;
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (nowState <= 0) return;
                int newP = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                onSelectCategory(newP);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getLabel() {
        return "商品";
    }


    public void refreshBuyNums(){
        foodAdapter.notifyDataSetChanged();
    }

    private void onSelectCategory(Good good) {
        if (categoryAdapter.selectCategory == good) return;
        categoryAdapter.selectCategory = good;
        categoryAdapter.notifyDataSetChanged();
        if (good.categoryP >= foodList.size()) return;
        foodLayoutManager.scrollToPositionWithOffset(good.categoryP, 0);
    }

    private void onSelectCategory(int foodP) {
        if (foodP < foodList.size()) {
            Good good = foodList.get(foodP);
            if (good.isCategory && good.categoryP < categoryList.size())
                onSelectCategory(categoryList.get(good.categoryP));
        }
    }

    class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.Holder> {
        private List<Good> list;
        private Activity activity;

        public FoodAdapter(List<Good> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false), viewType);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Good data = list.get(position);
            if (data.isCategory) {
                holder.category.setText(data.categoryStr);
                return;
            }
            holder.price.setText(String.format("￥%.2f",Math.random()*10));
            holder.imgView.id("919910512769269760");

            holder.itemView.setOnClickListener(v -> GoodDetailActivity.startGoodDetailActivity());


            holder.plusMinusView.setIsMultiSpec(data.isMultiSpec())
                    .setOnOpenSpecListener(()->{
                        GoodsSpecDialog.showSpecDialog(storeActivity,data,(trolleyGoods, num) -> {
                            storeActivity.trolleyStore.addGood(trolleyGoods,num);
                            storeActivity.refreshBottomTrolley();
                            refreshBuyNums();
                        });
                    })
                    .setOnPlusMinusListener(addNum -> {     //店铺 foodlist
                        int num=storeActivity.trolleyStore.addGood(data,addNum);
                        storeActivity.refreshBottomTrolley();
                        return num;
                    })
                    .setNum(storeActivity.trolleyStore.getGoodsBuyNum(data.id));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        @Override
        public int getItemViewType(int position) {
            return list.get(position).isCategory ? R.layout.item_store_foods_inner_category : R.layout.item_store_foods;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView category;
            ImgView imgView;
            TextView name;
            TextView nameSub;
            TextView sale;
            TextView price;
            GoodsPlusMinusView plusMinusView;

            public Holder(View itemView, int type) {
                super(itemView);
                if (type == R.layout.item_store_foods_inner_category) {
                    category = itemView.findViewById(R.id.item_name);
                    return;
                }
                imgView = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                nameSub = itemView.findViewById(R.id.item_name_sub);
                sale = itemView.findViewById(R.id.item_sale);
                price = itemView.findViewById(R.id.item_price);
                plusMinusView=itemView.findViewById(R.id.item_plus);
            }
        }

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {
        private List<Good> list;
        private Activity activity;
        private Good selectCategory = null;

        public CategoryAdapter(List<Good> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_store_foods_category, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Good data = list.get(position);

            holder.name.setText(data.categoryStr);
            if (data.categoryRedDot > 0) {
                holder.dot.setVisibility(View.VISIBLE);
                holder.dot.setText(data.categoryRedDot);
            } else {
                holder.dot.setVisibility(View.INVISIBLE);
            }

            holder.itemView.setBackgroundResource(data == selectCategory ? R.color.white : R.color.grey_100);
            holder.itemView.setOnClickListener(v -> onSelectCategory(data));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_name)
            TextView name;
            @BindView(R.id.item_dot)
            TextView dot;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
