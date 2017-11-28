package wolfbeacon.hackalist.util

import wolfbeacon.hackalist.HackalistHackathon
import java.util.*


class CoordinateComparator(private val lat1: Double, private val lon1: Double) : Comparator<HackalistHackathon> {
    private val memoDistances: MutableMap<Long, Double?>

    init {
        memoDistances = HashMap()
    }

    override fun compare(h1: HackalistHackathon, h2: HackalistHackathon): Int {
        if (!memoDistances.containsKey(h1.id)) {
            memoDistances.put(h1.id, distance(h1.latitude, h1.longitude))
        }
        if (!memoDistances.containsKey(h2.id)) {
            memoDistances.put(h2.id, distance(h2.latitude, h2.longitude))
        }

        return memoDistances[h1.id]!!.compareTo(memoDistances[h2.id]!!)

    }

    //Refer to http://www.geodatasource.com/
    private fun distance(lat2: Double?, lon2: Double?): Double {
        if (lat2 == null || lon2 == null) {
            return Integer.MAX_VALUE.toDouble()
        }
        val theta = lon1 - lon2
        var dist: Double? = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist!!)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515
        dist *= 1.609344
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180 / Math.PI
    }

}