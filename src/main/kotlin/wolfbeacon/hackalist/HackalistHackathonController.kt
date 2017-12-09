package wolfbeacon.hackalist


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat

/**
 * @apiVersion 1.0.0
 * @api {get} /hackalist/hackathons/ Get Hackalist Hackathons
 * @apiName GetHackalistHackathons
 * @apiGroup Hackalist
 *
 * @apiParam {String} start-date (yyyy-MM-dd) Returns all the hackathons after this date. Eg: start-date=2015-10-10 Users unique ID.
 * @apiParam {String} end-date (yyyy-MM-dd) Returns all the hackathons before this date. Eg: end-date=2015-12-10
 * @apiParam {String="date","distance&latitude=12.345&longitude=67.890"} sort-by Sorts by date or radial distance (See sample request)
 *
 * @apiSampleRequest https://api.wolfbeacon.com/v1/hackalist/hackathons?start-date=2016-01-01&end-date=2016-01-30&sort-by=distance&latitude=40.7127837&longitude=-74.00594130000002
 *
 * @apiSuccessExample {json} Success-Response:
 * {
 * ...
 *   {
 *   	"id": 201701201955420960,
 *   	"title": "PennApps XV",
 *   	"eventLink": "http://pennapps.com/",
 *   	"startDate": "2017-01-20", 
 *   	"endDate": "2017-01-22",
 *   	"lastUpdatedDate": "2017-12-01T03:26:31.244+0000",
 *   	"year": 2017,
 *   	"location": "Philadelphia, PA, United States", 
 *   	"host": "University of Pennsylvania",
 *   	"length": 48,
 *   	"size": "unknown",
 *   	"travel": true,
 *   	"prize": true,
 *   	"highSchoolers": true,
 *   	"cost": "free",
 *   	"facebookLink": "https://www.facebook.com/pennapps/",
 *   	"twitterLink": "https://twitter.com/pennapps",
 *   	"googlePlusLink": "",
 *   	"imageLink": "https://pbs.twimg.com/profile_images/930281893752442880/EFCW9AZJ_normal.jpg",
 *   	"latitude": 39.9525839,
 *   	"longitude": -80.5204096,
 *   	"notes": ""
 *   },
 * ...
 *}
 */

/**
 * @apiVersion 1.0.0
 * @api {get} /hackalist/ping/ Ping Hackalist Service
 * @apiName PingHackalist
 * @apiGroup Hackalist
 *
 * @apiSuccessExample {json} Success-Response (HTTP/1.1 200 OK):
 * Ping Successful
 *}
 */

@RestController
@RequestMapping("/v1/hackalist")
class ArticleController {

    @Autowired
    lateinit var hackalistHackathonService: HackalistHackathonService

    @RequestMapping("/hackathons")
    @CrossOrigin
    @Throws(Exception::class)
    fun getHackalistHackathons(@RequestParam(value = "start-date") startDate: String,
                               @RequestParam(value = "end-date") endDate: String,
                               @RequestParam(value = "sort-by") sortBy: String,
                               @RequestParam(value = "latitude", required = false) latitude: Double?,
                               @RequestParam(value = "longitude", required = false) longitude: Double?,
                               @RequestParam(value = "count", required = false) count: Int?): List<HackalistHackathon> {

        val format = SimpleDateFormat("yyyy-MM-dd")
        val parsedStartDate = format.parse(startDate)
        val parsedEndDate = format.parse(endDate)

        return hackalistHackathonService.getHackathonsBetweenDatesByCoordinates(parsedStartDate, parsedEndDate, sortBy, count, latitude, longitude)
    }

    @CrossOrigin
    @RequestMapping("/ping")
    fun ping(): String {
        return "Ping successful"
    }

}