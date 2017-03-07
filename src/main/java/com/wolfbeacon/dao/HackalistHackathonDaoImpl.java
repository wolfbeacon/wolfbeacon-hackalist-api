package com.wolfbeacon.dao;

import com.wolfbeacon.model.HackalistHackathon;
import com.wolfbeacon.model.HackalistHackathon_;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Component
public class HackalistHackathonDaoImpl implements HackalistHackathonDaoExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<HackalistHackathon> queryHackathonsBetweenDates(Date startDate, Date endDate, String sortBy, Integer count) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HackalistHackathon> criteria = builder.createQuery(HackalistHackathon.class);
        Root<HackalistHackathon> hRoot = criteria.from(HackalistHackathon.class);
        if (startDate != null) {
            criteria.where(builder.and(builder.greaterThanOrEqualTo(hRoot.get(HackalistHackathon_.startDate), startDate)));
            if (sortBy == null || !sortBy.equals("distance")) {
                criteria.orderBy(builder.asc(hRoot.get(HackalistHackathon_.startDate)));
            }
        }
        if (endDate != null) {
            criteria.where(builder.and(builder.lessThanOrEqualTo(hRoot.get(HackalistHackathon_.endDate), startDate)));
        }
        Query query = entityManager.createQuery(criteria);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }
}