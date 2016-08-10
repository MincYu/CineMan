package jp.co.worksap.intern.entities.commodity;

import jp.co.worksap.intern.constants.Constants;
import jp.co.worksap.intern.entities.AbstractDTOReader;
import jp.co.worksap.intern.util.DateTypeConverter;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by yuminchen on 16/7/24.
 */
public class RecordDTOReader extends AbstractDTOReader<RecordDTO>{

    private static final int COLUMN_INDEX_CUSTOMER_ID = 0;
    private static final int COLUMN_INDEX_CINEMA_ID = 1;
    private static final int COLUMN_INDEX_TYPE = 2;
    private static final int COLUMN_INDEX_NUMBER = 3;
    private static final int COLUMN_INDEX_MONEY = 4;
    private static final int COLUMN_INDEX_DATE = 5;


    private String fileAddress = Constants.DEFAULT_CSV_FOLDER
            + "RECORD_MST.csv";

    public RecordDTOReader() throws IOException{
        super(RecordDTOReader.class.getName());
        super.init();
    }

    @Override
    protected RecordDTO convertArrayToDTO(String[] value) throws IOException {
        Long customerId = Long.valueOf(value[COLUMN_INDEX_CUSTOMER_ID]);
        Long cinemaId = Long.valueOf(value[COLUMN_INDEX_CINEMA_ID]);
        CommodityType type = CommodityType.valueOf(value[COLUMN_INDEX_TYPE]);
        int number = Integer.valueOf(value[COLUMN_INDEX_NUMBER]);
        double money = Double.valueOf(value[COLUMN_INDEX_MONEY]);
        LocalDate date = DateTypeConverter.string2LocalDate(value[COLUMN_INDEX_DATE]);
        return new RecordDTO(customerId,cinemaId,type,number,money,date);
    }

    @Override
    protected String getFileArress() {
        return fileAddress;
    }
}
