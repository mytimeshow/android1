package cn.czyugang.tcg.client.modules.entry.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.base.BaseFragmentAdapter;
import cn.czyugang.tcg.client.entity.FollowCotent;
import cn.czyugang.tcg.client.modules.entry.adapter.FollowContentAdapter;
import cn.czyugang.tcg.client.modules.store.GoodDetailIntroFragment;
import cn.czyugang.tcg.client.modules.store.GoodDetailSpecFragment;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.LabelLayout;

import static cn.czyugang.tcg.client.utils.app.ResUtil.getColor;
import static cn.czyugang.tcg.client.utils.app.ResUtil.getDrawable;

/**
 * @author ruiaa
 * @date 2017/11/24
 */

public class InformFollowFragment extends BaseFragment {
    /*@BindView(R.id.inform_detail_tab)
    TabLayout tabLayout;
    @BindView(R.id.inform_detail_pager)
    ViewPager viewPager;
*/

    @BindView(R.id.tv_all_follow)
    TextView tvAllFollw;
    @BindView(R.id.tv_all_column)
    TextView tvAllColumn;
    @BindView(R.id.tv_follow_person)
    TextView tvFollowPerson;

    @BindView(R.id.column_type)
    LabelLayout columnType;

    @BindView(R.id.inform_follow_list)
    RecyclerView lvInformFollow;

    Unbinder unbinder;


    public static InformFollowFragment newInstance() {
        InformFollowFragment fragment = new InformFollowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inform_follow, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        onAllFollow();

        List<String> list = new ArrayList<>();
        list.add("全部栏目");
        list.add("电影扒客");
        list.add("吃货专区");
        list.add("八卦资讯协会");
        list.add("小城百事通");
        list.add("感情后花园");
        columnType.setTexts(list);

        columnType.setOnClickItemListener(new LabelLayout.OnClickItemListener() {

            @Override
            public void onClick(String text, TextView textView) {

                columnType.selectTextView.setBackgroundColor(getResources().getColor(R.color.column_click_bg));
                textView.setTextColor(getColor(R.color.main_red));
                if(columnType.selectTextView!=null){
                    textView.setBackgroundColor(getResources().getColor(R.color.grey_100));
                }


            }
        });

        return rootView;
    }

    @Override
    public String getLabel() {
        return "我的关注";
    }

    @OnClick(R.id.tv_all_follow)
    public void onAllFollow(){
        tvAllFollw.setTextColor(getResources().getColor(R.color.main_red));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_pull_down),null);
        columnType.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_all_column)
    public void onAllColumn(){
        tvAllColumn.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvFollowPerson.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_pull_down_red),null);
        if (tvAllColumn.isFocusable()){
            if(columnType.getVisibility()==View.GONE){
                columnType.setVisibility(View.VISIBLE);
            }
            if(columnType.getVisibility()==View.VISIBLE){
                columnType.setVisibility(View.GONE);
            }
        }else{
            columnType.setVisibility(View.GONE);
        }
        //tvAllColumn.setCompoundDrawablePadding(-30);
    }

    @OnClick(R.id.tv_follow_person)
    public void onFollowPerson(){
        tvFollowPerson.setTextColor(getResources().getColor(R.color.main_red));
        tvAllFollw.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setTextColor(getResources().getColor(R.color.text_dark_gray));
        tvAllColumn.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_pull_down),null);
        columnType.setVisibility(View.GONE);
    }

    private void init(){
        List<FollowCotent> followCotentsList = new ArrayList<FollowCotent>();
        FollowCotent followCotent=new FollowCotent();
        followCotent.setName("博主名称");
        followCotent.setContent("内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
        followCotent.setCommentNum("1234567");
        followCotent.setThumbNum("777777777");
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        followCotentsList.add(followCotent);
        FollowContentAdapter followContentAdapter=new FollowContentAdapter(context);
        lvInformFollow.setAdapter(followContentAdapter);

    }
}
