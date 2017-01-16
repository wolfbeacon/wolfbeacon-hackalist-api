package com.wolfbeacon.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MonthUtils {

    private Map<String, String> monthNameToMonthNumber;
    private Map<Integer, String> monthNumberToMonthName;
    private Map<Integer, String> IntegerMonthNoToStringMonthNo;

    public MonthUtils() {

        monthNameToMonthNumber = new LinkedHashMap<>();
        monthNameToMonthNumber.put("January", "01");
        monthNameToMonthNumber.put("February", "02");
        monthNameToMonthNumber.put("March", "03");
        monthNameToMonthNumber.put("April", "04");
        monthNameToMonthNumber.put("May", "05");
        monthNameToMonthNumber.put("June", "06");
        monthNameToMonthNumber.put("July", "07");
        monthNameToMonthNumber.put("August", "08");
        monthNameToMonthNumber.put("September", "09");
        monthNameToMonthNumber.put("October", "10");
        monthNameToMonthNumber.put("November", "11");
        monthNameToMonthNumber.put("December", "12");
        
        monthNumberToMonthName = new TreeMap<>();
        monthNumberToMonthName.put(1, "January");
        monthNumberToMonthName.put(2, "February");
        monthNumberToMonthName.put(3, "March");
        monthNumberToMonthName.put(4, "April");
        monthNumberToMonthName.put(5, "May");
        monthNumberToMonthName.put(6, "June");
        monthNumberToMonthName.put(7, "July");
        monthNumberToMonthName.put(8, "August");
        monthNumberToMonthName.put(9, "September");
        monthNumberToMonthName.put(10, "October");
        monthNumberToMonthName.put(11, "November");
        monthNumberToMonthName.put(12, "December");

        IntegerMonthNoToStringMonthNo = new TreeMap<>();
        IntegerMonthNoToStringMonthNo.put(1, "01");
        IntegerMonthNoToStringMonthNo.put(2, "02");
        IntegerMonthNoToStringMonthNo.put(3, "03");
        IntegerMonthNoToStringMonthNo.put(4, "04");
        IntegerMonthNoToStringMonthNo.put(5, "05");
        IntegerMonthNoToStringMonthNo.put(6, "06");
        IntegerMonthNoToStringMonthNo.put(7, "07");
        IntegerMonthNoToStringMonthNo.put(8, "08");
        IntegerMonthNoToStringMonthNo.put(9, "09");
        IntegerMonthNoToStringMonthNo.put(10, "10");
        IntegerMonthNoToStringMonthNo.put(11, "11");
        IntegerMonthNoToStringMonthNo.put(12, "12");
        
    }

    public String getMonthNumberFromMonthName(String month) {
        return monthNameToMonthNumber.get(month);
    }

    public String getMonthNameFromMonthNumber(Integer number) {
        return monthNumberToMonthName.get(number);
    }

    public String getStringMonthNumberFromIntegerMonthNumber(Integer month) {
        return IntegerMonthNoToStringMonthNo.get(month);
    }
    
    public List<String> getMonthNameList() {
        List<String> monthList = new ArrayList<>();
        for (Integer monthNo : monthNumberToMonthName.keySet()) {
            monthList.add(monthNumberToMonthName.get(monthNo));
        }
        return monthList;
    }
}
