package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/26.
 */

public class PromoterReport {

    /**
     * lastMonthIncome : 0
     * monthIncome : 0
     * sevenDayReport : {"clickNum":0,"productNum":0,"registerNum":0,"totalCommission":0}
     * thirtyDayReport : {"clickNum":0,"productNum":0,"registerNum":0,"totalCommission":0}
     * todayReport : {"clickNum":0,"productNum":0,"registerNum":0,"totalCommission":0}
     * yesterdayReport : {"clickNum":0,"productNum":0,"registerNum":0,"totalCommission":0}
     */

    @SerializedName("lastMonthIncome")
    public double lastMonthIncome;
    @SerializedName("monthIncome")
    public double monthIncome;
    @SerializedName("sevenDayReport")
    public SevenDayReportBean sevenDayReport;
    @SerializedName("thirtyDayReport")
    public ThirtyDayReportBean thirtyDayReport;
    @SerializedName("todayReport")
    public TodayReportBean todayReport;
    @SerializedName("yesterdayReport")
    public YesterdayReportBean yesterdayReport;

    public static class SevenDayReportBean {
        /**
         * clickNum : 0
         * productNum : 0
         * registerNum : 0
         * totalCommission : 0
         */

        @SerializedName("clickNum")
        public int clickNum;
        @SerializedName("productNum")
        public int productNum;
        @SerializedName("registerNum")
        public int registerNum;
        @SerializedName("totalCommission")
        public double totalCommission;
    }

    public static class ThirtyDayReportBean {
        /**
         * clickNum : 0
         * productNum : 0
         * registerNum : 0
         * totalCommission : 0
         */

        @SerializedName("clickNum")
        public int clickNum;
        @SerializedName("productNum")
        public int productNum;
        @SerializedName("registerNum")
        public int registerNum;
        @SerializedName("totalCommission")
        public double totalCommission;
    }

    public static class TodayReportBean {
        /**
         * clickNum : 0
         * productNum : 0
         * registerNum : 0
         * totalCommission : 0
         */

        @SerializedName("clickNum")
        public int clickNum;
        @SerializedName("productNum")
        public int productNum;
        @SerializedName("registerNum")
        public int registerNum;
        @SerializedName("totalCommission")
        public double totalCommission;
    }

    public static class YesterdayReportBean {
        /**
         * clickNum : 0
         * productNum : 0
         * registerNum : 0
         * totalCommission : 0
         */

        @SerializedName("clickNum")
        public int clickNum;
        @SerializedName("productNum")
        public int productNum;
        @SerializedName("registerNum")
        public int registerNum;
        @SerializedName("totalCommission")
        public double totalCommission;
    }
}
