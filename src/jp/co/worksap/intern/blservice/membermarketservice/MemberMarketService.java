package jp.co.worksap.intern.blservice.membermarketservice;

import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.vo.PreferenceVO;

import java.util.Collection;
import java.util.Set;

/**
 * Created by yuminchen on 16/7/26.
 */
public interface MemberMarketService {

    /**
     * get the result of classify
     *
     * @param regionId
     * @return
     */
    Collection<PreferenceVO> classifyCustomer(Long regionId,int typeValue);

    /**
     * get detailed information of the kind of customer
     *
     * @param regionId
     * @param type
     * @return
     */
    Collection<PreferenceVO> getDetailedPrefer(Long regionId, CommodityType type,CommodityType secondType);

    /**
     * get the suitable set of commodities
     *
     * @param regionId
     * @return
     */
    Collection<Set<CommodityType>> getSuitSet(Long regionId);

    /**
     * get the suit set for one grade
     *
     * @param regionId
     * @param type
     * @return
     */
    Set<CommodityType> getOneSet(Long regionId, CommodityType type,CommodityType secondType);

}
