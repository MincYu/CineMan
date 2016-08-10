package jp.co.worksap.intern.blservice.salesanalysisservice;

import jp.co.worksap.intern.entities.cinema.CinemaMstDTO;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.entities.commodity.RecordDTO;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.vo.CommoAllocationVO;
import jp.co.worksap.intern.vo.CommoditySalesVO;
import jp.co.worksap.intern.vo.SalesVolumeVO;

import java.time.LocalDate;
import java.util.Collection;

/**
 * provide data analysis
 *
 * Created by yuminchen on 16/7/25.
 */
public interface SalesAnalysisService {

    /**
     * get all the information about region
     *
     * @return a collection of region
     */
    Collection<RegionMstDTO> getAllRegion();

    /**
     * get cinemas' information in the selected region
     *
     * @param regionId
     * @return a collection of cinema
     */
    Collection<CinemaMstDTO> getCinemaInRegion(Long regionId);

    /**
     * get total sales volume in selected cinema
     *
     * @param cinemaId
     * @return
     */
    Collection<SalesVolumeVO> getSalesVolumeInCinema(Long cinemaId);

    /**
     * get total sales volume in selected cinema
     *
     * @param regionId
     * @return
     */
    Collection<SalesVolumeVO> getSalesVolumeInRegion(Long regionId);

    /**
     * get records related to selected cinema
     *
     * @param cinemaId
     * @return a collection of record
     */
    Collection<RecordDTO> getRecordInCinema(Long cinemaId);

    /**
     * get sales condition of commodity chosen
     *
     * @param cinemaId
     * @return
     */
    Collection<CommoditySalesVO> getCommoditySalesInCinema(Long cinemaId);

    /**
     * get sales condition of commodity in the region
     *
     * @param regionId
     * @return
     */
    Collection<CommoditySalesVO> getCommoditySalesInRegion(Long regionId);


    /**
     * daily sales in cinema
     *
     * @param cinemaId
     * @param date
     * @return
     */
    Collection<CommoditySalesVO> getDailyCommoditySalesInCinema(Long cinemaId, LocalDate date);


    /**
     * daily sales in region
     *
     * @param regionId
     * @param date
     * @return
     */
    Collection<CommoditySalesVO> getDailyCommoditySalesInRegion(Long regionId, LocalDate date);

    /**
     * get trend of allocation of commodity
     *
     * @param type
     * @param regionId
     * @return
     */
    Collection<CommoAllocationVO> getCommodityAllocation(CommodityType type, Long regionId);

    /**
     * get the trend of sales for the commodity in the region
     *
     * @param type
     * @param regionId
     * @return
     */
    Collection<CommoditySalesVO> getCommodityTrend(CommodityType type, Long regionId);

    /**
     * get info of the region from name
     *
     * @param name
     * @return
     */
    RegionMstDTO getRegionInfoByName(String name);

 }
