package com.wolfbeacon.dao;

import com.wolfbeacon.model.Hackathon;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
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
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hackathon> query = cb.createQuery(Hackathon.class);
        Root<Hackathon> HackathonRoot = query.from(Hackathon.class);
        query.select(HackathonRoot);

        Predicate predicate = null;
        Path<Date> startDatePath = HackathonRoot.<Date>get("startDate");
        if (startDate != null) {
            predicate = cb.greaterThanOrEqualTo(startDatePath, startDate);
        }
        if (endDate != null) {
            Predicate additionalPredicate = cb.lessThanOrEqualTo(HackathonRoot.<Date>get("endDate"), startDate);
            if (predicate == null) {
                predicate = additionalPredicate;
            } else {
                predicate = cb.and(predicate, additionalPredicate);
            }
        }
        query.where(predicate);

        if (sortBy == null || !sortBy.equals("distance")) {
            query.orderBy(cb.asc(startDatePath));
        }
        
        return entityManager.createQuery(query).setMaxResults(count).getResultList();

    }
}
