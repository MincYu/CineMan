package jp.co.worksap.intern.util;

import java.time.LocalDate;
import java.util.IllegalFormatConversionException;

/**
 * make it convenient to converse String and LocalDate
 *
 * Created by yuminchen on 16/7/24.
 */
public final class DateTypeConverter {

    private DateTypeConverter(){}

    public static String localDate2String(LocalDate date){
        return date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
    }

    public static LocalDate string2LocalDate(String dateString){

        String[] splDate = dateString.split("-");

        if (splDate==null||splDate.length<3){
            throw new IllegalArgumentException("the format of date String is wrong "+ dateString);
        }

        return LocalDate.of(Integer.valueOf(splDate[0]),
                Integer.valueOf(splDate[1]),
                Integer.valueOf(splDate[2]));
    }


    public static void main(String[] args) {
        System.out.println(localDate2String(LocalDate.of(2016,3,12)));
        System.out.println(string2LocalDate("2016-3-12"));
    }

}
