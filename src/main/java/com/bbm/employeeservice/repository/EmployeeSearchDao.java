package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Employee;
import com.bbm.employeeservice.model.dto.SearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeSearchDao {

    private final EntityManager entityManager;

    public List<Employee> findAllBySimpleQuery(String firstname,
                                               String lastname,
                                               String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        // SELECT * FROM employee
        Root<Employee> root = criteriaQuery.from(Employee.class);

        // prepare WHERE clause
        Predicate firstnamePredicate = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("firstname")), "%" + firstname.toLowerCase() + "%");
        Predicate lastnamePredicate = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("lastname")), "%" + lastname.toLowerCase() + "%");
        Predicate emailPredicate = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        Predicate firstnameorLastnamePredicate = criteriaBuilder.or(
                firstnamePredicate, lastnamePredicate);

        // query => SELECT * FROM employee WHERE firstname like '%an%' OR lastname like '%mk%'
        // AND email like '%ema%'
        criteriaQuery.where(criteriaBuilder.and(firstnameorLastnamePredicate, emailPredicate));
        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Employee> findAllByCriteria(SearchRequest request) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        Root<Employee> root = criteriaQuery.from(Employee.class);
        if (request.getFirstname() != null) {
            Predicate firstnamePredicate = criteriaBuilder.like(criteriaBuilder.lower(
                    root.get("firstname")), "%" + request.getFirstname().toLowerCase() + "%");
            predicates.add(firstnamePredicate);
        }

        if (request.getLastname() != null) {
            Predicate lastnamePredicate = criteriaBuilder.like(criteriaBuilder.lower(
                    root.get("lastname")), "%" + request.getLastname().toLowerCase() + "%");
            predicates.add(lastnamePredicate);
        }

        if (request.getEmail() != null) {
            Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(
                    root.get("email")),"%" + request.getEmail().toLowerCase() + "%");
            predicates.add(emailPredicate);
        }

        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
