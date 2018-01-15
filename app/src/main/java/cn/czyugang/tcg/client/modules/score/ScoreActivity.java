package cn.czyugang.tcg.client.modules.score;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

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
import cn.czyugang.tcg.client.modules.entry.activity.MainActivity;
import cn.czyugang.tcg.client.modules.order.MyOrderActivity;

/**
 * @author ruiaa
 * @date 2017/12/4
 */

public class ScoreActivity extends BaseActivity {
    private static final String TAG = "ScoreActivity";
    @BindView(R.id.score_score)
    TextView score;
    @BindView(R.id.score_sign_tip)
    TextView signTip;
    @BindView(R.id.score_sign_action)
    TextView signAction;
    @BindView(R.id.score_continue_sign_tip)
    TextView continueSignTip;
    @BindView(R.id.score_continue_sign)
    TextView continueSignDay;
    @BindView(R.id.score_continue_sign_action)
    TextView continueSignAction;
    @BindView(R.id.score_order_comment_tip)
    TextView orderCommentTip;
    @BindView(R.id.score_order_comment_action)
    TextView orderCommentAction;
    @BindView(R.id.score_inform_comment_tip)
    TextView informCommentTip;
    @BindView(R.id.score_inform_comment_action)
    TextView informCommentAction;
    @BindView(R.id.score_buy_give_tip)
    TextView buyGiveTip;
    @BindView(R.id.score_buy_give_action)
    TextView buyGiveAction;

    private Score mscore = null;
    private Response<Score> mResponse=null;
    public String myUserBonus;
    public String signedDay;
    public String currentOrderBonus;
    public String limitOrderBonus;
    public String currentInfoBonus;
    public String limitInfoBonus;
    public boolean isGotToday;
    public boolean isSignToday;
    public String signedDayBonusDict;
    public String signedDayDict;


    public static void startScoreActivity() {
        Intent intent = new Intent(getTopActivity(), ScoreActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);

  // getScoreInfo(UserOAuth.getUserId());
        getScoreDetail(UserOAuth.getUserId());

    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.score_sign_action)
    public void onSign() {

       // isSignToday=true;
        asynSignDays();
    }
    //用户签到后，同步数据到服务器
    private void asynSignDays() {
            if(!isSignToday){
                postData("SIGN",true);
            }else {
                showToast("你已经签到过了");

            }
    }

    @OnClick(R.id.score_continue_sign_action)
    public void onContinueSign() {
        if(Integer.parseInt(signedDay)>=Integer.parseInt(signedDayDict)){

            if(!isGotToday){
                postData("SIGN_CONTINUOUSLY",false);
            }else {
                showToast("你已经领取过了");
            }

        }else {
            showToast("要连续签到"+signedDayDict+"天才可以领取哦");
        }





    }
    @OnClick(R.id.score_order_comment_action)
    public void onOrderComment() {
				MyOrderActivity.startMyOrderActivity();
        //orderCommentAction.setText("+XX");
       // orderCommentAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
       // orderCommentAction.setClickable(false);


    }

    @OnClick(R.id.score_inform_comment_action)
    public void onInformComment() {
        MainActivity.openAndSelectFragment(1);
       // informCommentAction.setText("+XX");
       // informCommentAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
       // informCommentAction.setClickable(false);

    }

    @OnClick(R.id.score_buy_give_action)
    public void onBuyGive() {
        MainActivity.openAndSelectFragment(0);
     //   buyGiveAction.setText("+XX");
      //  buyGiveAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
        //buyGiveAction.setClickable(false);

    }

    @OnClick(R.id.score_history)
    public void onHistory() {
        ScoreHistoryActivity.startScoreHistoryActivity();
    }

    @OnClick(R.id.score_to_use)
    public void onToUse(){
        ScoreUseActivity.startScoreUseActivity();
    }

    //获取用户积分信息
    private void getScoreDetail(String userId){
        ScoreApi.getBaseScore(userId).subscribe(new NetObserver<Response<Score>>() {
            @Override
            public void onNext(Response<Score> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                   initData(response.getValues());
                    //今日任务数据的加载
                    score.setText(myUserBonus);
                    continueSignDay.setText("连续签到" +signedDay + "日");
                    signTip.setText("连续签到" +signedDay + "天");
                    if(isSignToday){
                        signAction.setText("+"+1);
                        signAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
                        signAction.setClickable(false);
                    }
                    if(Integer.parseInt(signedDay)>=Integer.parseInt(signedDayDict)){
                        if(isSignToday){
                           continueSignAction.setText("+"+signedDayBonusDict);
                            continueSignAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
                            continueSignAction.setClickable(false);
                            continueSignTip.setText("积分+"+signedDayBonusDict);
                        }else {
                            continueSignTip.setText("积分+"+0);
                        }
                    }else {
                        continueSignTip.setText("积分+"+0);
                    }

                    orderCommentTip.setText("每日上限：" +currentOrderBonus
                            + "/" +limitOrderBonus);
                    informCommentTip.setText("每日上限：" +currentInfoBonus
                            + "/" +limitInfoBonus);

                }
            }
        });
    }



    public void  postData(String way,boolean forSign){
        HashMap<String,Object> map=new HashMap<>();
        map.put("myUserBonus",myUserBonus);
        map.put("signedDay",signedDay);
        map.put("currentOrderBonus",currentOrderBonus);
        map.put("limitOrderBonus",limitOrderBonus);
        if(!forSign){
            map.put("currentInfoBonus",currentInfoBonus);
        }
        map.put("limitInfoBonus",limitInfoBonus);
        map.put("way",way);
        map.put("isGotToday",isGotToday);
        map.put("isSignToday",isSignToday);
        map.put("signedDayBonusDict",signedDayBonusDict);
        map.put("signedDayDict",signedDayDict);
        ScoreApi.PostUpdataScore(map).subscribe(new NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                if(ErrorHandler.judge200(response)){
                    getScoreDetail(UserOAuth.getUserId());
                    if(forSign){
                        showToast("签到成功+1");
                    }else {
                        showToast("积分加+"+signedDayBonusDict);
                    }

                }
            }
        });
    }
    public void initData(JSONObject values){
        myUserBonus= String.valueOf(values.opt("myUserBonus"));
        signedDay= String.valueOf(values.opt("signedDay"));
        currentOrderBonus= String.valueOf(values.opt("currentOrderBonus"));
        limitOrderBonus= String.valueOf(values.opt("limitOrderBonus"));
        currentInfoBonus= String.valueOf(values.opt("currentInfoBonus"));
        limitInfoBonus= String.valueOf(values.opt("limitInfoBonus"));
        isGotToday= (boolean) values.opt("isGotToday");
        isSignToday= (boolean) values.opt("isSignToday");
        signedDayBonusDict= String.valueOf(values.opt("signedDayBonusDict"));
        signedDayDict= String.valueOf(values.opt("signedDayDict"));


    }



}
