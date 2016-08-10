package jp.co.worksap.intern.util;

import jp.co.worksap.intern.constants.Constants;
import jp.co.worksap.intern.writer.IResultWriter;
import jp.co.worksap.intern.writer.ResultWriterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * for produce data, have no relation with function
 *
 * Created by yuminchen on 16/7/24.
 */
public final class DataFactory {

    private DataFactory(){

    }

    private static String fileName = "RECORD_MST.csv";
    private static IResultWriter writer = new ResultWriterImpl(
            Constants.DEFAULT_CSV_FOLDER +
                    fileName);

    public static void recordProducer(){
        String[] dates = {"2016-7-6", "2016-7-10","2016-7-14","2016-7-18","2016-7-22","2016-7-26"};
        double[] bias = {1.2,1.7,-1.0,2,1,-0.5};

        String[] types = {"popcorn", "sodas", "juice", "chips", "doll", "glasses", "toy"};
        double[] moneys = {15.8, 8.8, 9.8, 6.5, 20.5, 50.0, 40.0};

        List<String[]> records = new ArrayList<>();
        records.add(new String[]{
                "customerId",
                "cinemaId",
                "type",
                "number",
                "money",
                "date"
        });

        for(int i = 0; i< 288;i++){
            Long cid = Math.round( 1+ Math.random() * 49);
            int cidChoose = (int)(1+Math.random()*7);
            Long mid = Long.valueOf(cidChoose);
            int choose =(int) (Math.random() * 7);

            int number = (int) (Math.random()*5+1);
            int index = (int)( Math.random()*6);
            double money = number*(moneys[choose]+index);
            records.add(new String[]{cid+"",mid+"",types[choose],number+"",money+"",dates[index]});
        }

        for(int i = 0; i< 354;i++){
            Long cid = Math.round( 49+Math.random() * 125);

            int cidChoose = (int)(8+Math.random()*9);
            Long mid = Long.valueOf(cidChoose);

            int choose =(int) (Math.random() * 7);
            int number = (int) (Math.random()*5+1);
            int index = (int)( Math.random()*6);
            double money = number*(moneys[choose]+index);
            records.add(new String[]{cid+"",mid+"",types[choose],number+"",money+"",dates[index]});
        }

        for(int i = 0; i< 163;i++){
            Long cid = Math.round( 175+ Math.random() * 64);
            int cidChoose = (int)(17+Math.random()*3);
            Long mid = Long.valueOf(cidChoose);
            int choose =(int) (Math.random() * 7);

            int number = (int) (Math.random()*5+1);
            int index = (int)( Math.random()*6);
            double money = number*(moneys[choose]+index);
            records.add(new String[]{cid+"",mid+"",types[choose],number+"",money+"",dates[index]});
        }

        for(int i = 0; i< 123 ;i++){
            Long cid = Math.round( 240 + Math.random() * 41);
            int cidChoose = (int)(20+Math.random()*2);
            Long mid = Long.valueOf(cidChoose);
            int choose =(int) (Math.random() * 7);

            int number = (int) (Math.random()*5+1);
            int index = (int) (Math.random()*6);
            double money = number*(moneys[choose]+index);
            records.add(new String[]{cid+"",mid+"",types[choose],number+"",money+"",dates[index]});
        }

        writer.writeResult(records);

    }

    public static void main(String[] args) {
        recordProducer();
    }
//
}
