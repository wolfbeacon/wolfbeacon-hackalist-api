package com.wolfbeacon.api;

import com.wolfbeacon.model.HackalistHackathon;
import com.wolfbeacon.service.HackalistHackathonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class HackalistHackathonController {

    @Autowired
    HackalistHackathonService hackalistHackathonService;

    @RequestMapping("/api/hackalist-hackathon/list")
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

    @RequestMapping("/ping")
    public String ping(HttpServletRequest request, HttpServletResponse response) {
        return "Ping successful";
    }
}