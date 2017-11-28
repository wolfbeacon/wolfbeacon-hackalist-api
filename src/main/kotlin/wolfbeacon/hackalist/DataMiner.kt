package wolfbeacon.hackalist

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import wolfbeacon.hackalist.util.MonthUtils
import java.net.URL
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


@Component
@Transactional
class DataMiner {

    @Autowired
    lateinit var hackalistHackathonRepository: HackalistHackathonRepository

    @Value("\${GOOGLE_MAPS_API_KEY}")
    lateinit var GOOGLE_MAPS_API_KEY: String

    @Value("\${START_YEAR}")
    lateinit var START_YEAR: String

    @Value("\${START_MONTH}")
    lateinit var START_MONTH: String

    private val HACKALIST_API_ROUTE = "http://www.hackalist.org/api/1.0/"
    private var haveStoredOlderMonths = false

    private val monthUtils = MonthUtils()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @Async
    @Scheduled(initialDelay = 1000, fixedDelay = 43200000)
    fun updateHackalistHackathonData() {
        //Default Start dates of the hackalist API
        var currYear = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().year
        var currMonth = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().monthValue

        // if previous months data stored, refresh only current month
        if (!haveStoredOlderMonths) {
            currYear = START_YEAR.toInt()
            currMonth = START_MONTH.toInt()
            haveStoredOlderMonths = true
        }

        var res = true
        while (res) {
            try {

                // Convert month name to month number
                val monthNumber = monthUtils.getStringMonthNumberFromIntegerMonthNumber(currMonth)

                // Make Request to Hackalist API
                val client = OkHttpClient()
                val request = Request.Builder().url(HACKALIST_API_ROUTE + Integer.toString(currYear) + "/" + monthNumber + ".json").build()
                print(request)
                val response: Response = client.newCall(request).execute()

                if (response.code() == 200) {
                    val jsonData = response.body()!!.string()
                    val json = JSONObject(jsonData)

                    // Hackathons as entries in list, with list accessed by Month
                    val hackathonsThisMonth = json.getJSONArray(monthUtils.getMonthNameFromMonthNumber(currMonth))

                    for (i in 0..hackathonsThisMonth.length() - 1) {

                        val currHackathon = hackathonsThisMonth.getJSONObject(i)

                        val title = parseTitle(currHackathon)
                        val startDate = parseStartDate(currHackathon)
                        val endDate = parseEndDate(currHackathon)
                        val location = parseLocation(currHackathon)
                        val eventLink = parseEventLink(currHackathon)
                        val host = parseHost(currHackathon)
                        val length = parseLength(currHackathon)
                        val size = parseSize(currHackathon)
                        val travel = parseTravel(currHackathon)
                        val prize = parsePrize(currHackathon)
                        val highSchoolers = parseHighSchoolers(currHackathon)
                        val cost = parseCost(currHackathon)
                        val facebookLink = parseFacebookLink(currHackathon)
                        val twitterLink = parseTwitterLink(currHackathon)
                        val googlePlusLink = parseGPlusLink(currHackathon)
                        val notes = parseNotes(currHackathon)


                        // Get year from JSON but don't add as a column
                        val year = parseYear(currHackathon)

                        // Extract Image URL from Facebook page
                        var imageLink: String? = if (facebookLink != null) obtainEventImageFromFacebook(facebookLink) else null

                        // If still not present, get from twitter
                        if (imageLink == null && twitterLink != null) {
                            imageLink = obtainEventImageFromTwitter(twitterLink)
                        }

                        // Latitude and Longitude
                        val coordinates = obtainLocationCoordinates(location)
                        val latitude = coordinates[0]
                        val longitude = coordinates[1]

                        val lastUpdatedTime = fetchUpdatedTimestamp()

                        // Generate ID as year + month + day + |year| where + is concatenate
                        val id = generateIDForHackathon(currHackathon, title, year)

                        // Create Object
                        val thisHackalistHackathon = HackalistHackathon(id, title, eventLink, startDate, endDate, lastUpdatedTime, year, location, host, length, size, travel, prize, highSchoolers, cost, facebookLink, twitterLink, googlePlusLink, imageLink, latitude, longitude, notes)

                        // UPDATE DB
                        hackalistHackathonRepository.save(thisHackalistHackathon)
                    }
                } else { // 404 INDICATES END OF RESULTS
                    throw Exception("End of Results Reached")
                }

                currMonth++
                if (currMonth == 13) {
                    currMonth = 1
                    currYear++
                }
            } catch (e: Exception) {
                e.printStackTrace()
                res = false
            }
        }
    }

    private fun generateIDForHackathon(currHackathon: JSONObject, title: String, year: Int): Long {
        val startDateParts = currHackathon.getString("startDate").split(" ")
        val currStartDate = startDateParts[1]
        val currStartMonth = monthUtils.getMonthNumberFromMonthName(startDateParts[0])
        val idStr = Integer.toString(year) + currStartMonth + currStartDate + Math.abs(title.hashCode())
        return idStr.toLong()
    }

    @Throws(Exception::class)
    private fun parseTitle(currHackathon: JSONObject): String {
        return currHackathon.getString("title")
    }

    @Throws(Exception::class)
    private fun parseStartDate(currHackathon: JSONObject): Date {
        val year = currHackathon.getString("year")
        val startDateParts = currHackathon.getString("startDate").split(" ")
        val currStartDate = startDateParts[1]
        val currStartMonth = monthUtils.getMonthNumberFromMonthName(startDateParts[0])
        return dateFormat.parse("$year-$currStartMonth-$currStartDate")
    }

    @Throws(Exception::class)
    private fun parseEndDate(currHackathon: JSONObject): Date {
        val year = currHackathon.getString("year")
        val endDateParts = currHackathon.getString("endDate").trim().split(" ")
        val currEndDate = endDateParts[1]
        val currEndMonth = monthUtils.getMonthNumberFromMonthName(endDateParts[0])
        return dateFormat.parse("$year-$currEndMonth-$currEndDate")
    }

    @Throws(Exception::class)
    private fun parseYear(currHackathon: JSONObject): Int {
        return Integer.parseInt(currHackathon.getString("year"))
    }

    @Throws(Exception::class)
    private fun parseLocation(currHackathon: JSONObject): String {
        return currHackathon.getString("city")
    }

    private fun parseHost(currHackathon: JSONObject): String {
        return currHackathon.getString("host")
    }

    private fun parseLength(currHackathon: JSONObject): Int? {
        var length: Int? = null
        if (currHackathon.has("length") && currHackathon.getString("length") != "unknown") {
            val lengthStr = currHackathon.getString("length")
            length = 0
            try {
                var lengthStrI = ""
                for (j in 0..lengthStr.length - 1) {
                    if (lengthStr[j].toInt() >= 48 && lengthStr[j].toInt() <= 57) {
                        lengthStrI += lengthStr[j]
                    }
                }
                length = if (lengthStrI != "") Integer.parseInt(lengthStrI) else null
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return length
    }

    private fun parseCost(currHackathon: JSONObject): String? {
        val cost: String? = if (currHackathon.has("cost")) currHackathon.getString("cost") else null
        return cost
    }

    private fun parseHighSchoolers(currHackathon: JSONObject): Boolean? {
        var highSchoolers: Boolean? = null
        if (currHackathon.has("highSchoolers")) {
            val highSchoolersStr = currHackathon.getString("highSchoolers")
            if (highSchoolersStr != "unknown") {
                highSchoolers = (highSchoolersStr == "yes")
            }
        }
        return highSchoolers
    }

    private fun parseTravel(currHackathon: JSONObject): Boolean? {
        var travel: Boolean? = null
        if (currHackathon.has("travel")) {
            val travelStr = currHackathon.getString("travel")
            if (travelStr != "unknown") {
                travel = travelStr == "yes"
            }
        }
        return travel
    }

    private fun parsePrize(currHackathon: JSONObject): Boolean? {
        var prize: Boolean? = null
        if (currHackathon.has("prize")) {
            val prizeStr = currHackathon.getString("prize")
            if (prizeStr != "unknown") {
                prize = prizeStr == "yes"
            }
        }
        return prize
    }

    private fun parseSize(currHackathon: JSONObject): String? {
        val size: String? = if (currHackathon.has("size")) currHackathon.getString("size") else null
        return size
    }

    private fun parseEventLink(currHackathon: JSONObject): String {
        return currHackathon.getString("url")
    }

    private fun parseFacebookLink(currHackathon: JSONObject): String? {
        val facebookLink: String? = if (currHackathon.has("facebookURL")) currHackathon.getString("facebookURL") else null
        return facebookLink
    }

    private fun parseTwitterLink(currHackathon: JSONObject): String? {

        val twitterLink: String? = if (currHackathon.has("twitterURL")) currHackathon.getString("twitterURL") else null
        return twitterLink
    }

    private fun parseGPlusLink(currHackathon: JSONObject): String? {
        val googlePlusLink: String? = if (currHackathon.has("googlePlusURL")) currHackathon.getString("googlePlusURL") else null
        return googlePlusLink
    }

    private fun parseNotes(currHackathon: JSONObject): String? {
        var notes: String? = if (currHackathon.has("notes")) currHackathon.getString("notes") else null
        if (notes != null && notes.length > 255) {
            notes = notes.substring(0, 255)
        }
        return notes
    }

    private fun fetchUpdatedTimestamp(): Date {
        return Calendar.getInstance().time
    }

    // Obtain Event Coordinates through Google Maps Geocoding API
    private fun obtainLocationCoordinates(location: String): Array<Double?> {
        val ans = arrayOf<Double?>(null, null)
        try {
            val context = GeoApiContext.Builder().apiKey(GOOGLE_MAPS_API_KEY).build()
            val results = GeocodingApi.geocode(context, location).await()
            val latLng = results[0].geometry.location
            ans[0] = latLng.lat
            ans[1] = latLng.lng
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ans
    }

    // Get the Event thumbnail from the Facebook Graph API
    private fun obtainEventImageFromFacebook(passedFacebookLink: String): String? {
        var facebookLink = passedFacebookLink
        // Currently not working with events, only pages
        try {
            if (facebookLink.contains("https://www.")) {
                facebookLink = facebookLink.replace("https://www.", "https://graph.")
            } else if (facebookLink.contains("https://facebook")) {
                facebookLink = facebookLink.replace("https://facebook", "https://graph.facebook")
            }

            var toAppend = ""
            if (facebookLink[facebookLink.length - 1] != '/') {
                toAppend += "/"
            }
            toAppend += "picture?type=normal"

            //get redirected link
            facebookLink += toAppend

            val con = URL(facebookLink).openConnection()
            con.connect()
            con.getInputStream()
            facebookLink = con.getURL().toString()

            return facebookLink
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // Accessible directly from Twitter
    private fun obtainEventImageFromTwitter(passedTwitterLink: String): String? {
        var twitterLink = passedTwitterLink
        try {
            // Returns a redirected URL directly to the image
            if (twitterLink[twitterLink.length - 1] != '/') {
                twitterLink += "/"
            }
            twitterLink += "profile_image?size=normal"

            // Get redirected link
            val con = URL(twitterLink).openConnection()
            con.connect()
            con.getInputStream()
            twitterLink = con.url.toString()

            return twitterLink
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}
