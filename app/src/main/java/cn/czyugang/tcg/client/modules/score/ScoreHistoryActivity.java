package cn.czyugang.tcg.client.modules.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private static final int FOOTER_VIEW=1;
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
    private boolean isToday;
    private boolean isYesterday;
    private boolean hadLoadMore;
    //积分来源类型
    private HashMap<String, String> scoreType = new HashMap<>();
    //月份的初始化
    private String year;
    private String today;
    private int month;
    private  int lastMonth;
    private int theMonthLastMonth;
    //月份的item首行标题
    private boolean isLastMonth=true;
    private boolean isTheMonthLastMonth=true;
    private boolean isThisMonth=true;
    //月份的字符串化
    private String strLaMonth;
    private String strTheLaMonth;
    //保证filer的首选种类不被覆盖
    private boolean isFirstIn=true;
    //根据月份是1,2开始所设定筛选
    private boolean isDoThis1=true;
    private boolean isDoThis2=true;
    private boolean isDoThis3=true;



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
        initTimeL();
        initFliterL();
        getScoreInfo(UserOAuth.getUserId(),"allRecord",1,30);
        isFirstIn=false;
//        int s=02-01;
//        Log.e(TAG, "onCreate: " +" " +s );
    }
    private void initFliterL() {
        filterL.add("所有记录", "获取记录", "使用记录")
                .setOnSelectItemListener(text -> {
                    filter.setText(text);
                    if(!isFirstIn){
                        if(text.equals("获取记录")){
                            getRecord();
                        }else if(text.equals("使用记录")){
                            usedRecord();
                        }else {
                            allRecord();
                        }
                    }
                })
                .build();
        filterL.select("所有记录");
    }
    private void initTimeL() {
        timeL.add("近三个月","本月",strLaMonth,strTheLaMonth)
                .setOnSelectItemListener(text -> {
                    time.setText(text);
                    if(!isFirstIn){
                        if(text.equals(strLaMonth)){
                            setLastMonth();
                        }else if(text.equals(strTheLaMonth)){
                            setTheMonthLastMonth();
                        }else if(text.equals("本月")){
                            setThisMonth();
                        }else {
                            historyList.clear();
                            isLastMonth=true;
                            isThisMonth=true;
                            isTheMonthLastMonth=true;
                            getScoreInfo(UserOAuth.getUserId(),"allRecord",10,100);
                            adapter.notifyDataSetChanged();
                            timeL.setVisibility(timeL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                        }
                    }

                })
                .build();
        timeL.select("近三个月");
    }
    //上上个月的积分记录
    private void setTheMonthLastMonth() {
        historyList.clear();
        isTheMonthLastMonth=true;
        getScoreInfo(UserOAuth.getUserId(),strTheLaMonth,10,100);
        adapter.notifyDataSetChanged();
        timeL.setVisibility(timeL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
    //这个月的积分记录
    private void setThisMonth() {
        historyList.clear();
        isThisMonth=true;
        getScoreInfo(UserOAuth.getUserId(),"本月",10,100);
        adapter.notifyDataSetChanged();
        timeL.setVisibility(timeL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
    //上个月的积分记录
    private void setLastMonth() {
        historyList.clear();
        isLastMonth=true;
        getScoreInfo(UserOAuth.getUserId(),strLaMonth,10,100);
        adapter.notifyDataSetChanged();
        timeL.setVisibility(timeL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
    //所有记录
    private void allRecord() {
        historyList.clear();
        isLastMonth=true;
        isThisMonth=true;
        isTheMonthLastMonth=true;
        getScoreInfo(UserOAuth.getUserId(),"allRecord",10,100);
        adapter.notifyDataSetChanged();
        filterL.setVisibility(filterL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);


    }
    //使用记录
    private void usedRecord() {
        historyList.clear();
        isLastMonth=true;
        isThisMonth=true;
        isTheMonthLastMonth=true;
        getScoreInfo(UserOAuth.getUserId(),"usedRecord",10,100);
        filterL.setVisibility(filterL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
    //获取记录
    private void getRecord() {
        historyList.clear();
        isLastMonth=true;
        isThisMonth=true;
        isTheMonthLastMonth=true;
        getScoreInfo(UserOAuth.getUserId(),"getRecord",10,100);
        filterL.setVisibility(filterL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
//下拉框内数据的实现
    private void getCurrentMonth() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
        String date=format.format(new Date());
        month= Integer.parseInt(date.substring(5,7));
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        today=format1.format(new Date());
        if(month==02){
            year=date.substring(0,4)+"年 ";
            lastMonth=month-1;
            theMonthLastMonth=12;
            strLaMonth=year+lastMonth+"月";
            year=(Integer.parseInt(date.substring(0,4))-1)+"年";
            strTheLaMonth=year+theMonthLastMonth+"月";

        }else if(month==01){
            year=date.substring(0,4)+"年 ";
            lastMonth=12;
            theMonthLastMonth=lastMonth-1;
            year=(Integer.parseInt(date.substring(0,4))-1)+"年";
            strLaMonth=year+lastMonth+"月";
            strTheLaMonth=year+theMonthLastMonth+"月";
        }else {
            year=date.substring(0,4)+"年 ";
            Log.e(TAG, "getCurrentMonth: "+year );
            lastMonth=month-1;
            theMonthLastMonth=lastMonth-1;
            strLaMonth=year+lastMonth+"月";
            strTheLaMonth=year+theMonthLastMonth+"月";
        }

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
        private View footerView;

        public ScoreHistoryAdapter(List<ScoreHistory> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        public void addFooterView(View view){
            footerView=view;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==FOOTER_VIEW){
              return new  Holder(footerView);
            }
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if(position==list.size()) return;
            ScoreHistory data = list.get(position);
            if (data.isTitle) {
                holder.title.setText(data.title);
                return;
            }

            holder.time.setText(data.getTime);
            if(data.name==null){
                holder.name.setText(RichText.newRichText("dd")
                        .append(data.tip.equals("") ? "" : "\n")
                        .appendSpColorRes(data.tip, R.dimen.sp_12, R.color.text_gray)
                        .build());
            }else {
                holder.name.setText(RichText.newRichText(data.name)
                        .append(data.tip.equals("") ? "" : "\n")
                        .appendSpColorRes(data.tip, R.dimen.sp_12, R.color.text_gray)
                        .build());
            }

            holder.score.setText(data.getScoreStr());
            holder.score.setTextColor(data.isValid ?
                    ResUtil.getColor(R.color.main_red) : ResUtil.getColor(R.color.text_gray));
            holder.invalid.setVisibility(data.isValid ? View.GONE : View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return footerView==null ? list.size():list.size()+1;
        }

        @Override
        public int getItemViewType(int position) {

            if(footerView !=null && getItemCount()>14 && position==getItemCount()-1){
                return FOOTER_VIEW;
            }
            if(position<list.size()){
                if( list.size() >0 && list.get(position).isTitle ){
                    return R.layout.itme_score_history_title;
                }else if(!( list.size() >0&& list.get(position).isTitle)){
                    return R.layout.item_score_history;
                }
            }

            return R.layout.recyclerview_footerview;
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
    private void getScoreInfo(String userId,String id,int page,int num) {
        ScoreApi.getScoreDetail(userId,page,num).subscribe(new NetObserver<Response<List<Score>>>() {
            @Override
            public void onNext(Response<List<Score>> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    mscoreList = response.getData();
                    for (int i = 0, size = mscoreList.size(); i < size; i++) {
                            int months= Integer.parseInt(mscoreList.get(i).createTime.substring(5, 7));
                           //记录类型
                            String type=mscoreList.get(i).recordType;
                            Log.e(TAG, "onNext: "+type );
                            getSignName(response.getValues(), "wayDict");
                            getSignName(response.getValues(), "businessDict");
                            getSignName(response.getValues(), "typeDict");
                            //兼并跨越年份的积分
                            if((months==01 ||months==12  || months==11)&& isDoThis2){
                                isDoThis1=false;
                                isDoThis3=false;
                                setSecondType(i,months,response,id,type);
                                Log.e(TAG, "onNext: 2yue" );
                            }else  if((months==02 ||months==01 || months==12) && isDoThis1){
                                isDoThis2=false;
                                isDoThis3=false;
                                setFirstType(i,months,response,id,type);
                                Log.e(TAG, "onNext: 1yue" );
                            }
                            else if(months>2 && isDoThis3){
                                isDoThis1=false;
                                isDoThis2=false;
                                setThridType(i,months,response,id,type);
                                Log.e(TAG, "onNext: >2yue" );
                            }



                      //  }
                    }
                    adapter = new ScoreHistoryAdapter(historyList, ScoreHistoryActivity.this);
                    LinearLayoutManager manager=new LinearLayoutManager(ScoreHistoryActivity.this);
                    historyR.setLayoutManager(manager);
                    historyR.setAdapter(adapter);
                  /*  if(adapter.getItemCount()>13){
                        View footerView=LayoutInflater.from(ScoreHistoryActivity.this)
                                .inflate(R.layout.recyclerview_footerview,historyR,false);
                        adapter.addFooterView(footerView);
                    }*/

                    isDoThis1=true;
                    isDoThis2=true;
                    isDoThis3=true;

                    DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
                    historyR.addOnScrollListener(new EndlessRecyclerOnScrollListener(manager,displayMetrics.heightPixels) {
                        @Override
                        public void onLoadMore(int currentPage) {
                            if(!hadLoadMore){
                                historyList.clear();
                                isLastMonth=true;
                                isThisMonth=true;
                                isTheMonthLastMonth=true;
                                getScoreInfo(UserOAuth.getUserId(),"allRecord",10,100);
                                adapter.notifyDataSetChanged();
                                hadLoadMore=true;
                            }


                        }


                    });

                }
            }
        });
    }
    //近三个月2 1 12类型
    private void setFirstType(int i, int months, Response<List<Score>> response, String id,String type) {
        if(id.equals("本月") && Math.abs(months-month)==0){
            init(i,response);
            Log.e(TAG, "onNext: thismonth" +id);

        }else if(id.equals(strLaMonth) && Math.abs(months-month)==1 ){
            init(i,response);
            Log.e(TAG, "onNext: lastMonth" +id);
        }
        else if(id.equals(strTheLaMonth)&& (Math.abs(months-month)==2 || Math.abs(months-month)==10)  ){
            init(i,response);
            Log.e(TAG, "onNext: theMonthLastMonth" +id);
        }
        else if(id.equals("usedRecord") && type.equals("USE")){
            Log.e(TAG, "onNext: usedrecord" );
            init(i,response);
        }
        else if(id.equals("getRecord") && type.equals("GET") ){
            Log.e(TAG, "onNext: getrecord" );
            init(i,response);
        }else if(id.equals("allRecord")){
            Log.e(TAG, "onNext: allrecord"+id+ "  "+Math.abs(months-month));
            init(i,response);
        }
    }
    //近三个月1 12 11类型
    private void setSecondType(int i, int months, Response<List<Score>> response, String id,String type) {
        if(id.equals("本月") && Math.abs(months-month)==0){
            init(i,response);
            Log.e(TAG, "onNext: thismonth" +id);

        }else if(id.equals(strLaMonth) && (Math.abs(months-month)==1 || Math.abs(months-month)==11)   ){
            init(i,response);
            Log.e(TAG, "onNext: lastMonth" +id);
        }
        else if(id.equals(strTheLaMonth)&& (Math.abs(months-month)==2 || Math.abs(months-month)==10)  ){
            init(i,response);
            Log.e(TAG, "onNext: theMonthLastMonth" +id);
        }
        else if(id.equals("usedRecord") && type.equals("USE")){
            Log.e(TAG, "onNext: usedrecord" );
            init(i,response);
        }
        else if(id.equals("getRecord") && type.equals("GET") ){
            Log.e(TAG, "onNext: getrecord" );
            init(i,response);
        }else if(id.equals("allRecord")){
            Log.e(TAG, "onNext: allrecord"+id+ "  "+Math.abs(months-month));
            init(i,response);
        }
    }
     //其他类型
    private void setThridType(int i, int months, Response<List<Score>> response,String id,String type) {
        if(id.equals("本月") && Math.abs(months-month)==0){
            init(i,response);
            Log.e(TAG, "onNext: thismonth" +id);

        }else if(id.equals(strLaMonth) && Math.abs(months-month)==1   ){
            init(i,response);
            Log.e(TAG, "onNext: lastMonth" +id);
        }
        else if(id.equals(strTheLaMonth)&& Math.abs(months-month)==2   ){
            init(i,response);
            Log.e(TAG, "onNext: theMonthLastMonth" +id);
        }
        else if(id.equals("usedRecord") && type.equals("USE")){
            Log.e(TAG, "onNext: usedrecord" );
            init(i,response);
        }
        else if(id.equals("getRecord") && type.equals("GET") ){
            Log.e(TAG, "onNext: getrecord" );
            init(i,response);
        }else if(id.equals("allRecord")){
            Log.e(TAG, "onNext: allrecord"+id+ "  "+Math.abs(months-month));
            init(i,response);
        }
    }
    private void init(int i,Response<List<Score>> response) {
        int months= Integer.parseInt(mscoreList.get(i).createTime.substring(5, 7));
       // Log.e(TAG, "onNext:0 "+ months+"  "+month );
        //本月的数据
        if(Math.abs(months-month)==0){
            if(isThisMonth){
                historyList.add(new ScoreHistory("本月"));
                isThisMonth=false;
            }
            initHistoryList(i,response);
            Log.e(TAG, "onNext:0 "+ months+"  "+month );
        }//上月的数据
        else if(Math.abs(months-month)==1 || Math.abs(months-month)==11){
            if(isLastMonth){
                historyList.add(new ScoreHistory(strLaMonth));
                isLastMonth=false;
            }
            initHistoryList(i,response);
            Log.e(TAG, "onNext:1 " );
        }//上上月的数据
        else if(Math.abs(months-month)==2 ||Math.abs(months-month)==10){
            if(isTheMonthLastMonth){
                historyList.add(new ScoreHistory(strTheLaMonth));
                isTheMonthLastMonth=false;
            }
            initHistoryList(i,response);
            Log.e(TAG, "onNext:2 " );
        }
    }
    private void initHistoryList(int i, Response<List<Score>> response) {
        ScoreHistory scoreHistory = new ScoreHistory();
        if(today.equals(mscoreList.get(i).createTime.substring(0, 10))){
            isToday=true;

        }else try {
            if(IsYesterday(mscoreList.get(i).createTime)){
                isYesterday=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(isToday){
            scoreHistory.setGetTime("今天");
            isToday=false;
        }else if(isYesterday){
            scoreHistory.setGetTime("昨天");
            isYesterday=false;
        }else {
            scoreHistory.setGetTime(mscoreList.get(i).createTime.substring(5, 10));
        }

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
       if(mscoreList.get(i).effective.equals("NO")){
           scoreHistory.setValid(false);
       }
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
    //获取积分来源的订单或资讯标题
    public String getSignName(JSONObject object, String type) {
        JSONArray list = object.optJSONArray(type);
        for (int i = 0, size = list.length(); i < size; i++) {
            scoreType.put(list.optJSONObject(i).optString("id"), list.optJSONObject(i).optString("name"));
        }
        scoreType.put("SIGN_CONTI","连续签到积分");
        return "";
    }
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }
    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

}
