package jp.co.worksap.intern.bl.salesanalysis;

import jp.co.worksap.intern.bl.utility.DataSource;
import jp.co.worksap.intern.blservice.salesanalysisservice.SalesAnalysisService;
import jp.co.worksap.intern.entities.cinema.CinemaMstDTO;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.entities.commodity.RecordDTO;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.vo.CommoAllocationVO;
import jp.co.worksap.intern.vo.CommoditySalesVO;
import jp.co.worksap.intern.vo.SalesVolumeVO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/7/25.
 */
public class SalesAnalysisImpl implements SalesAnalysisService {

    private DataSource dataSource;

    public SalesAnalysisImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SalesAnalysisImpl() throws IOException {
        this(new DataSource());
    }

    @Override
    public Collection<RegionMstDTO> getAllRegion() {
        return dataSource.getRegion();
    }

    @Override
    public Collection<CinemaMstDTO> getCinemaInRegion(Long regionId) {

        Collection<CinemaMstDTO> cinemaList = dataSource.getCinema();
        List<CinemaMstDTO> result = cinemaList
                .stream()
                .filter(value->value.getRegionId().longValue()==regionId.longValue())
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public Collection<SalesVolumeVO> getSalesVolumeInCinema(Long cinemaId) {

        List<Long> cinemaIds = Arrays.asList(cinemaId);
        return getSalesVolume(new Vector<>(cinemaIds));

    }

    @Override
    public Collection<SalesVolumeVO> getSalesVolumeInRegion(Long regionId) {
        Collection<CinemaMstDTO> cinemaList = dataSource.getCinema();
        List<Long> cinemaIds = cinemaList
                .stream()
                .filter(value->value.getRegionId().longValue()==regionId.longValue())
                .map(o->o.getCinemaId())
                .collect(Collectors.toList());
        return getSalesVolume(new Vector<>(cinemaIds));
    }

    private Collection<SalesVolumeVO> getSalesVolume(Vector<Long> cinemaIds){
        Collection<RecordDTO> recordDTOs = dataSource.getRecord();

        List<SalesVolumeVO> tmp = recordDTOs
                .stream()
                .filter(o->cinemaIds.contains(o.getCinemaId().longValue()))
                .map(o->new SalesVolumeVO(o.getDate(),o.getMoney()))
                .collect(Collectors.toList());

        List<LocalDate> existDate = getExistDate();

        List<SalesVolumeVO> result = new ArrayList<>();

        for(LocalDate date : existDate){

            double sumMoney = tmp
                    .stream()
                    .filter(o->o.date.isEqual(date))
                    .mapToDouble(o->o.totalMoney)
                    .reduce(0.0, Double::sum);

            sumMoney = Math.round(sumMoney*10)/10.0;
            result.add(new SalesVolumeVO(date,sumMoney));
        }

        return result;
    }

    /**
     *  to get date that have record
     * @return
     */
    private List<LocalDate> getExistDate(){

        List<LocalDate> existDate = dataSource
                .getRecord()
                .stream()
                .map(o->o.getDate())
                .distinct()
                .sorted((o1,o2)->o1.compareTo(o2))
                .collect(Collectors.toCollection(ArrayList::new));

        return  existDate;
    }

    @Override
    public Collection<RecordDTO> getRecordInCinema(Long cinemaId) {
        return null;
    }

    @Override
    public Collection<CommoditySalesVO> getCommoditySalesInCinema(Long cinemaId) {

        List<Long> cinemaIds = Arrays.asList(cinemaId);
        return getCommoditySales(new Vector<>(cinemaIds));

    }

    @Override
    public Collection<CommoditySalesVO> getCommoditySalesInRegion(Long regionId) {
        Collection<CinemaMstDTO> cinemaList = dataSource.getCinema();
        List<Long> cinemaIds = cinemaList
                .stream()
                .filter(value->value.getRegionId().longValue()==regionId.longValue())
                .map(o->o.getCinemaId())
                .collect(Collectors.toList());
        return getCommoditySales(new Vector<>(cinemaIds));
    }

    /**
     * make it convenient to get total condition of sales
     *
     * @param cinemaIds
     * @return
     */
    private Collection<CommoditySalesVO> getCommoditySales(Vector<Long> cinemaIds){

        CommodityType[] typeMapper = CommodityType.values();

        List<LocalDate> dates = getExistDate();
        List<CommoditySalesVO> vos = new ArrayList<>();

        for(LocalDate date : dates){
            vos.addAll(getDailyCommoditySales(cinemaIds,date));
        }

        List<CommoditySalesVO> result = new ArrayList<>();

        for(CommodityType type: typeMapper){

            List<CommoditySalesVO> tmp = new ArrayList<>(vos);

            double money = tmp
                    .stream()
                    .filter(o->o.type==type)
                    .mapToDouble(o->o.money)
                    .reduce(0.0, Double::sum);

            int number = vos
                    .stream()
                    .filter(o->o.type==type)
                    .mapToInt(o->o.number)
                    .reduce(0,Integer::sum);

            money = Math.round(money*10)/10.0;
            result.add(new CommoditySalesVO(type,number,money));

        }



        return result;
    }


    @Override
    public Collection<CommoditySalesVO> getDailyCommoditySalesInCinema(Long cinemaId, LocalDate date) {

        List<Long> cinemaIds = Arrays.asList(cinemaId);
        return getDailyCommoditySales(new Vector<>(cinemaIds),date);

    }

    @Override
    public Collection<CommoditySalesVO> getDailyCommoditySalesInRegion(Long regionId, LocalDate date) {


        Collection<CinemaMstDTO> cinemaList = dataSource.getCinema();
        List<Long> cinemaIds = cinemaList
                .stream()
                .filter(value->value.getRegionId().longValue()==regionId.longValue())
                .map(o->o.getCinemaId())
                .collect(Collectors.toList());

        return getDailyCommoditySales(new Vector<>(cinemaIds),date);
    }

    @Override
    public Collection<CommoAllocationVO> getCommodityAllocation(CommodityType type, Long regionId) {

        Collection<RecordDTO> recordDTOs = dataSource.getRecord();
        Collection<CinemaMstDTO> cinemaList = dataSource.getCinema();
        List<Long> cinemaIds = cinemaList
                .stream()
                .filter(value->value.getRegionId().longValue()==regionId.longValue())
                .map(o->o.getCinemaId())
                .collect(Collectors.toList());


        List<CommoAllocationVO> commoAllocationVOs = recordDTOs
                .stream()
                .filter(o->cinemaIds.contains(o.getCinemaId())&&type==o.getType())
                .map(o->new CommoAllocationVO(o.getCinemaId(),o.getNumber()))
                .collect(Collectors.toCollection(ArrayList::new));

        List<CommoAllocationVO> result = new ArrayList<>();

        for(Long cinemaId : cinemaIds){
            int number = commoAllocationVOs
                    .stream()
                    .filter(o->o.cinemaId.longValue()==cinemaId)
                    .mapToInt(o->o.number)
                    .sum();
            result.add(new CommoAllocationVO(cinemaId,number));
        }

        return result;
    }

    @Override
    public Collection<CommoditySalesVO> getCommodityTrend(CommodityType type, Long regionId) {
        List<LocalDate> dates = getExistDate();

        List<Long> cinemaIds = getCinemaInRegion(regionId)
                .stream()
                .map(o->o.getCinemaId())
                .collect(Collectors.toCollection(ArrayList::new));

        List<CommoditySalesVO> commoditySalesVOs = dataSource.getRecord()
                .stream()
                .filter(o->cinemaIds.contains(o.getCinemaId())&&type==o.getType())
                .map(o->new CommoditySalesVO(o.getType(),o.getNumber(),o.getMoney(),o.getDate()))
                .collect(Collectors.toCollection(ArrayList::new));

        List<CommoditySalesVO> reslut = new ArrayList<>();

        for(LocalDate date : dates){
            List<CommoditySalesVO> targetvos = commoditySalesVOs
                    .stream()
                    .filter(o->o.date.isEqual(date))
                    .collect(Collectors.toList());

            double money = 0.0;
            int number = 0;
            for (CommoditySalesVO vo : targetvos){
                money+=vo.money;
                number+=vo.number;
            }
            reslut.add(new CommoditySalesVO(type,number,money,date));

        }
        return reslut;
    }

    @Override
    public RegionMstDTO getRegionInfoByName(String name) {
        return getAllRegion()
                .stream()
                .filter(o -> o.getName().equals(name))
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * to make it convenient to filter the record
     *
     * @param cinemaIds
     * @param date
     * @return
     */
    private Collection<CommoditySalesVO> getDailyCommoditySales(Vector<Long> cinemaIds, LocalDate date){
        CommodityType[] typeMapper = CommodityType.values();
        Collection<RecordDTO> recordDTOs = dataSource.getRecord();

        List<CommoditySalesVO> salesVOs = recordDTOs
                .stream()
                .filter(o->o.getDate().isEqual(date)&&cinemaIds.contains(o.getCinemaId()))
                .map(o->new CommoditySalesVO(o.getType(),o.getNumber(),o.getMoney(),o.getDate()))
                .collect(Collectors.toList());

        List<CommoditySalesVO> result = new ArrayList<>();

        for(CommodityType type: typeMapper){

            List<CommoditySalesVO> tmp = new ArrayList<>(salesVOs);

            double money = tmp
                    .stream()
                    .filter(o->o.type == type)
                    .mapToDouble(o->o.money)
                    .reduce(0.0, Double::sum);

            int number = salesVOs
                    .stream()
                    .filter(o->o.type == type)
                    .mapToInt(o->o.number)
                    .reduce(0,Integer::sum);

            money = Math.round(money*10)/10.0;
            result.add(new CommoditySalesVO(type,number,money,date));

        }

        return result;
    }


    /**
     * for test
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{

        SalesAnalysisImpl salesAnalysis = new SalesAnalysisImpl();
//
//        Collection<CinemaMstDTO> cinemaMstDTOs = salesAnalysis.getCinemaInRegion(Long.valueOf(1));
//        cinemaMstDTOs.stream().forEach(o-> System.out.println(o.getCinemaId()+","+o.getCinemaName()+","+o.getRegionId()));

//        Collection<SalesVolumeVO> salesVolumeVOs = salesAnalysis.getSalesVolumeInCinema(Long.valueOf(1));
//        salesVolumeVOs.stream().forEach(o-> System.out.println(o.date+","+o.totalMoney));

        Collection<CommoditySalesVO> commoditySalesVOs = salesAnalysis
                .getDailyCommoditySalesInCinema(Long.valueOf(1),LocalDate.of(2016,7,14));
        commoditySalesVOs.stream().forEach(o-> System.out.println(o.type+","+o.money+","+o.number));

        System.out.println("=====================");
        Collection<CommoditySalesVO> commoditySales = salesAnalysis
                .getCommoditySalesInCinema(Long.valueOf(1));
        commoditySales.stream().forEach(o-> System.out.println(o.type+","+o.money+","+o.number));

    }

}
