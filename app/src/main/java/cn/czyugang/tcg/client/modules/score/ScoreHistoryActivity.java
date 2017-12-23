package cn.czyugang.tcg.client.modules.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.ScoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Score;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.string.RichText;
import cn.czyugang.tcg.client.widget.SpinnerSelectView;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScoreHistoryActivity extends BaseActivity {
    private static final String TAG = "ScoreHistoryActivity";

    @BindView(R.id.score_history)
    RecyclerView historyR;
    @BindView(R.id.score_history_filter)
    TextView filter;
    @BindView(R.id.score_history_time)
    TextView time;
    @BindView(R.id.score_history_filterL)
    SpinnerSelectView filterL;
    @BindView(R.id.score_history_timeL)
    SpinnerSelectView timeL;
    private List<ScoreHistory> historyList = new ArrayList<>();
    private ScoreHistoryAdapter adapter;
    private List<Score> mscoreList;
    private Response<Score> mResponse;
    private HashMap<String, String> scoreType = new HashMap<>();
    private int month;
    private  int lastMonth;
    private int theMonthLastMonth;
    private boolean isLastMonth=true;
    private boolean isTheMonthLastMonth=true;
    private boolean isThisMonth=true;


    public static void startScoreHistoryActivity() {
        Intent intent = new Intent(getTopActivity(), ScoreHistoryActivity.class);
        getTopActivity().startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);
        ButterKnife.bind(this);
        getCurrentMonth();
        filterL.add("所有记录", "获取记录", "使用记录")
                .setOnSelectItemListener(text -> {
                    filter.setText(text);
                })
                .build();

        timeL.add("本月",String.valueOf(lastMonth),String.valueOf(lastMonth-1))
                .setOnSelectItemListener(text -> {
                    time.setText(text);
                })
                .build();
        filterL.select("所有记录");
        timeL.select("本月");
        filterL.setOnSelectItemListener(new SpinnerSelectView.OnSelectItemListener() {
            @Override
            public void onSelect(String text) {
               if(text.equals("获取记录")){
                   getRecord();
               }else if(text.equals("使用记录")){
                   usedRecord();
               }else {
                   allRecord();
               }

            }
        });
        timeL.setOnSelectItemListener(new SpinnerSelectView.OnSelectItemListener() {
            @Override
            public void onSelect(String text) {
                if(text.equals(String.valueOf(lastMonth))){
                        setLastMonth();
                }else if(text.equals(String.valueOf(theMonthLastMonth))){
                    setTheMonthLastMonth();
                }else {
                    setThisMonth();
                }
            }
        });
        getScoreInfo(UserOAuth.getUserId(),"allRecord");
    }
    //上上个月的积分记录
    private void setTheMonthLastMonth() {

    }
    //这个月的积分记录
    private void setThisMonth() {
    }
    //上个月的积分记录
    private void setLastMonth() {
    }
    //所有记录
    private void allRecord() {
        historyList.clear();
        getScoreInfo(UserOAuth.getUserId(),"allRecord");
    }
    //使用记录
    private void usedRecord() {
    }
    //获取记录
    private void getRecord() {
    }

    private void getCurrentMonth() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
        String date=format.format(new Date());
        month= Integer.parseInt(date.substring(5,7));
        lastMonth=month-1;
        theMonthLastMonth=lastMonth-1;
        Log.e(TAG, "onCreate: "+date+" "+month );
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.score_history_filter)
    public void onFilter() {
        filterL.setVisibility(filterL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        timeL.setVisibility(View.GONE);
    }

    @OnClick(R.id.score_history_time)
    public void onTime() {
        filterL.setVisibility(View.GONE);
        timeL.setVisibility(timeL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }


    private static class ScoreHistoryAdapter extends RecyclerView.Adapter<ScoreHistoryAdapter.Holder> {
        private List<ScoreHistory> list;
        private Activity activity;

        public ScoreHistoryAdapter(List<ScoreHistory> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ScoreHistory data = list.get(position);
            if (data.isTitle) {
                holder.title.setText(data.title);
                return;
            }

            holder.time.setText(data.getTime);
            holder.name.setText(RichText.newRichText(data.name)
                    .append(data.tip.equals("") ? "" : "\n")
                    .appendSpColorRes(data.tip, R.dimen.sp_12, R.color.text_gray)
                    .build());
            holder.score.setText(data.getScoreStr());
            holder.score.setTextColor(data.isValid ?
                    ResUtil.getColor(R.color.main_red) : ResUtil.getColor(R.color.text_gray));
            holder.invalid.setVisibility(data.isValid ? View.GONE : View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).isTitle ? R.layout.itme_score_history_title : R.layout.item_score_history;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView title;
            TextView time;
            TextView name;
            TextView score;
            View invalid;

            public Holder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.item_title);
                time = itemView.findViewById(R.id.item_time);
                name = itemView.findViewById(R.id.item_name);
                score = itemView.findViewById(R.id.item_score);
                invalid = itemView.findViewById(R.id.item_invalid);
            }
        }
    }

    //获取积分的来源途径信息,积分明细
    private void getScoreInfo(String userId,String id) {
        ScoreApi.getScoreDetail(userId).subscribe(new NetObserver<Response<List<Score>>>() {
            @Override
            public void onNext(Response<List<Score>> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    mscoreList = response.getData();
                    for (int i = 0, size = mscoreList.size(); i < size; i++) {

                        if (mscoreList.get(i).effective.equals("NO")) {
                        } else {
                            int months= Integer.parseInt(mscoreList.get(i).createTime.substring(5, 7));
                            getSignName(response.getValues(), "wayDict");
                            if(id.equals("本月") && Math.abs(months-month)==0){
                                init(i,response);

                            }else if(id.equals(String.valueOf(lastMonth)) && Math.abs(months-month)==1){
                                init(i,response);
                            }
                            else if(id.equals(String.valueOf(theMonthLastMonth))&& Math.abs(months-month)==2){
                                init(i,response);
                            }
                            else if(id.equals("使用记录")){

                            }
                            else if(id.equals("获取记录")){

                            }else {


                            }


                        }
                    }
                    adapter = new ScoreHistoryAdapter(historyList, ScoreHistoryActivity.this);
                    historyR.setLayoutManager(new LinearLayoutManager(ScoreHistoryActivity.this));
                    historyR.setAdapter(adapter);
                }
            }
        });
    }

    private void init(int i,Response<List<Score>> response) {
        int months= Integer.parseInt(mscoreList.get(i).createTime.substring(5, 7));
        Log.e(TAG, "onNext:0 "+ months+"  "+month );
        //本月的数据
        if(Math.abs(months-month)==0){
            if(isThisMonth){
                historyList.add(new ScoreHistory("本月"));
                isThisMonth=false;
            }
            initHistoryList(i,response);
            Log.e(TAG, "onNext:0 "+ months+"  "+month );
        }//上月的数据
        else if(Math.abs(months-month)==1){
            if(isLastMonth){
                historyList.add(new ScoreHistory(String.valueOf(lastMonth)));
                isLastMonth=false;
            }
            initHistoryList(i,response);
            Log.e(TAG, "onNext:1 " );
        }//上上月的数据
        else if(Math.abs(months-month)==2){
            if(isTheMonthLastMonth){
                historyList.add(new ScoreHistory(String.valueOf(theMonthLastMonth)));
                isTheMonthLastMonth=false;
            }
            initHistoryList(i,response);
            Log.e(TAG, "onNext:2 " );
        }
    }

    private void initHistoryList(int i, Response<List<Score>> response) {
        ScoreHistory scoreHistory = new ScoreHistory();
        scoreHistory.setGetTime(mscoreList.get(i).createTime.substring(5, 10));
        scoreHistory.setName(scoreType.get(mscoreList.get(i).way));
        if (mscoreList.get(i).objectId.equals("")) {
            Log.e(TAG, "onNext:objectId is null ");
        } else {
            //订单或资讯标题
            String objectId = mscoreList.get(i).objectId;
            getSignName(response.getValues(), "objectIdToSubTitleDict");
            scoreHistory.setTip(scoreType.get(objectId));
            Log.e(TAG, "onNext:objectId not null ");
        }
        scoreHistory.setNum(mscoreList.get(i).score);
        historyList.add(scoreHistory);
    }


    private class ScoreHistory {
        private String getTime;
        public String name ;
        public String tip="";
        public int num ;
        public boolean isValid = true;
        public boolean isTitle = false;
        public String title = "本月";

        public ScoreHistory() {
        }

        public void setValid(boolean isValid) {
            this.isValid = isValid;
        }

        public void setGetTime(String getTime) {
            this.getTime = getTime;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ScoreHistory(String title) {
            isTitle = true;
            this.title = title;
        }

        public String getTime() {
            return "今天";
        }

        public String getScoreStr() {
            return "+" + num;
        }
    }
    // public void initScoreType(){
//        scoreType.put("SIGN","每日签到");
//        scoreType.put("SIGN_CONTINUOUSLY","连续签到");
//        scoreType.put("LOGIN_CONTINUOUSLY","连续登陆");
//        scoreType.put("COMMENT","资讯评论");
//        scoreType.put("EVALUATE","订单评价");
//        scoreType.put("PAY","商城购物");
//    }

    //获取积分来源的订单或资讯标题
    public String getSignName(JSONObject object, String type) {
        JSONArray list = object.optJSONArray(type);
        // Log.e(TAG, "getSignName: isnull" +list.length() );
        for (int i = 0, size = list.length(); i < size; i++) {
            //  Log.e(TAG, "getSignName: "+list.optJSONObject(i).optString("id") );
            scoreType.put(list.optJSONObject(i).optString("id"), list.optJSONObject(i).optString("name"));
        }
        // Log.e(TAG, "getSignName: isnull"+scoreType.size()  );
        return "";
    }

}
