package jp.co.worksap.intern.entities.commodity;

import jp.co.worksap.intern.entities.ICsvMasterDTO;

import java.time.LocalDate;

/**
 * Created by yuminchen on 16/7/24.
 */
public class RecordDTO implements ICsvMasterDTO{

    private Long customerId;
    private Long cinemaId;
    private CommodityType type;
    private int number;
    private double money;
    private LocalDate date;


    /**
     * constructor
     *
     * @param customerId
     * @param cinemaId
     * @param type
     * @param number
     * @param money
     */
    public RecordDTO(Long customerId, Long cinemaId, CommodityType type, int number, double money, LocalDate date) {
        this.customerId = customerId;
        this.cinemaId = cinemaId;
        this.type = type;
        this.number = number;
        this.money = money;
        this.date = date;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getCinemaId() {
        return cinemaId;
    }

    public CommodityType getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public double getMoney() {
        return money;
    }

    public LocalDate getDate() {
        return date;
    }

}
