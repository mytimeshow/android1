package cn.czyugang.tcg.client.modules.store;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
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
    @BindView(R.id.good_buy_minus)
    ImageView buyMinus;
    @BindView(R.id.good_buy_num)
    TextView buyNum;
    @BindView(R.id.good_buy_plus)
    ImageView buyPlus;
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
    Unbinder unbinder;

    public static GoodFragment newInstance() {
        GoodFragment fragment = new GoodFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_good, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (multiImg.getImgNums()==0){
            ArrayList<String> list=new ArrayList<>();
            list.add("919910512769269760");
            list.add("919910512769269760");
            list.add("919910512769269760");
            list.add("919910512769269760");
            list.add("919910512769269730");
            list.add("919910512769269760");
            multiImg.setImgIds(list);
        }
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
