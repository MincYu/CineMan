package jp.co.worksap.intern.bl.membermarket;

import jp.co.worksap.intern.bl.utility.DataSource;
import jp.co.worksap.intern.bl.utility.KMeans;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.vo.CustomerRecordVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yuminchen on 16/7/26.
 */
public class CustomerPreference {

    private KMeans kMeans;

    /**
     * default value of k is 5
     *
     * @throws IOException
     */
    public CustomerPreference(){
        this(5);
    }

    public CustomerPreference(int k){
        this.kMeans = new KMeans(k);
    }


    public ArrayList<ArrayList<float[]>> classify(Collection<CustomerRecordVO> customerRecordVOs) {

//        CommodityType[] types = new CommodityType[CommodityType.values().length];
        ArrayList<float[]> dataSet = new ArrayList<>();
        int unionLength = CommodityType.values().length;

        for (CustomerRecordVO vo : customerRecordVOs) {

            float[] unit = new float[unionLength];
            for (int i = 0; i < unionLength; i++) {
                unit[i] = vo.percentageMap.get(CommodityType.values()[i]);
            }
            dataSet.add(unit);
        }

        kMeans.setDataSet(dataSet);
        kMeans.execute();

        ArrayList<ArrayList<float[]>> cluster = kMeans.getCluster();
        return cluster;
    }




}
