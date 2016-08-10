package jp.co.worksap.intern.vo;

import jp.co.worksap.intern.entities.commodity.CommodityType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuminchen on 16/7/26.
 */
public class CustomerRecordVO {

    public Long customerId;
    public Map<CommodityType,Float> percentageMap;

    public CustomerRecordVO(Long customerId, Map<CommodityType, Float> percentageMap) {
        this.customerId = customerId;
        this.percentageMap = percentageMap;
    }

    public CustomerRecordVO(Long customerId) {
        this.customerId = customerId;
        percentageMap = new HashMap<>();
    }
}
