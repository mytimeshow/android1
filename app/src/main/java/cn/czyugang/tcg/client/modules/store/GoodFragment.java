package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.common.ImgActivity;
import cn.czyugang.tcg.client.modules.common.dialog.GoodsSpecDialog;
import cn.czyugang.tcg.client.modules.common.dialog.StoreTrolleyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;
import cn.czyugang.tcg.client.widget.BottomBalanceView;
import cn.czyugang.tcg.client.widget.GoodsPlusMinusView;
import cn.czyugang.tcg.client.widget.MultiImgView;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class GoodFragment extends BaseFragment {
    @BindView(R.id.good_multi_img)
    MultiImgView multiImg;
    @BindView(R.id.good_name)
    TextView name;
    @BindView(R.id.good_name_sub)
    TextView nameSub;
    @BindView(R.id.good_price)
    TextView price;
    @BindView(R.id.good_sale)
    TextView sale;
    @BindView(R.id.good_tag)
    TextView tag;
    @BindView(R.id.good_promotion)
    TextView promotion;
    @BindView(R.id.good_coupon)
    TextView coupon;
    @BindView(R.id.good_guarantee)
    TextView guarantee;
    @BindView(R.id.good_comment)
    TextView comment;
    @BindView(R.id.good_comment_num)
    TextView commentNum;
    @BindView(R.id.good_comment_list)
    RecyclerView commentR;
    @BindView(R.id.good_bottom_balance)
    BottomBalanceView bottomBalanceView;
    @BindView(R.id.good_plus_minus)
    GoodsPlusMinusView plusMinusView;
    Unbinder unbinder;

    private GoodDetailActivity goodDetailActivity;

    //购物车
    public TrolleyStore trolleyStore = null;
    private StoreTrolleyDialog storeTrolleyDialog = null;

    public static GoodFragment newInstance() {
        GoodFragment fragment = new GoodFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodDetailActivity=(GoodDetailActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    public void setGoodInfo(Good good) {
        multiImg.setShowBigImg(false).setOnClickImg(v -> ImgActivity.startImgActivity(good.picList,multiImg.getPosition()))
                .setImgIds(good.picList);
        name.setText(good.title);
        nameSub.setText(good.subTitle);
        price.setText(CommonUtil.formatPrice(good.showPrice));
        sale.setText("已售" + good.sales);

        initTrolley(good);
    }

    private void initTrolley(Good good){
        LogRui.i("initTrolley####");
        trolleyStore = AppKeyStorage.getTrolleyStore(good.storeId);

        bottomBalanceView.setTrolleyStore(trolleyStore);
        bottomBalanceView.refresh();
        bottomBalanceView.trolleyImg.setOnClickListener(v -> {
            if (storeTrolleyDialog == null) {
                storeTrolleyDialog = new StoreTrolleyDialog();
                storeTrolleyDialog.setTrolleyStore(trolleyStore, activity);
                storeTrolleyDialog.setOnDismissRefresh(dialog -> {
                    bottomBalanceView.refresh();
                });
            }
            storeTrolleyDialog.show(getActivity().getFragmentManager(), "StoreTrolleyDialog");
        });

        plusMinusView.setIsMultiSpec(good.isMultiSpec())
                .setOnOpenSpecListener(() -> {
                    GoodsSpecDialog.showSpecDialog(getActivity(), good, (trolleyGoods, num) -> {
                        trolleyStore.addGood(trolleyGoods, num);
                        bottomBalanceView.refresh();
                        plusMinusView.setNum(trolleyStore.getGoodsBuyNum(good.id));
                    });
                })
                .setOnPlusMinusListener(addNum -> {      //商品详情
                    int num = trolleyStore.addGood(good, addNum);
                    bottomBalanceView.refresh();
                    return num;
                })
                .setNum(trolleyStore.getGoodsBuyNum(good.id));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getLabel() {
        return "商品";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
