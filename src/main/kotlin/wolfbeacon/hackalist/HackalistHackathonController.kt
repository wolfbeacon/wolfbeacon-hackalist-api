package wolfbeacon.hackalist


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat

/**
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
 *   "id": 20160916552226390,
 *   "title": "Hack the North",
 *   "eventLink": "https://hackthenorth.com/",
 *   "startDate": "2016-09-15",
 *   "endDate": "2016-09-17",
 *   "lastUpdatedDate": "2016-11-13T00:00:00.000+0000",
 *   "year": 2016,
 *   "location": "Waterloo, ON, Canada",
 *   "host": "Hack the North",
 *   "length": 36,
 *   "size": "1000",
 *   "travel": true,
 *   "prize": true,
 *   "highSchoolers": true,
 *   "cost": "free",
 *   "facebookLink": "https://www.facebook.com/hackthenorth",
 *   "twitterLink": "https://twitter.com/hackthenorth",
 *   "googlePlusLink": "",
 *   "imageLink": "https://scontent.xx.fbcdn.net/v/t1.0-1/p100x100/13501875_1423460301013883_426092165374437510_n.png?oh=23ff635c68eee2faa74b376adc7982fd&oe=58CE1F88",
 *   "latitude": 43.4642578,
 *   "longitude": -80.5204096,
 *   "notes": ""
 *   },
 * ...
 *}
 */

/**
 * @api {get} /hackalist/ping/ Get Hackalist Service
 * @apiName PingHackalist
 * @apiGroup Hackalist
 *
 * @apiParamExample Sample Request
 * https://api.wolfbeacon.com/v1/hackalist/ping
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