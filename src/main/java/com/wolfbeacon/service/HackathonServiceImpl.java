package com.wolfbeacon.service;

import com.wolfbeacon.dao.HackathonDao;
import com.wolfbeacon.model.Hackathon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional
public class HackathonServiceImpl implements HackathonService {

    @Autowired
    HackathonDao hackathonDao;

    @Override
    public List<Hackathon> getHackathonsBetweenDate(Date startDate, Date endDate, String sortBy, Integer count) {
        return hackathonDao.queryHackathonsBetweenDates(startDate, endDate, sortBy, count);
    }
}
