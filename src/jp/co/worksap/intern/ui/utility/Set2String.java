package jp.co.worksap.intern.ui.utility;

import jp.co.worksap.intern.entities.commodity.CommodityType;

import java.util.Set;

/**
 * Created by yuminchen on 16/7/27.
 */
public final class Set2String {

    public static String set2String(Set<CommodityType> set){

        String result = "";
        for (CommodityType type : set){
            result = result+type.name()+",";
        }
        return result.substring(0,result.length()-1);

    }
}
