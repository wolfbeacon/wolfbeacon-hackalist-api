package com.wolfbeacon.service;

import com.wolfbeacon.dao.HackalistHackathonDao;
import com.wolfbeacon.model.HackalistHackathon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional
public class HackalistHackathonServiceImpl implements HackalistHackathonService {

    @Autowired
    HackalistHackathonDao hackalistHackathonDao;

    @Override
    public List<HackalistHackathon> getHackathonsBetweenDate(Date startDate, Date endDate, String sortBy, Integer count) {
        return hackalistHackathonDao.queryHackathonsBetweenDates(startDate, endDate, sortBy, count);
    }
}
