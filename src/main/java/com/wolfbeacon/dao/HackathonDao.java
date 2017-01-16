package com.wolfbeacon.dao;

import com.wolfbeacon.model.Hackathon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HackathonDao extends CrudRepository<Hackathon, Long>, HackathonDaoExtension {

}