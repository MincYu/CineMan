package jp.co.worksap.intern.vo;

import jp.co.worksap.intern.entities.commodity.CommodityType;

/**
 * Created by yuminchen on 16/7/26.
 */
public class PreferenceVO {

    public CommodityType type;
    public CommodityType second;


    public int number;
    public double percentage;

    public PreferenceVO(CommodityType type, double percentage) {
        this.type = type;
        this.percentage = percentage;
    }


    public PreferenceVO(CommodityType type, CommodityType second, double percentage) {
        this.type = type;
        this.second = second;
        this.percentage = percentage;
    }
}
