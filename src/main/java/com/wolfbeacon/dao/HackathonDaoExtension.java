package com.wolfbeacon.dao;

import com.wolfbeacon.model.Hackathon;

import java.util.Date;
import java.util.List;

public interface HackathonDaoExtension {
    List<Hackathon> queryHackathonsBetweenDates(Date startDate, Date endDate, String sortBy, Integer count);
}
