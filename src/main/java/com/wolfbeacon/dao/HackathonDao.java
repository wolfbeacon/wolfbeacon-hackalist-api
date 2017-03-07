package com.wolfbeacon.dao;

import com.wolfbeacon.model.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface HackathonDao extends JpaRepository<Hackathon, Long>, HackathonDaoExtension {

}