package com.wolfbeacon.dao;

import com.wolfbeacon.model.HackalistHackathon;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface HackalistHackathonDao extends JpaRepository<HackalistHackathon, Long> {

}