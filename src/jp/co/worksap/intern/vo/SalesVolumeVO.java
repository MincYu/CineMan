package jp.co.worksap.intern.vo;

import java.time.LocalDate;

/**
 * Created by yuminchen on 16/7/25.
 */
public class SalesVolumeVO {

    public LocalDate date;
    public double totalMoney;

    /**
     *
     * @param date
     * @param totalMoney
     */
    public SalesVolumeVO(LocalDate date, double totalMoney) {
        this.date = date;
        this.totalMoney = totalMoney;
    }

    public SalesVolumeVO() {
    }
}

