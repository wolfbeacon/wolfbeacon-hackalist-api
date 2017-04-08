package com.wolfbeacon.service;

import com.wolfbeacon.dao.HackalistHackathonDao;
import com.wolfbeacon.model.HackalistHackathon;
import com.wolfbeacon.util.CoordinateComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class HackalistHackathonServiceImpl implements HackalistHackathonService {

    @Autowired
    HackalistHackathonDao hackalistHackathonDao;

    @Override
    public List<HackalistHackathon> getHackathonsBetweenDate(Date startDate, Date endDate, String sortBy, Integer count, Double latitude, Double longitude) {
        List<HackalistHackathon> fullHackalistHackathonList = hackalistHackathonDao.findAll();
        List<HackalistHackathon> queriedHackathons = new ArrayList<>();
        // Get Hackathons between dates
        for (HackalistHackathon currHackathon : fullHackalistHackathonList) {
            if (currHackathon.getStartDate().compareTo(startDate) >= 0 && currHackathon.getEndDate().compareTo(endDate) <= 0) {
                queriedHackathons.add(currHackathon);
            }
        }
        // Sort by date or distance
        if (sortBy == null || sortBy.equals("date") || !sortBy.equals("distance")) {
            Collections.sort(queriedHackathons, new Comparator<HackalistHackathon>() {
                @Override
                public int compare(HackalistHackathon h1, HackalistHackathon h2) {
                    return h1.getStartDate().compareTo(h2.getStartDate());
                }
            });
        } else {
            if (latitude != null && longitude != null) {
                Collections.sort(queriedHackathons, new CoordinateComparator(latitude, longitude));
            }
        }
        // Adjust count
        if (count != null) {
            queriedHackathons = queriedHackathons.subList(0, count);
        }
        return queriedHackathons;
    }

    @Override
    public void saveHackathon(HackalistHackathon hackalistHackathon) {
        hackalistHackathonDao.save(hackalistHackathon);
    }
}
