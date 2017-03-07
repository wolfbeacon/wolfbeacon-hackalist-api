package com.wolfbeacon.service;

import com.wolfbeacon.model.HackalistHackathon;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface HackalistHackathonService {
    List<HackalistHackathon> getHackathonsBetweenDate(Date startDate, Date endDate, String sortBy, Integer count);
}
