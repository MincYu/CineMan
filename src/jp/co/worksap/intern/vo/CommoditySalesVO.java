package jp.co.worksap.intern.vo;

import jp.co.worksap.intern.entities.commodity.CommodityType;

import java.time.LocalDate;

/**
 * Created by yuminchen on 16/7/25.
 */
public class CommoditySalesVO {

    public CommodityType type;
    public int number;
    public double money;
    public LocalDate date;

    /**
     * a constructor for full-part construction
     * @param type
     * @param money
     * @param date
     */
    public CommoditySalesVO(CommodityType type,int number, double money, LocalDate date) {
        this.type = type;
        this.number = number;
        this.money = money;
        this.date = date;
    }

    public CommoditySalesVO() {
    }

    public CommoditySalesVO(CommodityType type, int number, double money) {
        this.type = type;
        this.number = number;
        this.money = money;
    }
}
