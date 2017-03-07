package com.wolfbeacon.api.v1;

import com.wolfbeacon.model.HackalistHackathon;
import com.wolfbeacon.service.HackalistHackathonService;
import com.wolfbeacon.util.CoordinateComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/hackathon/")
public class HackathonController {

    @Autowired
    HackalistHackathonService hackalistHackathonService;

    @RequestMapping("list/hackalist-hackathons")
    public List<HackalistHackathon> getHackalistHackathons(HttpServletRequest request, HttpServletResponse response,
                                                           @RequestParam(value = "start-date", required = false) String startDate,
                                                           @RequestParam(value = "end-date", required = false) String endDate,
                                                           @RequestParam(value = "sort-by", required = false) String sortBy,
                                                           @RequestParam(value = "latitude", required = false) Double latitude,
                                                           @RequestParam(value = "longitude", required = false) Double longitude,
                                                           @RequestParam(value = "count", required = false) Integer count) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = null, parsedEndDate = null;
        if (startDate != null) {
            parsedStartDate = format.parse(startDate);
        }
        if (endDate != null) {
            parsedEndDate = format.parse(endDate);
        }
        List<HackalistHackathon> hackalistHackathonList = hackalistHackathonService.getHackathonsBetweenDate(parsedStartDate, parsedEndDate, sortBy, count);
        if (sortBy != null && sortBy.equals("distance") && latitude != null && longitude != null) {
            Collections.sort(hackalistHackathonList, new CoordinateComparator(latitude, longitude));
        }
        return hackalistHackathonList;
    }
}