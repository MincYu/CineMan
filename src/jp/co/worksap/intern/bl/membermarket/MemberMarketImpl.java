package jp.co.worksap.intern.bl.membermarket;

import jp.co.worksap.intern.bl.salesanalysis.SalesAnalysisImpl;
import jp.co.worksap.intern.bl.utility.DataSource;
import jp.co.worksap.intern.blservice.membermarketservice.MemberMarketService;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.entities.commodity.RecordDTO;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.ui.utility.Set2String;
import jp.co.worksap.intern.vo.CustomerRecordVO;
import jp.co.worksap.intern.vo.PreferenceVO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/7/26.
 */
public class MemberMarketImpl implements MemberMarketService {

    private DataSource dataSource;
    private SalesAnalysisImpl analysis;
    private CustomerPreference preference;
    private ArrayList<ArrayList<float[]>> result;
    private List<PreferenceVO> preferenceVOs;
    private ArrayList<float[]> averageValues;
    private List<Set<CommodityType>> groupSets;

    public MemberMarketImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        analysis = new SalesAnalysisImpl(dataSource);
    }

    public MemberMarketImpl() throws IOException{
        dataSource = new DataSource();
        analysis = new SalesAnalysisImpl(dataSource);
    }

    @Override
    public Collection<PreferenceVO> classifyCustomer(Long regionId, int typeValue) {

        Collection<RecordDTO> recordDTOs = dataSource.getRecord();
        List<Long> cinemaIds = analysis.getCinemaInRegion(regionId)
                .stream()
                .map(o->o.getCinemaId())
                .collect(Collectors.toCollection(ArrayList::new));

        recordDTOs = recordDTOs
                .stream()
                .filter(recordDTO -> cinemaIds.contains(recordDTO.getCinemaId()))
                .collect(Collectors.toCollection(ArrayList::new));

        List<Long> customerIds = recordDTOs
                .stream()
                .map(o -> o.getCustomerId())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        /*
         * produce the customer record list to for kmeans
         */
        List<CustomerRecordVO> customerRecords = new ArrayList<>();
        for(Long customerId : customerIds){
            Map<CommodityType,Float> map = new HashMap<>();
            List<RecordDTO> tmp = recordDTOs
                    .stream()
                    .filter(o->o.getCustomerId().longValue()==customerId.longValue())
                    .collect(Collectors.toCollection(ArrayList::new));

            float totalNumber = tmp.stream().mapToInt(o -> o.getNumber()).sum();
            CommodityType[] types = CommodityType.values();

            for (CommodityType type : types){
                float number = tmp
                        .stream()
                        .filter(o->o.getType()==type)
                        .mapToInt(o->o.getNumber())
                        .sum();
                map.put(type,number/totalNumber);
            }

            customerRecords.add(new CustomerRecordVO(customerId,map));
        }

        preference = new CustomerPreference(typeValue);
        result = preference.classify(customerRecords);
        preferenceVOs = new ArrayList<>(result.size());
        averageValues = new ArrayList<>();
        /*
        find the feature of the result and transport it to PreferenceVO
         */
        for (ArrayList<float[]> oneType : result ){

            float[] unit = new float[CommodityType.values().length];
            for (int i = 0; i < unit.length; i++){
                unit[i] = 0;
                for(float[] other : oneType){
                    unit[i] += other[i];
                }
                unit[i] = unit[i]/oneType.size();
            }

            averageValues.add(unit);
            int maxIndex = 0;
            int secondIndex = 0;
            float max = unit[0];
            float second = unit[0];
            for (int i = 1; i < unit.length; i++){
                if(unit[i]>max) {
                    max = unit[i];
                    maxIndex = i;
                }
            }
            for (int i = 1; i < unit.length; i++){
                if(second<max){
                    if(second<unit[i]){
                        secondIndex = i;
                        second = unit[i];
                    }
                }
                else {
                    secondIndex = i;
                    second = unit[i];
                }

            }

            CommodityType preferType =CommodityType.values()[maxIndex];
            CommodityType secondType = CommodityType.values()[secondIndex];
            PreferenceVO vo = new PreferenceVO(preferType,secondType,(oneType.size()+0.0)/customerRecords.size());
            preferenceVOs.add(vo);
        }

        return preferenceVOs;
    }

    @Override
    public Collection<PreferenceVO> getDetailedPrefer(Long regionId, CommodityType type, CommodityType secondType) {
        if(result==null){
            classifyCustomer(regionId, 4);
        }

        int index = 0;
        for(; index < preferenceVOs.size(); index++){
            if(preferenceVOs.get(index).type==type&& preferenceVOs.get(index).second==secondType){
                break;
            }
        }

        List<PreferenceVO> detailedPrefers = new ArrayList<>();
        float[] values = averageValues.get(index);
        for (int i = 0; i < values.length; i++){
            detailedPrefers.add(new PreferenceVO(CommodityType.values()[i],values[i]));
        }


        return detailedPrefers;
    }

    @Override
    public Collection<Set<CommodityType>> getSuitSet(Long regionId) {

        if(result==null){
            classifyCustomer(regionId,4);
        }

        List<Set<CommodityType>> suitSets = new ArrayList<>();

        int index = 0;
        for (int i = 0; i<preferenceVOs.size(); i++){

            // get the best group index
            if(preferenceVOs.get(index).percentage
                    <preferenceVOs.get(i).percentage){
                index = i;
            }

            List<PreferenceVO> detailedPrefers = new ArrayList<>();
            float[] values = averageValues.get(i);
            for (int j = 0; j < values.length; j++){
                detailedPrefers.add(new PreferenceVO(CommodityType.values()[j],values[j]));
            }
            Set<CommodityType> commodityTypes = getTopGroup(detailedPrefers);
            suitSets.add(commodityTypes);

        }

        /*
        copy the ordered sets
         */
        groupSets = new ArrayList<>(suitSets);

//        suitSets.stream().forEach(o -> System.err.println(Set2String.set2String(o)));
        /*
        put the best group in first
         */
        Set<CommodityType> tmp = suitSets.get(0);
        suitSets.set(0,suitSets.get(index));
        suitSets.set(index,tmp);

        return suitSets;
    }

    @Override
    public Set<CommodityType> getOneSet(Long regionId, CommodityType type,CommodityType secondType) {
        if(result==null){
            classifyCustomer(regionId, 4);
        }

        int index = 0;
        for(; index < preferenceVOs.size(); index++){
            if(preferenceVOs.get(index).type==type&&preferenceVOs.get(index).second==secondType){
                break;
            }
        }

        Set<CommodityType> set = groupSets.get(index);

        return set;
    }

    /**
     * to get the set of commodity that satisfy condition of high popularity
     *
     * @param preferenceVOs
     * @return
     */
    private Set<CommodityType> getTopGroup(List<PreferenceVO> preferenceVOs){

        Set<CommodityType> result = new HashSet<>();

        preferenceVOs = preferenceVOs
                .stream()
                .sorted(((o1, o2) -> o1.percentage<o2.percentage?1:-1))
                .collect(Collectors.toCollection(ArrayList::new));

        /**
         * get the top commodity that sales well
         * when number of types is more than 3 or sum of percentages more than 80%, end
         *
         */
        double sum = 0;
        for (PreferenceVO vo : preferenceVOs){
//            System.err.println(vo.type+","+vo.percentage);
            result.add(vo.type);
            sum+=vo.percentage;

            if(result.size()>2||sum>0.7){
                break;
            }

//            if(result.size()==1&&sum>0.7){
//                break;
//            }
        }


        return result;
    }
    /**
     * test
     * @param args
     */
    public static void main(String[] args) throws IOException{
        MemberMarketImpl memberMarket = new MemberMarketImpl();
        Collection<PreferenceVO> preferenceVOs = memberMarket.classifyCustomer(Long.valueOf(1),5);
        preferenceVOs.stream().forEach(o -> System.out.println(o.type+"  ,  "+o.percentage));


    }


}

