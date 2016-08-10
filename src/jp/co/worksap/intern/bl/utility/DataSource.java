package jp.co.worksap.intern.bl.utility;

import jp.co.worksap.intern.entities.cinema.CinemaMstDTO;
import jp.co.worksap.intern.entities.cinema.CinemaMstDTOReader;
import jp.co.worksap.intern.entities.commodity.RecordDTO;
import jp.co.worksap.intern.entities.commodity.RecordDTOReader;
import jp.co.worksap.intern.entities.customer.CustomerDTO;
import jp.co.worksap.intern.entities.customer.CustomerDTOReader;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.entities.region.RegionMstDTOReader;

import java.io.IOException;
import java.util.Collection;

/**
 * help business logical module get data from data layer
 * and hide the implement of date transport
 *
 * Created by yuminchen on 16/7/25.
 */
public class DataSource {

    private RecordDTOReader recordDTOReader;
    private RegionMstDTOReader regionMstDTOReader;
    private CinemaMstDTOReader cinemaMstDTOReader;
    private CustomerDTOReader customerDTOReader;

    public DataSource() throws IOException{
        this(new RecordDTOReader(),
                new RegionMstDTOReader(),
                new CinemaMstDTOReader(),
                new CustomerDTOReader());
    }

    /**
     *
     * @param recordDTOReader
     * @param regionMstDTOReader
     * @param cinemaMstDTOReader
     * @param customerDTOReader
     */
    public DataSource(RecordDTOReader recordDTOReader,
                      RegionMstDTOReader regionMstDTOReader,
                      CinemaMstDTOReader cinemaMstDTOReader,
                      CustomerDTOReader customerDTOReader) {
        this.recordDTOReader = recordDTOReader;
        this.regionMstDTOReader = regionMstDTOReader;
        this.cinemaMstDTOReader = cinemaMstDTOReader;
        this.customerDTOReader = customerDTOReader;
    }

    public Collection<RecordDTO> getRecord(){
        return recordDTOReader.getValues();
    }

    public Collection<RegionMstDTO> getRegion(){
        return regionMstDTOReader.getValues();
    }

    public Collection<CinemaMstDTO> getCinema(){
        return cinemaMstDTOReader.getValues();
    }

    public Collection<CustomerDTO> getCustomer(){
        return customerDTOReader.getValues();
    }


}
