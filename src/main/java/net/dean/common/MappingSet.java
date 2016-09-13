package net.dean.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Created by dean on 16/6/27.
 */
public class MappingSet {

    private MappingSet() throws Exception{
        throw new Exception("can't be instantiation");
    }

    private final static Set<String> districtSet = ImmutableSet.of(
            "上城","下城","西湖","拱墅","滨江","江干");

    public final static int RECORD_HOUR = 0;

    public static boolean isMainDistrict(String district){
        return districtSet.contains(district);
    }
    public static ImmutableMap<String, String> NUMBER_MAPPING = ImmutableMap.<String, String>builder()
            .put("numberone","1")
            .put("numbertwo","2")
            .put("numberthree","3")
            .put("numberfour","4")
            .put("numberfive","5")
            .put("numbersix","6")
            .put("numberseven","7")
            .put("numbereight","8")
            .put("numbernine","9")
            .put("numberzero","0")
            .put("numberdor",".")
            .put("numbbone","1")
            .put("numbbtwo","2")
            .put("numbbthree","3")
            .put("numbbfour","4")
            .put("numbbfive","5")
            .put("numbbsix","6")
            .put("numbbseven","7")
            .put("numbbeight","8")
            .put("numbbnine","9")
            .put("numbbzero","0")
            .put("numbbdor",".")
            .put("numbone","1")
            .put("numbtwo","2")
            .put("numbthree","3")
            .put("numbfour","4")
            .put("numbfive","5")
            .put("numbsix","6")
            .put("numbseven","7")
            .put("numbeight","8")
            .put("numbnine","9")
            .put("numbzero","0")
            .put("numbdor",".")
            .build();
}
