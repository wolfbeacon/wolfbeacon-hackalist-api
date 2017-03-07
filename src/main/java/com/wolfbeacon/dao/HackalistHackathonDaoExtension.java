package com.wolfbeacon.dao;

import com.wolfbeacon.model.HackalistHackathon;

import java.util.Date;
import java.util.List;

public interface HackalistHackathonDaoExtension {
    List<HackalistHackathon> queryHackathonsBetweenDates(Date startDate, Date endDate, String sortBy, Integer count);
}
