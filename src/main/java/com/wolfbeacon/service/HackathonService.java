package com.wolfbeacon.service;

import com.wolfbeacon.model.Hackathon;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface HackathonService {
    List<Hackathon> getHackathonsBetweenDate(Date startDate, Date endDate, String sortBy, Integer count);
}
