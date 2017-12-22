package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.common.dialog.StoreTrolleyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;
import cn.czyugang.tcg.client.widget.BottomBalanceView;
import cn.czyugang.tcg.client.widget.FlowLayout;
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
    LinearLayout tagL;
    @BindView(R.id.good_promotion)
    TextView promotion;
    @BindView(R.id.good_coupon)
    TextView coupon;
    @BindView(R.id.good_guarantee)
    LinearLayout guaranteeL;
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

    private Good good;

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
        this.good=good;

        multiImg.setShowBigImg(false).setOnClickImg(v -> ImgActivity.startImgActivity(good.picList,multiImg.getPosition()))
                .setImgIds(good.picList);
        name.setText(good.title);
        nameSub.setText(good.subTitle);
        price.setText(CommonUtil.formatPrice(good.showPrice));
        sale.setText("已售" + good.sales);

        initTrolley();
        initProductTags();
        initServiceTag();
    }

    private void initTrolley(){
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

    private void initProductTags(){
        if (good.productTagList==null||good.productTagList.isEmpty()) {
            tagL.setVisibility(View.GONE);
            return;
        }

        for(int i=0,size=good.productTagList.size();i<size&&i<3;i++){
            Good.Tag tag=good.productTagList.get(i);
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_good_detail_tag,tagL,false);
            ((ImgView)view.findViewById(R.id.item_img)).id(tag.picId);
            ((TextView)view.findViewById(R.id.item_name)).setText(tag.name);
            tagL.addView(view,0);
        }

        tagL.setOnClickListener(v -> {
            MyDialog.Builder.newBuilder(getActivity())
                    .custom(R.layout.dialog_good_detail_tags)
                    .gravity(Gravity.BOTTOM)
                    .width(-1)
                    .heightPercent(0.4f)
                    .bindView(myDialog -> {
                        myDialog.onClick(R.id.dialog_close);
                        FlowLayout flowLayout=myDialog.rootView.findViewById(R.id.dialog_tags);
                        for(int i=0,size=good.productTagList.size();i<size;i++){
                            Good.Tag tag=good.productTagList.get(i);
                            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_good_detail_tag_dialog,flowLayout,false);
                            ((ImgView)view.findViewById(R.id.item_img)).id(tag.picId);
                            ((TextView)view.findViewById(R.id.item_name)).setText(tag.name);
                            flowLayout.addView(view,0);
                        }
                    })
                    .build()
                    .show();
        });
    }

    private void initServiceTag(){
        if (good.serviceTagList==null||good.serviceTagList.isEmpty()) {
            guaranteeL.setVisibility(View.GONE);
            return;
        }
        for(Good.Tag tag:good.serviceTagList){
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_good_detail_tag,guaranteeL,false);
            ((ImgView)view.findViewById(R.id.item_img)).id(tag.picId);
            ((TextView)view.findViewById(R.id.item_name)).setText(tag.name);
            guaranteeL.addView(view,0);
        }
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
