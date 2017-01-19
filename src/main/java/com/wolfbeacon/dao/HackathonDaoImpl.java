package com.wolfbeacon.dao;

import com.wolfbeacon.model.Hackathon;
import com.wolfbeacon.model.Hackathon_;
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
public class HackathonDaoImpl implements HackathonDaoExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Hackathon> queryHackathonsBetweenDates(Date startDate, Date endDate, String sortBy, Integer count) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hackathon> criteria = builder.createQuery(Hackathon.class);
        Root<Hackathon> hRoot = criteria.from(Hackathon.class);
        if (startDate != null) {
            criteria.where(builder.and(builder.greaterThanOrEqualTo(hRoot.get(Hackathon_.startDate), startDate)));
            if (sortBy == null || !sortBy.equals("distance")) {
                criteria.orderBy(builder.asc(hRoot.get(Hackathon_.startDate)));
            }
        }
        if (endDate != null) {
            criteria.where(builder.and(builder.lessThanOrEqualTo(hRoot.get(Hackathon_.endDate), startDate)));
        }
        Query query = entityManager.createQuery(criteria);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }
}