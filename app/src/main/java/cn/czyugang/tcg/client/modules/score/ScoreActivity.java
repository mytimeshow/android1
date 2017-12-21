package cn.czyugang.tcg.client.modules.score;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

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


    public static void startScoreActivity() {
        Intent intent = new Intent(getTopActivity(), ScoreActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);

      getScoreInfo(UserOAuth.getUserId());

    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.score_sign_action)
    public void onSign() {
        signAction.setText("+XX");
        signAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
        signAction.setClickable(false);
    }

    @OnClick(R.id.score_continue_sign_action)
    public void onContinueSign() {
        continueSignAction.setText("+XX");
        continueSignAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
        continueSignAction.setClickable(false);
    }

    @OnClick(R.id.score_order_comment_action)
    public void onOrderComment() {
        orderCommentAction.setText("+XX");
        orderCommentAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
        orderCommentAction.setClickable(false);
    }

    @OnClick(R.id.score_inform_comment_action)
    public void onInformComment() {
        informCommentAction.setText("+XX");
        informCommentAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
        informCommentAction.setClickable(false);
    }

    @OnClick(R.id.score_buy_give_action)
    public void onBuyGive() {
        buyGiveAction.setText("+XX");
        buyGiveAction.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
        buyGiveAction.setClickable(false);
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
    private void getScoreInfo(String userId){
        ScoreApi.getScore(userId).subscribe(new NetObserver<Response<Score>>() {
            @Override
            public void onNext(Response<Score> response) {
                super.onNext(response);
                if(ErrorHandler.judge200(response)) {
                    mscore =response.getData();
                    score.setText(String.valueOf(mscore.score));
                    Log.e(TAG, "onNext: run" + mscore.score);


                }
            }
        });

    }

}
