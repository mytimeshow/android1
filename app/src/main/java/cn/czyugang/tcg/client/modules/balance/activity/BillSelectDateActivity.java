package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.utils.DateFormat;

/**
 * Created by wuzihong on 2017/10/18.
 * 账单选择日期
 */

public class BillSelectDateActivity extends BaseActivity implements DatePicker.OnDateChangedListener {
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    @BindView(R.id.tv_select)
    TextView tv_select;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.tv_to)
    TextView tv_to;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.datePicker)
    DatePicker datePicker;
    private Resources mResources;

    private boolean mSelectStartTime;
    private Date mStartTime;
    private Date mEndTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_select_date);
        ButterKnife.bind(this);
        mResources = getResources();
        initView();
    }

    private void initView() {
        datePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        Calendar now = Calendar.getInstance();
        datePicker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), this);
        selectMonth();
    }

    private void selectMonth() {
        tv_select.setText("按日选择");
        datePicker.setVisibility(View.GONE);
        ((LinearLayout) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        tv_start_date.setText("选择月份");
        tv_start_date.setTextColor(mResources.getColor(R.color.text_gray));
        tv_to.setVisibility(View.GONE);
        tv_end_date.setVisibility(View.GONE);
        mStartTime = null;
        mEndTime = null;
    }

    private void selectDay() {
        tv_select.setText("按月选择");
        datePicker.setVisibility(View.GONE);
        ((LinearLayout) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.VISIBLE);
        tv_start_date.setText("开始日期");
        tv_end_date.setText("结束日期");
        tv_start_date.setTextColor(mResources.getColor(R.color.text_gray));
        tv_end_date.setTextColor(mResources.getColor(R.color.text_gray));
        tv_to.setVisibility(View.VISIBLE);
        tv_end_date.setVisibility(View.VISIBLE);
        mStartTime = null;
        mEndTime = null;
    }

    @OnClick({R.id.iv_back, R.id.tv_finish, R.id.tv_select, R.id.tv_start_date, R.id.tv_end_date,
            R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_finish:
                if (tv_end_date.getVisibility() == View.GONE) {
                    if (mStartTime == null) {
                        setResult(RESULT_OK);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(KEY_START_TIME, mStartTime);
                        setResult(RESULT_OK, intent);
                    }
                } else {
                    if (mStartTime == null && mEndTime == null) {
                        setResult(RESULT_OK);
                    } else if (mStartTime == null) {
                        showToast("请选择开始日期");
                        return;
                    } else if (mEndTime == null) {
                        showToast("请选择结束日期");
                        return;
                    } else if (mStartTime.after(mEndTime)) {
                        showToast("结束时间不能比开始时间早");
                        return;
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(KEY_START_TIME, mStartTime);
                        intent.putExtra(KEY_END_TIME, mEndTime);
                        setResult(RESULT_OK, intent);
                    }
                }
                finish();
                break;
            case R.id.tv_select:
                if (tv_end_date.getVisibility() == View.GONE) {
                    selectDay();
                } else {
                    selectMonth();
                }
                break;
            case R.id.tv_start_date: {
                mSelectStartTime = true;
                tv_start_date.setTextColor(mResources.getColor(R.color.main_light_blue));
                tv_end_date.setTextColor(mResources.getColor(R.color.text_gray));
                datePicker.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                if (tv_end_date.getVisibility() == View.GONE) {
                    mStartTime = calendar.getTime();
                    tv_start_date.setText(DateFormat.format("yyyy-MM", mStartTime));
                } else {
                    mStartTime = calendar.getTime();
                    tv_start_date.setText(DateFormat.formatDate(mStartTime));
                }
                break;
            }
            case R.id.tv_end_date: {
                mSelectStartTime = false;
                tv_start_date.setTextColor(mResources.getColor(R.color.text_gray));
                tv_end_date.setTextColor(mResources.getColor(R.color.main_light_blue));
                datePicker.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                mEndTime = calendar.getTime();
                tv_end_date.setText(DateFormat.formatDate(mEndTime));
                break;
            }
            case R.id.iv_delete:
                if (tv_end_date.getVisibility() == View.GONE) {
                    selectMonth();
                } else {
                    selectDay();
                }
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, monthOfYear, dayOfMonth);
        if (mSelectStartTime) {
            if (tv_end_date.getVisibility() == View.GONE) {
                mStartTime = calendar.getTime();
                tv_start_date.setText(DateFormat.format("yyyy-MM", mStartTime));
            } else {
                mStartTime = calendar.getTime();
                tv_start_date.setText(DateFormat.formatDate(mStartTime));
            }
        } else {
            mEndTime = calendar.getTime();
            tv_end_date.setText(DateFormat.formatDate(mEndTime));
        }
    }
}

