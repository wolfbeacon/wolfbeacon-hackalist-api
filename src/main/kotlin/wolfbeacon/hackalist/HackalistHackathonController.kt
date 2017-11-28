package wolfbeacon.hackalist


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat


@RestController
@RequestMapping("/v1")
class ArticleController {

    @Autowired
    lateinit var hackalistHackathonService: HackalistHackathonService

    @RequestMapping("/hackalist-hackathons")
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

}