package com.wolfbeacon.api;

import com.wolfbeacon.model.HackalistHackathon;
import com.wolfbeacon.service.HackalistHackathonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @api {get} /hackalist-hackathons/ Get Hackalist Hackathons
 * @apiName GetHackalistHackathons
 * @apiGroup Hackalist
 * 
 * @apiParam {String} start-date (yyyy-MM-dd) Returns all the hackathons after this date. Eg: start-date=2015-10-10 Users unique ID.
 * @apiParam {String} end-date (yyyy-MM-dd) Returns all the hackathons before this date. Eg: end-date=2015-12-10
 * @apiParam {String="date","distance&latitude=12.345&longitude=67.890"} sort-by Sorts by date or radial distance (See sample request)
 *
 * @apiSampleRequest https://api.wolfbeacon.com/hackalist-hackathons?start-date=2016-01-01&end-date=2016-01-30&sort-by=distance&latitude=40.7127837&longitude=-74.00594130000002
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
@RestController
public class HackalistHackathonController {

    @Autowired
    HackalistHackathonService hackalistHackathonService;

    @CrossOrigin
    @RequestMapping("/hackalist-hackathons")
    public List<HackalistHackathon> getHackalistHackathons(HttpServletRequest request, HttpServletResponse response,
                                                           @RequestParam(value = "start-date") String startDate,
                                                           @RequestParam(value = "end-date") String endDate,
                                                           @RequestParam(value = "sort-by") String sortBy,
                                                           @RequestParam(value = "latitude", required = false) Double latitude,
                                                           @RequestParam(value = "longitude", required = false) Double longitude,
                                                           @RequestParam(value = "count", required = false) Integer count) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = format.parse(startDate);
        Date parsedEndDate = format.parse(endDate);
        return hackalistHackathonService.getHackathonsBetweenDate(parsedStartDate, parsedEndDate, sortBy, count, latitude, longitude);
    }

    @CrossOrigin
    @RequestMapping("/ping/wolfbeacon-hackalist-api")
    public String ping(HttpServletRequest request, HttpServletResponse response) {
        return "Ping successful";
    }
}
