package cn.czyugang.tcg.client.modules.promote;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoterProfitTimeFragment extends BaseFragment {

    @BindView(R.id.promote_profit_money)
    TextView money;
    @BindView(R.id.promote_profit_click_num)
    TextView tvClickNum;
    @BindView(R.id.promote_profit_register_num)
    TextView tvRegisterNum;
    @BindView(R.id.promote_profit_pay_nums)
    TextView payNums;
    Unbinder unbinder;

    private String type=null;
    private List<BaseFragment> fragments=new ArrayList<>();

    private String monthIncome;
    private String lastMonthIncome;
    private String clickNum;
    private String productNum;
    private String registerNum;
    private String totalCommission;

    // 今天 昨天 近7日 近30日
    public static PromoterProfitTimeFragment newInstance(String type) {
        PromoterProfitTimeFragment fragment = new PromoterProfitTimeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type==null) type=getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_promoter_profit_time, container, false);
        unbinder = ButterKnife.bind(this, rootView);


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
    public void setData(String clickNum,String productNum,String registerNum,String totalCommission){
        money.setText("结算预估收入：￥"+totalCommission);
        tvClickNum.setText("点击数\n"+clickNum);
        payNums.setText("推广付款单数\n"+productNum);
        tvRegisterNum.setText("注册首单数\n"+registerNum);

    }
}
