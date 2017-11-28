package wolfbeacon.hackalist.util

import java.util.*

class MonthUtils {

    private val monthNameToMonthNumber: MutableMap<String, String>
    private val monthNumberToMonthName: MutableMap<Int, String>
    private val IntegerMonthNoToStringMonthNo: MutableMap<Int, String>

    init {

        monthNameToMonthNumber = LinkedHashMap()
        monthNameToMonthNumber.put("January", "01")
        monthNameToMonthNumber.put("February", "02")
        monthNameToMonthNumber.put("March", "03")
        monthNameToMonthNumber.put("April", "04")
        monthNameToMonthNumber.put("May", "05")
        monthNameToMonthNumber.put("June", "06")
        monthNameToMonthNumber.put("July", "07")
        monthNameToMonthNumber.put("August", "08")
        monthNameToMonthNumber.put("September", "09")
        monthNameToMonthNumber.put("October", "10")
        monthNameToMonthNumber.put("November", "11")
        monthNameToMonthNumber.put("December", "12")

        monthNumberToMonthName = TreeMap()
        monthNumberToMonthName.put(1, "January")
        monthNumberToMonthName.put(2, "February")
        monthNumberToMonthName.put(3, "March")
        monthNumberToMonthName.put(4, "April")
        monthNumberToMonthName.put(5, "May")
        monthNumberToMonthName.put(6, "June")
        monthNumberToMonthName.put(7, "July")
        monthNumberToMonthName.put(8, "August")
        monthNumberToMonthName.put(9, "September")
        monthNumberToMonthName.put(10, "October")
        monthNumberToMonthName.put(11, "November")
        monthNumberToMonthName.put(12, "December")

        IntegerMonthNoToStringMonthNo = TreeMap()
        IntegerMonthNoToStringMonthNo.put(1, "01")
        IntegerMonthNoToStringMonthNo.put(2, "02")
        IntegerMonthNoToStringMonthNo.put(3, "03")
        IntegerMonthNoToStringMonthNo.put(4, "04")
        IntegerMonthNoToStringMonthNo.put(5, "05")
        IntegerMonthNoToStringMonthNo.put(6, "06")
        IntegerMonthNoToStringMonthNo.put(7, "07")
        IntegerMonthNoToStringMonthNo.put(8, "08")
        IntegerMonthNoToStringMonthNo.put(9, "09")
        IntegerMonthNoToStringMonthNo.put(10, "10")
        IntegerMonthNoToStringMonthNo.put(11, "11")
        IntegerMonthNoToStringMonthNo.put(12, "12")

    }

    /*
     Return types are String? as the accessed key might point to no entry (null)
     See MutableMaps Generics Docs: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/get.html
    */

    fun getMonthNumberFromMonthName(month: String): String? {
        return monthNameToMonthNumber[month]
    }

    fun getMonthNameFromMonthNumber(number: Int?): String? {
        return monthNumberToMonthName.get(number)
    }

    fun getStringMonthNumberFromIntegerMonthNumber(month: Int?): String? {
        return IntegerMonthNoToStringMonthNo.get(month)
    }

    val monthNameList: List<String?>
        get() {
            val monthList = ArrayList<String?>()
            for (monthNo in monthNumberToMonthName.keys) {
                monthList.add(monthNumberToMonthName[monthNo])
            }
            return monthList
        }
}